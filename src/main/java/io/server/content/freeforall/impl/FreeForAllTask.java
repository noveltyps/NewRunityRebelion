package io.server.content.freeforall.impl;

import io.server.game.world.entity.mob.player.Player;

/**
 * Free For All Task Appending
 * 
 * @author Nerik#8690
 *
 */
public interface FreeForAllTask {

	/**
	 * Executes the given task
	 * 
	 * @param player
	 */
	void execute(Player player, String content);

}
