package io.server.content.event.impl;

import io.server.content.event.InteractionEvent;
import io.server.game.world.object.GameObject;

public class SecondObjectClick extends ObjectInteractionEvent {

	public SecondObjectClick(GameObject object) {
		super(InteractionEvent.InteractionType.SECOND_CLICK_OBJECT, object, 1);
	}

}