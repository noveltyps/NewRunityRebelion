package io.server.content.preload;

import io.server.game.world.items.Item;

public interface PreLoadListener {

	/**
	 * Preset name
	 * @return
	 */
	String getPreset();
	
	/**
	 * Equipment
	 * @return All Equipments for the selected preset
	 */
	Item[] getEquipment();
	
	/**
	 * 
	 * @return All Inventory items for the selected preset
	 */
	Item[] getInventory();
	
}
