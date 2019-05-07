package io.server.game.event.impl.log;

import java.time.LocalDateTime;

import io.server.Config;
import io.server.game.event.Event;

public abstract class LogEvent implements Event {

	//private static final Logger logger = LogManager.getLogger();
	protected final LocalDateTime dateTime = LocalDateTime.now();

	public void log() {
		if (!Config.FORUM_INTEGRATION || !Config.LOG_PLAYER || true) {
			return;
		}
/*
		new Thread(() -> {
			try {
				onLog();
			} catch (Exception ex) {
				logger.error(String.format("Error logging %s", this.getClass().getSimpleName()), ex);
			}
		}).start();*/
	}

	public abstract void onLog() throws Exception;

}
