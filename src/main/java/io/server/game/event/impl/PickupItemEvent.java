package io.server.game.event.impl;

import io.server.game.event.Event;
import io.server.game.world.items.Item;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.position.Position;

public class PickupItemEvent implements Event {

	private final GroundItem groundItem;

	public PickupItemEvent(GroundItem groundItem) {
		this.groundItem = groundItem;
	}

	public GroundItem getGroundItem() {
		return groundItem;
	}

	public Item getItem() {
		return groundItem.item;
	}

	public Position getPosition() {
		return groundItem.getPosition();
	}

}
