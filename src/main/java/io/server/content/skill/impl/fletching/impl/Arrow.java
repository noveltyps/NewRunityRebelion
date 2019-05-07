package io.server.content.skill.impl.fletching.impl;

import io.server.content.skill.impl.fletching.Fletchable;
import io.server.content.skill.impl.fletching.FletchableItem;
import io.server.content.skill.impl.fletching.Fletching;
import io.server.game.world.items.Item;

public enum Arrow implements Fletchable {
	
	BRONZE_ARROWS(new Item(53, 15), new Item(39, 15), new FletchableItem(new Item(882, 15), 1, 20)),
	IRON_ARROWS(new Item(53, 15), new Item(40, 15), new FletchableItem(new Item(884, 15), 15, 37.5)),
	STEEL_ARROWS(new Item(53, 15), new Item(41, 15), new FletchableItem(new Item(886, 15), 30, 7.0)),
	MITHRIL_ARROWS(new Item(53, 15), new Item(42, 15), new FletchableItem(new Item(888, 15), 45, 112.5)),
	ADAMANT_ARROWS(new Item(53, 15), new Item(43, 15), new FletchableItem(new Item(890, 15), 60, 150.0)),
	RUNE_ARROWS(new Item(53, 15), new Item(44, 15), new FletchableItem(new Item(892, 15), 75, 187.5)),
	DRAGON_ARROWS(new Item(53, 15), new Item(11237, 15), new FletchableItem(new Item(11212, 15), 90, 225.0)),
	DRAGON_JAVELIN(new Item(19582, 15), new Item(19584, 15), new FletchableItem(new Item(19484, 15), 92, 225.0));

	private final Item use;
	private final Item with;
	private final FletchableItem[] items;

	Arrow(Item use, Item with, FletchableItem... items) {
		this.use = use;
		this.with = with;
		this.items = items;
	}

	public static void load() {
		for (Arrow cuttable : values()) {
			Fletching.addFletchable(cuttable);
		}
	}

	@Override
	public int getAnimation() {
		return 65535;
	}

	@Override
	public Item getUse() {
		return use;
	}

	@Override
	public Item getWith() {
		return with;
	}

	@Override
	public FletchableItem[] getFletchableItems() {
		return items;
	}

	@Override
	public String getProductionMessage() {
		return null;
	}

	@Override
	public Item[] getIngediants() {
		return new Item[] { use, with };
	}
}