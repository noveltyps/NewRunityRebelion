package io.server.content.mysterybox;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;

/**
 * Interface mystery box system
 * 
 * @author Nerik#8690
 *
 */
public interface MysteryBoxListener {

	/**
	 * returns the loot
	 * 
	 * @return
	 */
	Item[] getCommon();

	Item[] getUncommon();

	Item[] getRare();

	Item[] getUltra();

	/**
	 * Executes special action
	 */
	void execute(Player player);
}
