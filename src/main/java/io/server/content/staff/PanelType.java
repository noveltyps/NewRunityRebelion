package io.server.content.staff;

/**
 * The panel types.
 * 
 * @author Daniel
 */
public enum PanelType {
	/**
	 * The information panel - displays strings such as username, password, host,
	 * etc.
	 */
	INFORMATION_PANEL(36700),

	/**
	 * The action panel - displays all the actions that can be executed.
	 */
	ACTION_PANEL(36500),

	/**
	 * The developer panel - displays all the fun and useful actions that only
	 * developers can execute.
	 */
	DEVELOPER_PANEL(36300);

	/** The identification of the panel. */
	private final int identification;

	/**
	 * Constructs a new <code>PanelType<code>.
	 * 
	 * @param identification The identification of the itemcontainer.
	 */
	PanelType(int identification) {
		this.identification = identification;
	}

	/**
	 * Gets the itemcontainer identification of the panel.
	 * 
	 * @return The itemcontainer identification.
	 */
	public int getIdentification() {
		return identification;
	}
}
