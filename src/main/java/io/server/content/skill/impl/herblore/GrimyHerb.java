package io.server.content.skill.impl.herblore;

import java.util.Arrays;
import java.util.Optional;

import io.server.game.world.items.Item;

/**
 * Holds all the grimy herb data.
 *
 * @author Daniel
 * @author Michael | Chex
 */
public enum GrimyHerb {
	GUAM(199, 249, 1, 1.0), MARRENTILL(201, 251, 5, 1.1), TARROMIN(203, 253, 11, 1.3), HARRALANDER(205, 255, 20, 1.4),
	RANARR(207, 257, 25, 1.5), TOADFLAX(3049, 2998, 30, 1.6), SPIRITWEED(12174, 12172, 35, 1.7), IRIT(209, 259, 40, 1.8),
	WERGALI(14836, 14854, 30, 1.9), AVANTOE(211, 261, 48, 2.0), KWUARM(213, 263, 54, 2.1),
	SNAPDRAGON(3051, 3000, 59, 2.2), CADANTINE(215, 265, 65, 2.4), LANTADYME(2485, 2481, 67, 2.5),
	DWARFWEED(217, 267, 70, 2.6), TORSTOL(219, 269, 75, 2.7);

	/**
	 * The grimy herb.
	 */
	private int grimy;

	/**
	 * The clean herb.
	 */
	private int clean;

	/**
	 * The level to clean the herb.
	 */
	private int level;

	/**
	 * The experience for cleaning the herb.
	 */
	private double experience;

	/**
	 * The grimy herb data.
	 *
	 * @param grimy      The grimy herb.
	 * @param clean      The clean herb.
	 * @param level      The level required to clean the herb.
	 * @param experience The experience given for cleaning the herb.
	 */
	GrimyHerb(int grimy, int clean, int level, double experience) {
		this.grimy = grimy;
		this.clean = clean;
		this.level = level;
		this.experience = experience;
	}

	/**
	 * Gets the grimy herb.
	 *
	 * @return The grimy herb.
	 */
	public Item getGrimy() {
		return new Item(grimy);
	}

	/**
	 * Gets the clean herb.
	 *
	 * @return The clean herb.
	 */
	public Item getClean() {
		return new Item(clean);
	}

	/**
	 * Gets the experience for cleaning a herb.
	 *
	 * @return The experience.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the level to clean the herb.
	 *
	 * @return The level required.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the herb data from the item identification.
	 *
	 * @param id The item identification.
	 * @return The grimy herb data.
	 */
	public static Optional<GrimyHerb> forId(int id) {
		return Arrays.stream(values()).filter(a -> a.grimy == id).findAny();
	}
}
