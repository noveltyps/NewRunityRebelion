package io.server.content.skill.impl.woodcutting;

/**
 * Holds all the data for trees.
 *
 * @author Daniel
 */
public enum TreeData {
	// int level, int item, int replacement, int respawn, double experience, int
	// petRate, int logs, double success, int[] tree
	AFK_TREE(1, 2862, 5, 5, 5.0, 317_647, 20_000, 1.0, new int[] { 2092 }, 250),
	NORMAL_TREE(1, 1511, 1342, 15, 25.0D, 317_647, 1, 1.0, new int[] { 1276, 1278, 1279 }, 500),
	DYING_TREE(1, 1511, 3649, 15, 25.0D, 317_647, 1, 1.0, new int[] { 3648 }, 500),
	DEAD_TREE(1, 1511, 1341, 15, 25.0D, 317_647, 1, 1.0, new int[] { 1284 }, 500),
	DEAD_TREE1(1, 1511, 1351, 15, 25.0D, 317_647, 1, 1.0, new int[] { 1286 }, 500),
	DEAD_TREE2(1, 1511, 1347, 15, 25.0D, 317_647, 1, 1.0, new int[] { 1282, 1283 }, 500),
	DEAD_TREE3(1, 1511, 1352, 15, 25.0D, 317_647, 1, 1.0, new int[] { 1365 }, 500),
	DEAD_TREE4(1, 1511, 1353, 15, 25.0D, 317_647, 1, 1.0, new int[] { 1289 }, 500),
	DEAD_TREE5(1, 1511, 1285, 15, 25.0D, 317_647, 1, 1.0, new int[] { 1349 }, 500),
	ACHEY_TREE(1, 2862, 3371, 15, 25.0D, 317_647, 1, 1.0, new int[] { 2023 }, 600),
	OAK_TREE(15, 1521, 1356, 25, 37.5D, 361_146, 5, 0.8, new int[] { 1751 }, 750),
	WILLOW_TREE(30, 1519, 9471, 30, 67.5D, 289_286, 10, 0.6, new int[] { 1756, 1758, 1760 }, 1500),
	WILLOW_TREE1(30, 1519, 9711, 30, 67.5D, 289_286, 10, 0.6, new int[] { 1750 }, 1500),
	TEAK_TREE(35, 6333, 9037, 32, 85.0D, 251_528, 10, 0.6, new int[] { 9036 }, 1750),
	MAPLE_TREE(45, 1517, 9712, 35, 100.0D, 221_918, 10, 0.5, new int[] { 1759 }, 2500),
	HOLLOW_TREE(45, 3239, 4061, 35, 100.0D, 221_918, 10, 0.5, new int[] { 1752 }, 5000),
	MAHOGANY_TREE(50, 6332, 9035, 40, 130.0D, 175_227, 11, 0.4, new int[] { 9034 }, 7500),
	ARCTIC_PINE_TREE(54, 10810, 1356, 40, 150.0D, 155_227, 11, 0.4, new int[] { 3037 }, 7500),
	YEW_TREE(60, 1515, 9714, 45, 175.0D, 145_013, 12, 0.3, new int[] { 1753, 1754 }, 12500),
	MAGIC_TREE(75, 1513, 9713, 60, 250.0D, 72_321, 15, 0.1, new int[] { 1761 }, 25000),
	REDWOOD_TREE(90, 19669, 28860, 60, 250.0D, 28_321, 19, 0.1, new int[] { 28859 }, 27500);
	/** The level required to chop the tree. */
	public final int levelRequired;

	/** The product identification for chopping the tree. */
	public final int item;

	/** THe replacement object for chopping down the tree. */
	public final int replacement;

	/** The respawn time for the tree. */
	public final int respawn;

	/** The experience rewarded for chopping down the tree. */
	public final double experience;

	/** The pet rate for obtaining the tree. */
	public final int petRate;

	/** The log attribute for the tree. */
	public final int logs;

	/** The success rate for chopping down the tree. */
	public final double success;

	/** The tree identifications. */
	public final int[] tree;

	public int money;

	/** Constructs a new <enum>TreeData</enum>. */
	TreeData(int level, int item, int replacement, int respawn, double experience, int petRate, int logs,
			double success, int[] tree, int money) {
		this.levelRequired = level;
		this.item = item;
		this.replacement = replacement;
		this.respawn = respawn;
		this.experience = experience;
		this.petRate = petRate;
		this.tree = tree;
		this.logs = logs;
		this.success = success;
		this.money = money;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	/** Gets the tree data based on the object identification. */
	public static TreeData forId(int id) {
		for (TreeData data : TreeData.values())
			for (int object : data.tree)
				if (object == id)
					return data;
		return null;
	}
}