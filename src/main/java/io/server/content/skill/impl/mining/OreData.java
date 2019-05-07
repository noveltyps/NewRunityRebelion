package io.server.content.skill.impl.mining;

/**
 * Holds all the ore data.
 *
 * @author Daniel
 */
public enum OreData {
	CLAY(434, 1, 5, 0, 7468, 15, 741_600, 5, 0.3, new int[] { 7454, 7487 }, 500),
	COPPER(436, 1, 17.5, 0, 7468, 15, 741_600, 5, 0.5, new int[] { 7484, 7453 }, 1000),
	TIN(438, 1, 17.5, 0, 7468, 15, 741_600, 5, 0.5, new int[] { 7485, 7486 }, 1500),
	IRON(440, 15, 35, 0, 7468, 20, 741_600, 8, 0.2, new int[] { 7488, 7455 }, 2000),
	SILVER(442, 20, 40, 0, 7468, 25, 741_600, 8, 0.2, new int[] { 7457, 7490 }, 2500),
	COAL(453, 30, 50, 0, 7468, 25, 290_640, 9, 0.2, new int[] { 7456, 7489 }, 3000),
	GOLD(444, 40, 65, 0, 7468, 30, 296_640, 7, 0.15, new int[] { 7491, 7458 }, 5000),
	MITHRIL(447, 55, 80, 0, 7468, 50, 148_320, 9, 0.05, new int[] { 7492, 7459 }, 10000),
	ADAMANTITE(449, 70, 95, 0, 7468, 75, 59_328, 11, 0.03, new int[] { 7493, 7460 }, 15000),
	RUNITE(451, 85, 125, 0, 7468, 100, 42_337, 13, 0.02, new int[] { 7494 }, 25000),
	RUNE_ESSENCE(1436, 1, 18.5, 0, -1, 20, 326_780, Integer.MAX_VALUE, 0.1, new int[] { 7494, 7471 }, 1000),
	GEM_ROCK(1623, 40, 85, 0, -1, 75, 326_780, 2, 0.2, new int[] { 9030 }, 25000),
	HERB3(203, 19, 19, 0, 8139, 15, 741_600, 5, 0.5, new int[] { 8001 }, 2500),

	GEM_ROCK_I(1623, 40, 65, 0, 7468, 135, 326_780, 2, 0.3, new int[] { 7463, 7464 }, 25000),;

	/** CHANGE THE GEM_ROCKS TO -1 As it was that BEFORE! **/

	/** The ore this node contains. */
	public final int ore;

	/** The minimum level to mine this node. */
	public final int level;

	/** The experience. */
	public final double experience;
	/** The farmexp. */
	public final double farmexp;

	/** The node replacement. */
	public final int replacement;

	/** The node respawn timer. */
	public final int respawn;

	/** The pet chance. */
	public final int pet;

	/** The amount of ores that this ore can give. */
	public final int oreCount;

	/** The success rate for the ore data. */
	public final double success;

	/** The object identification of this node. */
	public final int[] objects;

	public int money;

	/** Creates the node. */
	OreData(int ore, int level, double experience, double farmexp, int replacement, int respawn, int pet, int oreCount,
			double success, int[] objects, int money) {
		this.objects = objects;
		this.level = level;
		this.experience = experience;
		this.farmexp = farmexp;
		this.replacement = replacement;
		this.respawn = respawn;
		this.pet = pet;
		this.oreCount = oreCount;
		this.success = success;
		this.ore = ore;
		this.money = money;
	}

	/** Gets the ore data. */
	public static OreData forId(int id) {
		for (OreData data : OreData.values())
			for (int object : data.objects)
				if (object == id)
					return data;
		return null;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

}
