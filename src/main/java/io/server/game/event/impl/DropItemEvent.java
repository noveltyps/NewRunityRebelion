package io.server.game.event.impl;

import io.server.game.event.Event;
import io.server.game.world.items.Item;
import io.server.game.world.position.Position;

public class DropItemEvent implements Event {

	private final Item item;
	private final int slot;
	private final Position position;

	public DropItemEvent(Item item, int slot, Position position) {
		this.item = item;
		this.slot = slot;
		this.position = position;
	}

	public Item getItem() {
		return item;
	}

	public int getSlot() {
		return slot;
	}

	public Position getPosition() {
		return position;
	}

}
