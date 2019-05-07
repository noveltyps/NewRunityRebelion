package io.server.content.skill.impl.prayer;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds the Bone data.
 */
public enum BoneData {
	NORMAL_BONES(526, 4.5D), 
	WOLF_BONES(2859, 4.5D),
	BAT_BONES(530, 4.5D),
	BIG_BONES(532, 18.0D),
	BABYDRAGON_BONES(534, 30.0D),
	DRAGON_BONES(536, 72.0D),
	DAGG_BONES(6729, 105.0D), 
	OURG_BONES(4834, 97.0D),
	LONG_BONE(10976, 97.0D), 
	SUPERIOR_BONE(22124, 105.0D), 
	SKELETAL_WYVERN_BONES(6812, 95.0D),
	LAVA_DRAGON_BONES(11943, 100.0D);

	/* The id of the bone */
	private final int id;

	/* The experience of the bone */
	private final double experience;

	/* The bone data */
	BoneData(int id, double experience) {
		this.id = id;
		this.experience = experience;
	}

	/* Gets the id of the bone */
	public int getId() {
		return id;
	}

	/* Gets the experience of the bone */
	public double getExperience() {
		return experience;
	}

	/** Gets the bone data based on the item */
	public static Optional<BoneData> forId(int id) {
		return Arrays.stream(values()).filter(a -> a.id == id).findAny();
	}
}