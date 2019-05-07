package io.server.game.event.impl;

import io.server.game.event.Event;

public class ButtonClickEvent implements Event {

	private final int button;

	public ButtonClickEvent(int button) {
		this.button = button;
	}

	public int getButton() {
		return button;
	}

}
