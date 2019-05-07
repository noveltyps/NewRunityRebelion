package io.server.content.skill.impl.cooking;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds the cooking data.
 * 
 * @author Daniel
 */
public enum CookData {
	RAW_MEAT(13199, 1, 34, 2142, 2146, 30.0D, 250), RAW_SHRIMP(317, 1, 34, 315, 323, 30.0D, 500),
	SARDINE(327, 1, 38, 325, 369, 40.0D, 750), ANCHOVIES(321, 1, 34, 319, 323, 30.0D, 1000),
	HERRING(345, 5, 41, 347, 353, 50.0D, 1250), MACKEREL(353, 10, 45, 355, 353, 60.0D, 1500),
	TROUT(335, 15, 50, 333, 343, 70.0D, 1750), COD(341, 18, 52, 339, 343, 75.0D, 2000),
	PIKE(349, 20, 53, 351, 343, 80.0D, 2250), SALMON(331, 25, 58, 329, 343, 90.0D, 2500),
	SLIMY_EEL(3379, 28, 58, 3381, 3383, 95.0D, 2750), TUNA(359, 30, 65, 361, 367, 100.0D, 3000),
	KARAMBWAN(3142, 30, 200, 3144, 3148, 190.0D, 3250), RAINBOW_FISH(10138, 35, 60, 10136, 10140, 110.0D, 3500),
	CAVE_EEL(5001, 38, 40, 4003, 5002, 115.0D, 3750), LOBSTER(377, 40, 74, 379, 381, 120.0D, 500),
	BASS(363, 43, 80, 365, 367, 130.0D, 5250), SWORDFISH(371, 45, 86, 373, 375, 140.0D, 6000),
	LAVA_EEL(2148, 53, 53, 2149, 3383, 30.0D, 700), MONKFISH(7944, 62, 92, 7946, 7948, 150.0D, 8000),
	SHARK(383, 80, 99, 385, 387, 210.0D, 10000), SEA_TURTLE(395, 82, 150, 397, 399, 212.0D, 12500),
	CAVEFISH(15264, 88, 150, 15266, 15268, 214.0D, 15000), MANTA_RAY(389, 91, 150, 391, 393, 216.0D, 17500),
	ANGLERFISH(13439, 84, 175, 13441, 13443, 220.0D, 22500), DARK_CRAB(11934, 90, 185, 11936, 11938, 225.0D, 25000);
	private final int item;

	private final int level;

	private final int cooked;

	private final double exp;

	private final int burnt;

	private final int noBurn;

	public int money;

	CookData(int item, int level, int noBurnrate, int cooked, int burnt, double exp, int money) {
		this.item = item;
		this.level = level;
		this.noBurn = noBurnrate;
		this.cooked = cooked;
		this.burnt = burnt;
		this.exp = exp;
		this.money = money;
	}

	public double getExp() {
		return exp;
	}

	public int getItem() {
		return item;
	}

	public int getMoney() {
		return money;
	}

	public int getLevel() {
		return level;
	}

	public int getCooked() {
		return cooked;
	}

	public int getNoBurn() {
		return noBurn;
	}

	public int getBurnt() {
		return burnt;
	}

	public static Optional<CookData> forId(int id) {
		return Arrays.stream(values()).filter(a -> a.item == id).findAny();
	}
}
