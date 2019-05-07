package io.server.content.command;

import io.server.game.world.entity.mob.player.Player;

/**
 * Interface {@code execution} {@code canUse}
 * 
 * @author Nerik#8690
 *
 */
public interface Command {

	/**
	 * Handles the execution of the given command
	 * 
	 * @param player
	 */
	void execute(Player player, String command, String[] parts);

	/**
	 * Checks if the player has the recuirements to execute the command
	 * 
	 * @param player
	 * @return
	 */
	boolean canUse(Player player);

	String description();
}
