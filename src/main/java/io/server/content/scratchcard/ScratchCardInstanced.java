package io.server.content.scratchcard;

public class ScratchCardInstanced {

	private int buttonId, id;

	public ScratchCardInstanced(int buttonId, int id) {
		this.buttonId = buttonId;
		this.id = id;
	}

	public int getButtonId() {
		return buttonId;
	}

	public int getId() {
		return id;
	}

}
