package io.server.game.event.impl;

import io.server.game.event.Event;
import io.server.game.world.items.Item;
import io.server.game.world.object.GameObject;

public class ItemOnObjectEvent implements Event {

	private final Item used;

	private final int slot;

	private final GameObject object;

	public ItemOnObjectEvent(Item used, int slot, GameObject object) {
		this.used = used;
		this.slot = slot;
		this.object = object;
	}

	public Item getUsed() {
		return used;
	}

	public int getSlot() {
		return slot;
	}

	public GameObject getObject() {
		return object;
	}

}
