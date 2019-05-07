package io.server.content.skill.impl.fishing;

import static io.server.util.ItemIdentifiers.BIG_FISHING_NET;
import static io.server.util.ItemIdentifiers.DARK_FISHING_BAIT;
import static io.server.util.ItemIdentifiers.FISHING_BAIT;
import static io.server.util.ItemIdentifiers.FISHING_ROD;
import static io.server.util.ItemIdentifiers.FLY_FISHING_ROD;
import static io.server.util.ItemIdentifiers.HARPOON;
import static io.server.util.ItemIdentifiers.LOBSTER_POT;
import static io.server.util.ItemIdentifiers.SMALL_FISHING_NET;

import java.util.HashMap;
import java.util.Map;

public enum Fishable {
	SHRIMP(317, SMALL_FISHING_NET, 1, 10.0D, -1, 500), CRAYFISH(13435, 13431, 1, 10.0D, -1, 500),
	KARAMBWANJI(3150, SMALL_FISHING_NET, 5, 5.0D, -1, 750), SARDINE(327, FISHING_ROD, 5, 20.0D, FISHING_BAIT, 750),
	HERRING(345, FISHING_ROD, 10, 30.0D, FISHING_BAIT, 1000), ANCHOVIES(321, SMALL_FISHING_NET, 15, 40.0D, -1, 1250),
	MACKEREL(353, BIG_FISHING_NET, 16, 20.0D, -1, 1250), TROUT(335, FLY_FISHING_ROD, 20, 50.0D, 314, 1500),
	COD(341, BIG_FISHING_NET, 23, 45.0D, -1, 1750), PIKE(349, FISHING_ROD, 25, 60.0D, FISHING_BAIT, 2000),
	SLIMY_EEL(3379, FISHING_ROD, 28, 65.0D, FISHING_BAIT, 2500), SALMON(331, FLY_FISHING_ROD, 30, 70.0D, 314, 3000),
	FROG_SPAWN(5004, SMALL_FISHING_NET, 33, 75.0D, -1, 3500), TUNA(359, HARPOON, 35, 80.0D, -1, 5000),
	CAVE_EEL(5001, FISHING_ROD, 38, 80.0D, FISHING_BAIT, 5500),
	LOBSTER(378, LOBSTER_POT, 40, 90.0D, -1, 1500),
	BASS(363, BIG_FISHING_NET, 46, 100.0D, -1, 10000), SWORD_FISH(371, HARPOON, 50, 100.0D, -1, 12500),
	LAVA_EEL(2148, FISHING_ROD, 53, 30.0D, FISHING_BAIT, 13500),
	MONK_FISH(7944, SMALL_FISHING_NET, 62, 110.0D, -1, 15000), KARAMBWAN(3142, 3157, 65, 100.0D, -1, 17500),
	SHARK(383, HARPOON, 76, 125.0D, -1, 20000), SEA_TURTLE(395, -1, 79, 38.0D, -1, 25000),
	MANTA_RAY(389, HARPOON, 76, 125.0D, -1, 25000),
//rawFishid, toolid, levelneeded, baitneeded, experience recieved., int money
	DARK_CRAB(11934, 301, 85, 205.0D, DARK_FISHING_BAIT, 25000);

	public static void declare() {
		for (Fishable fishes : values())
			fish.put((int) fishes.getRawFishId(), fishes);
	}

	private short rawFishId;
	private short toolId;
	private short levelRequired;
	private short baitRequired;
	private double experienceGain;
	private static Map<Integer, Fishable> fish = new HashMap<>();
	private int money;

	public static Fishable forId(int rawFishId) {
		return fish.get(rawFishId);
	}

	Fishable(int rawFishId, int toolId, int levelRequired, double experienceGain, int baitRequired, int money) {
		this.rawFishId = ((short) rawFishId);
		this.toolId = ((short) toolId);
		this.levelRequired = ((short) levelRequired);
		this.experienceGain = experienceGain;
		this.baitRequired = ((short) baitRequired);
		this.money = money;
	}

	public short getBaitRequired() {
		return baitRequired;
	}

	public double getExperience() {
		return experienceGain;
	}

	public int getRawFishId() {
		return rawFishId;
	}

	public short getRequiredLevel() {
		return levelRequired;
	}

	public short getToolId() {
		return toolId;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}