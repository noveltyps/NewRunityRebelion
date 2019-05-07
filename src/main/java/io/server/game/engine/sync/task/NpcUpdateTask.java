package io.server.game.engine.sync.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendNpcUpdate;

public class NpcUpdateTask extends SynchronizationTask {

	private static final Logger logger = LogManager.getLogger();

	private final Player player;

	public NpcUpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (player == null) {
				return;
			}

			player.send(new SendNpcUpdate());
		} catch (Exception ex) {
			logger.fatal(String.format("Error in %s %s", this.getClass().getSimpleName(), player), ex);
		}
	}

}
