package io.server.content.donators;

/**
 * Holds all the donator bond data.
 *
 * @author Daniel.
 */
public enum DonatorBond {
	BOND_5(13189, 5, 100), BOND_10(13190, 10, 200), BOND_25(13191, 25, 500), BOND_50(13192, 50, 1000), BOND_100(13193, 100, 2000), BOND_200(13194, 200, 4000),
	BOND_500(13195, 500, 10000);

	/** The item identification of the donator bond. */
	public final int item;

	/** The amount of money spent that was required for this bond. */
	public final int moneySpent;

	/** The amount of credits this bond will give. */
	public final int credits;

	/** Constructs a new <code>DonatorBond</code>. */
	DonatorBond(int item, int moneySpent, int credits) {
		this.item = item;
		this.moneySpent = moneySpent;
		this.credits = credits;
	}

	/** Gets the bond data based on the item provided. */
	public static DonatorBond forId(int item) {
		for (DonatorBond bond : values()) {
			if (bond.item == item)
				return bond;
		}
		return null;
	}
}