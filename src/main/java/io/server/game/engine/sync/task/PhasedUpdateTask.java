package io.server.game.engine.sync.task;

import java.util.concurrent.Phaser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PhasedUpdateTask extends SynchronizationTask {

	private static final Logger logger = LogManager.getLogger();

	private final SynchronizationTask task;
	private final Phaser phaser;

	public PhasedUpdateTask(Phaser phaser, SynchronizationTask task) {
		this.task = task;
		this.phaser = phaser;
	}

	@Override
	public void run() {
		try {
			task.run();
		} catch (Exception ex) {
			logger.fatal(String.format("Error in %s", task.getClass().getSimpleName()), ex);
		}
		phaser.arriveAndDeregister();
	}

}
