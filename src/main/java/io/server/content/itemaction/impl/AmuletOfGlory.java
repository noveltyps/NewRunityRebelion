package io.server.content.itemaction.impl;

import io.server.content.itemaction.ItemAction;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.game.world.position.Position;

/**
 * @author Austin#8138
 */
public class AmuletOfGlory extends ItemAction {

	public static final int[] AMULETS = {1704, 1706, 1708, 1710, 1712, 11976, 11978, };
	public static final int ETERNAL = 19707;

	public static int getCurrentIdx(int id) {
		int idx = 0;
		for (int i = 0; i < AMULETS.length; i++)
			if (AMULETS[i] == id)
				idx = i;
		return idx;
	}
	
	public static int getNextIdx(int id) {
		int idx = 0;
		for (int i = 0; i < AMULETS.length; i++)
			if (AMULETS[i] == id)
				idx = i - 1;
		if (idx < 0)
			idx = 0;
		return idx;
	}
	
	public enum AmuletRubType {
		INVENTORY, EQUIPMENT;
	}
	
	public static void teleport(Player player, Position position, AmuletRubType type, Item item, Item newItem) {
		if (type.equals(AmuletRubType.EQUIPMENT) && !player.equipment.getAmuletSlot().equals(item))
			return;
		if (type.equals(AmuletRubType.INVENTORY) && !player.inventory.contains(item.getId()))
			return;
		if (Teleportation.teleport(player, position)) {
			if (type.equals(AmuletRubType.INVENTORY) && ETERNAL != item.getId()) {
				player.inventory.replace(item, newItem, true);
				player.sendMessage("Your amulet has " + getCurrentIdx(newItem.getId()) + " charges left.");
			} else if (type.equals(AmuletRubType.EQUIPMENT) && ETERNAL != item.getId()) {
				player.equipment.set(Equipment.AMULET_SLOT, newItem, true);
				player.sendMessage("Your amulet has " + getCurrentIdx(newItem.getId()) + " charges left.");
			}
		}
	}
	
	public static boolean rubAmulet(Player player, AmuletRubType type, Item item) {
		if (getCurrentIdx(item.getId()) == 0 && item.getId() != ETERNAL)
			return false;
		Item newItem = new Item(AMULETS[getNextIdx(item.getId())]);
		player.dialogueFactory.sendOption(
			"Edgeville", () -> {
				teleport(player, new Position(3087, 3496), type, item, newItem); }, 
			"Karamja", () -> {
				teleport(player, new Position(2918, 3176), type, item, newItem); }, 
			"Dranyor Village", () -> {
				teleport(player, new Position(3105, 3251), type, item, newItem); }, 
			"Al-Kahrid", () -> {
				teleport(player, new Position(3293, 3163), type, item, newItem); }, 
			"Nowhere", player.interfaceManager::close).execute();
		return true;
	}
	@Override
	public String name() {
		return "Amulet of glory";
	}

	@Override
	public boolean inventory(Player player, Item item, int opcode) {
		if (opcode == 3) {
			rubAmulet(player, AmuletRubType.INVENTORY, item);
		}
		return true;
	}
	
	@Override
	public boolean equipment(Player player, Item item, int opcode) {
		if (opcode == 1) {
			rubAmulet(player, AmuletRubType.EQUIPMENT, item);
		}
		return true;
	}

}
