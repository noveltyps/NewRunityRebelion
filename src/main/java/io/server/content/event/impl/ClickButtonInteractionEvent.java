package io.server.content.event.impl;

import io.server.content.event.InteractionEvent;

public class ClickButtonInteractionEvent extends InteractionEvent {

	private final int button;

	public ClickButtonInteractionEvent(int button) {
		super(InteractionType.CLICK_BUTTON);
		this.button = button;
	}

	public int getButton() {
		return button;
	}
}