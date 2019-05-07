package io.server.content.skill.impl.herblore;

import java.util.Arrays;

import io.server.game.world.items.Item;

/**
 * Holds all the finished potion data.
 *
 * @author Adam
 */
public enum FinishedPotion implements Potion {
	ATTACK_POTION(121, 91, 221, 1, 50, 500), ANTIPOISON(175, 93, 235, 5, 60, 1000),
	STRENGTH_POTION(115, 95, 225, 12, 70, 1250), RESTORE_POTION(127, 97, 223, 22, 75, 1500),
	ENERGY_POTION(3010, 97, 1975, 26, 80, 1750), DEFENCE_POTION(133, 99, 239, 30, 85, 2000),
	AGILITY_POTION(3034, 3002, 2152, 34, 90, 2250), COMBAT_POTION(9741, 97, 9736, 36, 95, 2500),
	PRAYER_POTION(139, 99, 231, 38, 105, 3000), SUPER_ATTACK(145, 101, 221, 45, 115, 3250),
	VIAL_OF_STENCH(18661, 101, 1871, 46, 0, 3500), FISHING_POTION(181, 101, 235, 48, 119, 3750),
	SUPER_ENERGY(3018, 103, 2970, 52, 120, 4000), SUPER_STRENGTH(157, 105, 225, 55, 137.5, 4250),
	WEAPON_POISON(187, 105, 241, 60, 138, 4500), SUPER_RESTORE(3026, 3004, 223, 63, 142.5, 4750),
	SUPER_DEFENCE(163, 107, 239, 66, 150, 5000), ANTIFIRE(2454, 2483, 241, 69, 157.5, 7500),
	RANGING_POTION(169, 109, 245, 72, 162.5, 10000), MAGIC_POTION(3042, 2483, 3138, 76, 172.5, 15000),
	ZAMORAK_BREW(189, 111, 247, 78, 175, 17500), SARADOMIN_BREW(6687, 3002, 6693, 81, 180, 20000),
	SUPER_COMBATPOTION(12695, 9739, 269, 90, 215.5, 22500), SUPER_ANTIFIRE(21978, 2454, 21975, 92, 221.5, 25000),
	EXTENDED_ANTIFIRE(11951, 21978, 11994, 95, 235.5, 25000),

	;
	/**
	 * The finished potion product.
	 */
	private final int product;

	/**
	 * The finished potion ingredients.
	 */
	private final Item[] ingredients;

	/**
	 * The level required to make the potion.
	 */
	private final int level;

	/**
	 * The experience rewarded for the potion.
	 */
	private final double experience;

	public int money;

	/**
	 * Constructs a new <code>FinishedPotion</code>.
	 *
	 * @param product          The product item.
	 * @param unfinishedPotion The unfinished potion item.
	 * @param ingredient       The ingredients needed for the potion.
	 * @param level            The level required.
	 * @param experience       The experience rewarded.
	 */
	FinishedPotion(int product, int unfinishedPotion, int ingredient, int level, double experience, int money) {
		this.product = product;
		this.ingredients = new Item[] { new Item(unfinishedPotion), new Item(ingredient) };
		this.level = level;
		this.experience = experience;
		this.money = money;
	}

	/**
	 * Gets the finished potion data.
	 *
	 * @param use  The item being used.
	 * @param with The item being used with.
	 * @return The finished potion data.
	 */
	public static FinishedPotion get(Item use, Item with) {
		for (final FinishedPotion potion : values()) {
			if (potion.ingredients[0].equals(use) && potion.ingredients[1].equals(with)
					|| potion.ingredients[1].equals(use) && potion.ingredients[0].equals(with)) {
				return potion;
			}
		}
		return null;
	}

	@Override
	public int getAnimation() {
		return 363;
	}

	@Override
	public double getExperience() {
		return experience;
	}

	@Override
	public Item[] getIngredients() {
		return ingredients;
	}

	@Override
	public int getLevel() {
		return level;
	}

	public int getMoney() {
		return money;
	}

	@Override
	public Item getProduct() {
		return new Item(product);
	}

	@Override
	public String toString() {
		return "FinishedPotion[product: " + getProduct() + ", level: " + level + ", experience: " + experience
				+ ", ingredients: " + Arrays.toString(ingredients) + "]";
	}
}
