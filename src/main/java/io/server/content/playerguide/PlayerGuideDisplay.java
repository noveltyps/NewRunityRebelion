package io.server.content.playerguide;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;

/**
 * Handles the display on the interface
 * 
 * @author Nerik#8690
 *
 */
public class PlayerGuideDisplay {

	public static void display(Player player, int buttonId) {

		for (PlayerGuideData data : PlayerGuideData.values()) {
			if (data.getButtonId() == buttonId) {
				// General display
				player.send(new SendString("[Information]", 56006, true));
				player.send(new SendString("Difficulty: " + data.getDifficulty(), 56007, true));
				player.send(new SendString("Title: " + data.getTitle(), 56008, true));

				// Title display
				player.send(new SendString(data.getTitle(), 56012, true));

				// Text content display
				for (int i = 0; i < data.getContent().length; i++) {
					player.send(new SendString(data.getContent()[i], 56017 + i, true));
				}
			}
		}
	}
}
