package io.server.game.event.impl.log;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

import io.server.game.service.PostgreService;
import io.server.game.world.entity.mob.player.Player;

public class PrivateMessageChatLogEvent extends LogEvent {

	private final Player sender;
	private final Player receiver;
	private final String decoded;

	public PrivateMessageChatLogEvent(Player sender, Player receiver, String decoded) {
		this.sender = sender;
		this.receiver = receiver;
		this.decoded = decoded;
	}

	@Override
	public void onLog() throws Exception {
		JdbcSession session = new JdbcSession(PostgreService.getConnection());
		long logId = session.autocommit(false).sql("INSERT INTO log.log(log_time) VALUES (?::timestamp) RETURNING id")
				.set(dateTime).insert(new SingleOutcome<>(Long.class));

		session.sql("INSERT INTO log.pm_log(log_id, sender_id, receiver_id, message) VALUES (?, ?, ?, ?)").set(logId)
				.set(sender.getMemberId()).set(receiver.getMemberId()).set(decoded).execute().commit();
	}

}
