package io.server.game.event.listener;

import io.server.game.event.Event;
import io.server.game.event.impl.log.LogEvent;

public final class WorldEventListener implements EventListener {

	@Override
	public void accept(Event event) {
		if (event instanceof LogEvent) {
			LogEvent logEvent = (LogEvent) event;
			logEvent.log();
		}
	}

}
