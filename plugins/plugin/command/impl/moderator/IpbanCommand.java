package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.content.discord.DiscordConstant;
import io.server.content.discord.DiscordManager;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.punishments.PunishmentExecuter;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendMessage;

public class IpbanCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1].replaceAll("_", " "));

		World.search(name.toString()).ifPresent(other -> {
			if (PunishmentExecuter.IPBanned(other.lastHost)) {
				player.send(new SendMessage(
						"Player " + other.getUsername() + "'s IP is already banned. Command logs written."));
				return;
			}
			final String bannedIP = other.lastHost;
			PunishmentExecuter.addBannedIP(bannedIP);
			player.send(new SendMessage(
					"Player " + other.getUsername() + "'s IP was successfully banned. Command logs written."));
			for (Player playersToBan : World.getPlayers()) {
				if (playersToBan == null) {
					continue;
				}
				if (playersToBan.lastHost == bannedIP) {
					World.queueLogout(playersToBan);
					if (other.getUsername() != playersToBan.getUsername()) {
						player.send(new SendMessage("Player " + playersToBan.getUsername()
								+ " was successfully IPBanned. Command logs written."));
					}
				}
				player.send(new SendInputMessage("ENTER THE REASON FOR THE IPBAN", 100, string -> {
					try {
						System.err.println("" + string);
						new DiscordManager(DiscordConstant.STAFF_BAN_LOGS, player, other.getUsername() + " Has just been IP Banned by", "Reason: " + string).log();
					} catch (Exception e) {
						System.err.println("Erorr.");
					}
				}));
			}
		});
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isPriviledged(player);
	}

	@Override
	public String description() {
		return "Bans the specified player's IP address.";
	}
}
