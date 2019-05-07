package io.server.content.prestige;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles the perk rewards from prestiging.
 *
 * @author Daniel.
 */
public enum PrestigePerk {
	ARROWHEAD("Arrowhead", "Using arrows will not break.", 6798),
	MASTERBAIRTER("Masterbaiter", "15% chance of catching extra fish.", 6799),
	DOUBLE_WOOD("Double wood", "15% chance to receive an additional log.", 6800),
	LITTLE_BIRDY("Little Birdy", "Increase the woodcutting rate of bird nest drops by 15%.", 6801),
	THE_ROCK("The Rock", "10% chance to receive an additional ore.", 6802),
	FLAME_ON("Flame On", "25% chance of burning an extra log.", 6803);

	/** The name of the perk. */
	public final String name;

	/** The description of the perk. */
	public final String description;

	/** The item identification of the perk. */
	public final int item;

	/** Construts a new <code>PrestigePerk</code>. */
	PrestigePerk(String name, String description, int item) {
		this.name = name;
		this.description = description;
		this.item = item;
	}

	public static Optional<PrestigePerk> forItem(int item) {
		return Arrays.stream(values()).filter(perk -> perk.item == item).findFirst();
	}

	public static Optional<PrestigePerk> forOrdinal(int ordinal) {
		return Arrays.stream(values()).filter(perk -> perk.ordinal() == ordinal).findFirst();
	}
}
