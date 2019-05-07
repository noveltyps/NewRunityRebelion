package io.server.content.skill.impl.farming;

import java.util.HashMap;
import java.util.Map;

public enum Plants {

	GUAM(5291, 199, 4, 173, 170, 9, 5, SeedType.HERB, 11, 12, 5), // 24
	MARENTILL(5292, 201, 11, 173, 170, 14, 5, SeedType.HERB, 13, 15, 5), // 24
	TARROMIN(5293, 203, 18, 173, 170, 19, 5, SeedType.HERB, 16, 18, 5), // 42
	HARRALANDER(5294, 205, 25, 173, 170, 26, 5, SeedType.HERB, 21, 24, 5), // 89
	RANARR(5295, 207, 32, 173, 170, 32, 5, SeedType.HERB, 26, 30, 5), // 35957
	TOADFLAX(5296, 3050, 39, 173, 170, 36, 5, SeedType.HERB, 34, 38, 5), // 5296
	IRIT(5297, 209, 46, 173, 170, 44, 5, SeedType.HERB, 43, 48, 5), // 89
	AVANTOE(5298, 211, 53, 173, 170, 50, 5, SeedType.HERB, 54, 61, 5), // 805
	KWUARM(5299, 213, 68, 173, 170, 56, 5, SeedType.HERB, 69, 78, 5), // 1425
	SNAPDRAGON(5300, 3052, 75, 173, 170, 62, 5, SeedType.HERB, 87, 98, 5), // 43585
	CADANTINE(5301, 215, 82, 173, 170, 67, 5, SeedType.HERB, 106, 120, 5), // 285
	LANTADYME(5302, 2486, 89, 173, 170, 73, 5, SeedType.HERB, 134, 151, 5), // 967
	DWARF_WEED(5303, 217, 96, 173, 170, 79, 5, SeedType.HERB, 170, 192, 5), // 268
	TORSTOL(5304, 219, 103, 173, 170, 85, 5, SeedType.HERB, 199, 224, 5), // 19813

	POTATO(5318, 1942, 6, 0, 0, 1, 7, SeedType.ALLOTMENT, 8, 9, 4),
	ONION(5319, 1957, 13, 0, 0, 5, 7, SeedType.ALLOTMENT, 9, 10, 4),
	CABBAGE(5324, 1967, 20, 0, 0, 7, 7, SeedType.ALLOTMENT, 10, 11, 4),
	TOMATO(5322, 1982, 27, 0, 0, 12, 7, SeedType.ALLOTMENT, 12, 14, 4),
	SWEETCORN(5320, 5986, 34, 0, 0, 20, 7, SeedType.ALLOTMENT, 17, 19, 6),
	STRAWBERRY(5323, 5504, 43, 0, 0, 31, 6, SeedType.ALLOTMENT, 26, 29, 6),
	WATERMELON(5321, 5982, 52, 0, 0, 47, 4, SeedType.ALLOTMENT, 48, 54, 8),

	MARIGOLD(5096, 6010, 8, 0, 0, 2, 7, SeedType.FLOWER, 8, 47, 4),
	ROSEMARY(5097, 6014, 13, 0, 0, 11, 7, SeedType.FLOWER, 12, 66, 4),
	NASTURTIUM(5098, 6012, 18, 0, 0, 24, 7, SeedType.FLOWER, 19, 111, 4),
	WOAD(5099, 5738, 23, 0, 0, 25, 7, SeedType.FLOWER, 20, 115, 4),
	LIMPWURT(5100, 225, 28, 0, 0, 26, 7, SeedType.FLOWER, 21, 120, 4),// 156
	// WHITE_LILY(14589, 14583, 37, 0, 0, 52, 7, SeedType.FLOWER, 300, 20547, 4)
	;

	public final int seed;
	public final int harvest;
	public final int healthy;
	public final int diseased;
	public final int dead;
	public final int level;
	public final int minutes;
	public final byte stages;
	public final double plantExperience;
	public final double harvestExperience;
	public final SeedType type;

	private static final Map<Integer, Plants> plantsBySeed = new HashMap<Integer, Plants>();

	static {
		for (Plants plant : Plants.values()) {
			plantsBySeed.put(plant.seed, plant);
		}
	}

	public static Plants getPlantForSeed(int seed) {
		return plantsBySeed.get(seed);
	}

	public static boolean isSeed(int id) {
		return plantsBySeed.containsKey(id);
	}

	private Plants(int seed, int harvest, int config, int diseased, int dead, int level, int minutes, SeedType type,
			double plantExperience, double harvestExperience, int stages) {
		this.seed = seed;
		this.harvest = harvest;
		healthy = config;
		this.level = level;
		this.diseased = diseased;
		this.dead = dead;
		this.minutes = minutes;
		this.type = type;
		this.plantExperience = plantExperience;
		this.harvestExperience = harvestExperience;
		this.stages = ((byte) stages);
	}
}