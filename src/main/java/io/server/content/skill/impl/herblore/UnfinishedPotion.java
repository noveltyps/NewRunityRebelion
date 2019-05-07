package io.server.content.skill.impl.herblore;

import java.util.Arrays;

import io.server.game.world.items.Item;

public enum UnfinishedPotion implements Potion {
	GUAM_POTION(91, 249, 1, 20.0), MARRENTILL_POTION(93, 251, 5, 25.0), TARROMIN_POTION(95, 253, 12, 30),
	HARRALANDER_POTION(97, 255, 22, 35), RANARR_POTION(99, 257, 30, 40), TOADFLAX_POTION(3002, 2998, 34, 45),
	SPIRIT_WEED_POTION(12181, 12172, 40, 50), IRIT_POTION(101, 259, 45, 55), WERGALI_POTION(14856, 14854, 1, 60),
	AVANTOE_POTION(103, 261, 50, 65), KWUARM_POTION(105, 263, 55, 70), SNAPDRAGON_POTION(3004, 3000, 63, 75),
	CADANTINE_POTION(107, 265, 66, 80), LANTADYME(2483, 2481, 69, 85), DWARF_WEED_POTION(109, 267, 72, 90),
	TORSTOL_POTION(111, 269, 78, 95);
	
	private final int product;
	private final Item[] ingredients;
	private final int level;
	public final double experience;

	UnfinishedPotion(int product, int ingredient, int level, double experience) {
		this.product = product;
		this.ingredients = new Item[] { new Item(227), new Item(ingredient) };
		this.level = level;
		this.experience = experience;
	}

	public static UnfinishedPotion get(Item use, Item with) {
		for (final UnfinishedPotion potion : values()) {
			if (potion.ingredients[0].equals(use) && potion.ingredients[1].equals(with)
					|| potion.ingredients[1].equals(use) && potion.ingredients[0].equals(with)) {
				return potion;
			}
		}
		return null;
	}

	@Override
	public int getAnimation() {
		return -1;
	}

	@Override
	public Item[] getIngredients() {
		return Arrays.copyOf(ingredients, ingredients.length);
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public Item getProduct() {
		return new Item(product);
	}
	
	@Override
	public double getExperience() {
		return experience;
	}

	@Override
	public String toString() {
		return "UnfinishedPotion[product: " + getProduct() + ", level: " + level + ", ingredients: "
				+ Arrays.toString(ingredients) + "]";
	}
}