package io.server.game.world.entity.mob.player;

import io.server.util.Utility;

/**
 * A class which represents the definition of an account
 * 
 * @author Telopya <telopya@frostblades.org>
 *
 */
public class AccountDefinition {

	/**
	 * The account name of the {@link Player}
	 */
	private final String name;

	/**
	 * The displayed character name of the {@link Player}
	 */
	private String characterName;

	/**
	 * The previous displayed character name of the {@link Player}
	 */
	private String previousName;

	/**
	 * The authority rank of the {@link Player}
	 */
	private PlayerRight authority;

	/**
	 * The total credits of the {@link Player}
	 */
	private int totalCredits;

	/**
	 * The spendable credits of the {@link Player}
	 */
	private int credits;

	/**
	 * If the player logged in with the master password
	 */
	private boolean master;

	public AccountDefinition(String name) {
		this.name = Utility.capitalizeFully(name);
		this.characterName = this.name;
		this.previousName = this.name;
		this.authority = PlayerRight.PLAYER;
		this.totalCredits = 0;
		this.credits = 0;
	}

	public String getAccountName() {
		return this.name;
	}

	public String getCharacterName() {
		return this.characterName;
	}

	public void setCharacterName(String name) {
		this.characterName = name;
	}

	public String getPreviousName() {
		return this.previousName;
	}

	public void setPreviousName(String name) {
		this.previousName = name;
	}

	public PlayerRight getAuthority() {
		return this.authority;
	}

	public void setAuthority(PlayerRight authority) {
		this.authority = authority;
	}

	public int getTotalCredits() {
		return this.totalCredits;
	}

	public void setTotalCredits(int credits) {
		this.totalCredits = credits;
	}

	public int getCredits() {
		return this.credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public boolean isMaster() {
		return this.master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof AccountDefinition)) {
			return false;
		}

		return name.equalsIgnoreCase(((AccountDefinition) obj).name);

	}

}
