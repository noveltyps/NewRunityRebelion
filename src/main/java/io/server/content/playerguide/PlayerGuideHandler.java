package io.server.content.playerguide;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;

/**
 * Handles the interface opening
 * 
 * @author Nerik#8690
 *
 */
public class PlayerGuideHandler {

	/**
	 * Main Method, anything that is needed to appear on the interface should be
	 * called on here.
	 * 
	 * @param player
	 */
	public void open(Player player) {
		player.interfaceManager.open(56000);
		sendGuideNames(player);

	}

	/**
	 * Send's the strings to the interface.
	 * 
	 * @param player
	 */
	public void sendGuideNames(Player player) {
		for (int i = 0; i < PlayerGuideNames.values().length; i++) {
			player.send(new SendString(PlayerGuideNames.values()[i].name().replaceAll("_", " "), 56051 + i, true));
		}

	}
}
