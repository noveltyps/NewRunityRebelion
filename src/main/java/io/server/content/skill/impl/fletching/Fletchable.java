package io.server.content.skill.impl.fletching;

import io.server.game.world.items.Item;

public interface Fletchable {

	int getAnimation();

	Item getUse();

	Item getWith();

	FletchableItem[] getFletchableItems();

	Item[] getIngediants();

	String getProductionMessage();
}