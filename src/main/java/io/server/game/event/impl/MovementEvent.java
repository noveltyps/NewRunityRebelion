package io.server.game.event.impl;

import io.server.game.event.Event;
import io.server.game.world.position.Position;

public class MovementEvent implements Event {

	private final Position destination;

	public MovementEvent(Position destination) {
		this.destination = destination;
	}

	public Position getDestination() {
		return destination;
	}

}
