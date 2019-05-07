package io.server.game.task.impl;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.util.Stopwatch;

public final class SystemUpdateEvent extends Task {

	private static final Logger logger = LogManager.getLogger();

	private final boolean restart;

	private final int miliseconds;

	private final Stopwatch stopwatch = Stopwatch.start();

	private long temp;

	public SystemUpdateEvent(int miliseconds, boolean restart) {
		super(true, 0);
		this.miliseconds = miliseconds;
		this.restart = restart;
	}

	@Override
	public void execute() {
		final long ds = miliseconds - stopwatch.elapsedTime(TimeUnit.MILLISECONDS) - 1;

		if (temp != ds && ds >= 0) {
			logger.info(String.format("Restarting in: %d seconds.", ds/1000));
		}

		if (stopwatch.elapsed(miliseconds, TimeUnit.MILLISECONDS)) {
			cancel();
		}

		temp = ds;
	}

	@Override
	protected void onCancel(boolean logout) {
		if (restart) {
			World.restart();
		} else {
			World.shutdown();
		}
	}

}
