package io.server.content.event.impl;

import io.server.content.event.InteractionEvent;
import io.server.game.world.items.Item;

public class SecondItemClickInteractionEvent extends ItemInteractionEvent {

	public SecondItemClickInteractionEvent(Item item, int slot) {
		super(InteractionEvent.InteractionType.SECOND_ITEM_CLICK, item, slot, 1);
	}
}
