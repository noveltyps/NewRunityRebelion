package io.server.content.skill.impl.thieving;

import java.util.Arrays;
import java.util.Optional;

import io.server.game.world.items.Item;

/**
 * Holds all the data for thieving stalls.
 *
 * @author Daniel
 * @edited by Adam_#6723
 */
public enum StallData {
	FOOD1(11730, new Item(3162), 1, 25, 275), GENERAL1(11732, new Item(1887), 40, 50, 760),
	CRAFTING1(11734, new Item(1692), 50, 80, 1453), MAGIC1(4877, new Item(8788), 75, 125, 2546),
	// HERB1(8012, new Item(199), 1, 10, 100),//guam
	SCIMITAR1(11731, new Item(1617), 85, 250, 3860),
	 FOOD(4875, new Item(3162), 1, 18, 275),
	    GENERAL(4876, new Item(1887), 25, 25, 760),
	    CRAFTING(4874, new Item(1635), 50, 33, 1453),
	    MAGIC(4877, new Item(8788), 75, 42, 2546),
	    SCIMITAR(4878, new Item(672), 85, 57, 3860);;

	/** The object identification. */
	private final int object;

	/** The item rewarded */
	private final Item item;

	/** The level required */
	private final int level;

	/** The experience rewarded. */
	private final int experience;

	/** The item value. */
	private final int value;

	/** Constructs a new <code>StallData<code> */
	StallData(int object, Item item, int level, int experience, int value) {
		this.object = object;
		this.item = item;
		this.level = level;
		this.experience = experience;
		this.value = value;
	}

	/** Gets the object identification. */
	public int getObject() {
		return object;
	}

	/** Gets the item reward. */
	public Item getItem() {
		return item;
	}

	/** Gets the level required. */
	public int getLevel() {
		return level;
	}

	/** Gets the reward value. */
	public int getValue() {
		return value;
	}

	/** Gets the experience rewarded. */
	public int getExperience() {
		return experience;
	}

	/** Gets the stall data base off object identification. */
	public static Optional<StallData> forId(int id) {
		return Arrays.stream(values()).filter(a -> a.object == id).findAny();
	}

	/** Checks if the item is a reward. */
	public static boolean isReward(Item item) {
		if (item == null)
			return false;
		return Arrays.stream(values()).anyMatch(i -> i.getItem().getId() == item.getId());
	}

	/** Gets the value of the reward. */
	public static final int getValue(Item item) {
		return Arrays.stream(values()).filter(i -> i.getItem().getId() == item.getId()).findAny().get().getValue();
	}
}