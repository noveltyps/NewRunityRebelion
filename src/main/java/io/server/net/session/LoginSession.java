package io.server.net.session;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.mindrot.jbcrypt.BCrypt;

import com.jcabi.jdbc.JdbcSession;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;
import io.server.Config;
import io.server.Server;
import io.server.game.service.ForumService;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.game.world.entity.mob.player.punishments.PunishmentExecuter;
import io.server.net.codec.game.GamePacketDecoder;
import io.server.net.codec.game.GamePacketEncoder;
import io.server.net.codec.login.LoginDetailsPacket;
import io.server.net.codec.login.LoginResponse;
import io.server.net.codec.login.LoginResponsePacket;
import io.server.util.Stopwatch;
import io.server.util.Utility;

/**
 * Represents a {@link Session} for authenticating users logging in.
 *
 * @author nshusa
 */
public final class LoginSession extends Session {

	private static final ConcurrentMap<String, FailedLoginAttempt> failedLogins = new ConcurrentHashMap<>();

	public LoginSession(Channel channel) {
		super(channel);
	}

	@Override
	public void handleClientPacket(Object o) {
		if (o instanceof LoginDetailsPacket) {
			LoginDetailsPacket packet = (LoginDetailsPacket) o;
			handleUserLoginDetails(packet);
		}
	}

