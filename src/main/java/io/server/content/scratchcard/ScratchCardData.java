package io.server.content.scratchcard;

public enum ScratchCardData {

	ABYSSAL_WHIP(11739),  ABYSSAL_BLUDGEON(13687);
	// I actually did it fair so this are all legit random rolls just so u know
	private int displayId;

	// bruh fucking close ur shit fucking unorganised bitch, okay stfu and where do
	// you handle the bonus prize
	ScratchCardData(int displayId) {
		this.displayId = displayId;
	}

	public int getDisplayId() {
		return displayId;
	}

	public void setDisplayId(int displayId) {
		this.displayId = displayId;
	}

}
