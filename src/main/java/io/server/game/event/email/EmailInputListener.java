package io.server.game.event.email;

import java.sql.SQLException;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendMessage;

public class EmailInputListener {

	public static void input(Player player) {
		player.locking.lock();
		player.dialogueFactory.sendStatement("For security purposes we would like u to put in your email!.")
				.onAction(() -> {
					player.send(new SendInputMessage("Enter your email:", 20, input -> {
						try {
							SqlEmailListener listener = new SqlEmailListener(player.getUsername(), input);
							listener.connect();
							player.send(new SendMessage("Thank you for securing your account"));
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
					}));
				}).execute();
	}

}