	private void handleUserLoginDetails(LoginDetailsPacket packet) {
		final SocketChannel channel = (SocketChannel) packet.getContext().channel();

		LoginResponse response = LoginResponse.NO_RESPONSE;

		if (failedLogins.containsKey(packet.getUsername())) {
			FailedLoginAttempt failedAttempt = failedLogins.get(packet.getUsername());

			if (failedAttempt.getAttempt().get() >= Config.FAILED_LOGIN_ATTEMPTS
					&& !failedAttempt.getStopwatch().elapsed(Config.FAILED_LOGIN_TIMEOUT, TimeUnit.MINUTES)) {
				response = LoginResponse.LOGIN_ATTEMPTS_EXCEEDED;
			} else if (failedAttempt.getAttempt().get() >= Config.FAILED_LOGIN_ATTEMPTS
					&& failedAttempt.getStopwatch().elapsed(Config.FAILED_LOGIN_TIMEOUT, TimeUnit.MINUTES)) {
				failedLogins.remove(packet.getUsername());
			} else {
				failedAttempt.getAttempt().incrementAndGet();
			}
		}

		final Player player = new Player(packet.getUsername());
		player.setPassword(packet.getPassword());

		if (response == LoginResponse.NO_RESPONSE) {
			response = evaluate(player);
		}

		if (response == LoginResponse.INVALID_CREDENTIALS) {
			if (!failedLogins.containsKey(packet.getUsername())) {
				failedLogins.put(packet.getUsername(), new FailedLoginAttempt());
			}
			failedLogins.get(packet.getUsername());
		} else if (response == LoginResponse.NORMAL) {
			failedLogins.remove(packet.getUsername());
		}

		final ChannelFuture future = channel.writeAndFlush(new LoginResponsePacket(response, player.right, false));
		if (response != LoginResponse.NORMAL) {
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		future.awaitUninterruptibly();
		channel.pipeline().replace("login-encoder", "game-encoder", new GamePacketEncoder(packet.getEncryptor()));
		channel.pipeline().replace("login-decoder", "game-decoder", new GamePacketDecoder(packet.getDecryptor()));

		final GameSession session = new GameSession(channel, player);
		channel.attr(Config.SESSION_KEY).set(session);
		player.setSession(session);

		World.queueLogin(player);
	}

	private LoginResponse evaluate(Player player) {
		final String username = player.getUsername();
		final String password = player.getPassword();
		final boolean isEmail = username.contains("@");

		if (PunishmentExecuter.banned(player.getUsername())) {
			return LoginResponse.ACCOUNT_DISABLED;
		}

		if (PunishmentExecuter.IPBanned(host)) {
			return LoginResponse.ACCOUNT_DISABLED;
		}
		
		if (!Server.serverStarted.get()) {
			return LoginResponse.SERVER_BEING_UPDATED;
		}


		// the world is currently full
		if (World.getPlayerCount() == Config.MAX_PLAYERS) {
			return LoginResponse.WORLD_FULL;
		}

		// prevents users from logging in if the world is being updated
		if (World.update.get()) {
			return LoginResponse.SERVER_BEING_UPDATED;
		}

		if (isEmail) {
			if (!Config.FORUM_INTEGRATION) {
				return LoginResponse.BAD_USERNAME;
			}

			if (username.length() > Config.EMAIL_MAX_CHARACTERS || username.length() < Config.EMAIL_MIN_CHARACTERS) {
				return LoginResponse.INVALID_EMAIL;
			}

			// does email have illegal characters
			if (!(username.matches("^[a-zA-Z0-9.@]{1," + Config.EMAIL_MAX_CHARACTERS + "}$"))) {
				return LoginResponse.INVALID_CREDENTIALS;
			}
		} else if (username.length() < Config.USERNAME_MIN_CHARACTERS) {
			return LoginResponse.SHORT_USERNAME;
		} else if (username.length() > Config.USERNAME_MAX_CHARACTERS) {
			return LoginResponse.BAD_USERNAME;
		} else if (World.getPlayerByHash(Utility.nameToLong(username)).isPresent()) { // this user is already online
			return LoginResponse.ACCOUNT_ONLINE;
		} else if (!(username.matches("^[a-zA-Z0-9 ]{1," + Config.USERNAME_MAX_CHARACTERS + "}$"))) { // does username
																										// have illegal
																										// characters
			return LoginResponse.INVALID_CREDENTIALS;
		} else if (password.isEmpty() || password.length() > Config.PASSWORD_MAX_CHARACTERS) {
			return LoginResponse.INVALID_CREDENTIALS;
		}

		if (World.search(username).isPresent()) {
			return LoginResponse.ACCOUNT_ONLINE;
		}

		if (Config.FORUM_INTEGRATION) {
			LoginResponse response = LoginResponse.NORMAL;
			response = PlayerSerializer.load(player, password);

			return response;
		}

		LoginResponse response = PlayerSerializer.load(player, password);

		if (World.searchAll(player.getUsername()).isPresent()) {
			return LoginResponse.ACCOUNT_ONLINE;
		}

		return response;
	}

	@SuppressWarnings("unused")
	private LoginResponse authenticatedForumUser(Player player, boolean isEmail) {
		final String username = player.getUsername();
		try {
			final LoginResponse response = new JdbcSession(ForumService.getConnection()).sql(isEmail
					? "SELECT member_id, members_pass_hash, name, temp_ban FROM core_members WHERE UPPER(email) = ?"
					: "SELECT member_id, members_pass_hash, temp_ban FROM core_members WHERE UPPER(name) = ?")
					.set(username.toUpperCase()).select((rset, stmt) -> {
						if (rset.next()) {
							final int memberId = rset.getInt(1);
							final String passwordHash = rset.getString(2);
							final String forumUsername = isEmail ? rset.getString(3) : username;
							final long unixTime = rset.getLong(isEmail ? 4 : 3);

							if (isBanned(unixTime)) {
								return LoginResponse.ACCOUNT_DISABLED;
							}

							if (passwordHash.isEmpty()) {
								return LoginResponse.INVALID_CREDENTIALS;
							} else if (BCrypt.checkpw(player.getPassword(), passwordHash)) {
								player.setMemberId(memberId);
								player.setUsername(forumUsername);
								player.setPassword(passwordHash);
								return LoginResponse.NORMAL;
							} else {
								return LoginResponse.INVALID_CREDENTIALS;
							}
						}
						return LoginResponse.FORUM_REGISTRATION;
					});
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return LoginResponse.LOGIN_SERVER_OFFLINE;
	}

	private boolean isBanned(long unixTime) {
		// not banned
		if (unixTime == 0) {
			return false;
		} else if (unixTime == -1) { // perm ban
			return true;
		}

		final Date date = Date.from(Instant.ofEpochSecond(unixTime));

		final Date currentDate = Date.from(Instant.now());

		return date.after(currentDate);
	}

	/**
	 * A data class that represents a failed login attempt.
	 *
	 * @author nshusa
	 */
	private static class FailedLoginAttempt {

		private final AtomicInteger attempt = new AtomicInteger(0);
		private final Stopwatch stopwatch = Stopwatch.start();

		public AtomicInteger getAttempt() {
			return attempt;
		}

		public Stopwatch getStopwatch() {
			return stopwatch;
		}

	}

}
