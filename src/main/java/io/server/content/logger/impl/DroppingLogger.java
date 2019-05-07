package io.server.content.logger.impl;

import io.server.content.logger.LoggerListener;
import io.server.game.world.entity.mob.player.Player;

public class DroppingLogger implements LoggerListener {

	@Override
	public String getPath() {
		return "./data/content/logs";
	}

	@Override
	public void execute(Player player, Player other, String log) {
		// TODO Auto-generated method stub
		
	}

}
