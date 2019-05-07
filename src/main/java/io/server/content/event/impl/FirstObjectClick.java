package io.server.content.event.impl;

import io.server.game.world.object.GameObject;

public class FirstObjectClick extends ObjectInteractionEvent {

	public FirstObjectClick(GameObject object) {
		super(InteractionType.FIRST_CLICK_OBJECT, object, 0);
	}

}