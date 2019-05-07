package io.server.content.mysterybox;

import io.server.game.world.entity.mob.player.Player;

/**
 * Executes the selected mystery box
 * 
 * @author Nerik#8690
 *
 */
public class MysteryBoxExecuter {

	public static boolean execute(Player player, int box) {
		MysteryBoxListener Mystery_Box = null;
		for (MysteryBoxType type : MysteryBoxType.values()) {
			if (type.getId() == box) {
				Mystery_Box = MysteryBoxEvent.MYSTERY_BOX.get(type);
			}
		}
		if (Mystery_Box != null) {
			Mystery_Box.execute(player);
			return true;
		}
		return false;
	}
}
