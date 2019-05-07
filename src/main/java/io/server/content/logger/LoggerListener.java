package io.server.content.logger;

import io.server.game.world.entity.mob.player.Player;

public interface LoggerListener {

	String getPath();
	
	void execute(Player player, Player other, String log);
}
