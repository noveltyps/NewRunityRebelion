package io.server.content.itemaction.impl;

//package io.battlerune.content.itemaction.impl;
//
//import io.battlerune.net.packet.out.SendMessage;
//import io.battlerune.net.packet.out.SendMessage.MessageColor;
//import io.battlerune.game.world.entity.mob.player.Player;
//import io.battlerune.content.itemaction.ItemAction;
//import io.battlerune.content.itemaction.ItemActionType;
//import io.battlerune.content.skill.impl.magic.teleport.Teleportation;
//import io.battlerune.game.world.items.Item;
//import io.battlerune.game.world.location.Position;
//import io.battlerune.util.Utility;
//
///**
// * Handles the ring of dueling action.
// *
// * @author Daniel
// */
//public class RingOfDueling extends ItemAction {
//
//	@Override
//	public String name() {
//		return "ring of dueling";
//	}
//
//	@Override
//	public String message(Item item) {
//		return "You rub the ring...";
//	}
//
//	@Override
//	public int delay() {
//		return 1;
//	}
//
//	@Override
//	public void execute(Player player, Item item, ItemActionType type, int opcode) {
//		player.attributes.set("ITEM_ACTION_KEY", charges(item));
//		player.dialogueFactory.sendOption("Al Kahrid Duel Arena.", () -> {
//			teleport(player, item, new Position(3314, 3235), type);
//		}, "Castle Wars Arena.", () -> {
//			teleport(player, item, new Position(2441, 3088), type);
//		}, "Clan Wars Arena", () -> {
//			teleport(player, item, new Position(3390, 3162), type);
//		}, "Nowhere.", () -> {
//			player.interfaceManager.close();
//		}).execute();
//	}
//
//	public Item[] items() {
//		return new Item[] { new Item(2566), new Item(2564), new Item(2562), new Item(2560), new Item(2558), new Item(2556), new Item(2554), new Item(2552) };
//	};
//
//	public int charges(Item item) {
//		int charge = 0;
//		for (int index = 0; index < items().length; index++) {
//			if (item.getId() == items()[index].getId()) {
//				charge = index;
//				break;
//			}
//		}
//		return charge;
//	}
//
//	public void teleport(Player player, Item item, Position position, ItemActionType type) {
//		int charge = (player.attributes.get("ITEM_ACTION_KEY", Integer.class)) - 1;
//
//		if (type == ItemActionType.EQUIPMENT) {
//			if (charges(item) == 0) {
//				player.equipment.remove(item);
//			} else {
//				player.equipment.replace(item.getId(), items()[charge].getId(), true);
//			}
//		} else if (type == ItemActionType.INVENTORY) {
//			if (charges(item) == 0) {
//				player.inventory.remove(item);
//			} else {
//				player.inventory.replace(item.getId(), items()[charge].getId(), true);
//			}
//		}
//
//		Teleportation.teleport(player, position);
//		player.send(new SendMessage(charge == 0 || charge == -1 ? "Your " + name() + " crumbles to dust." : "Your " + name() + " has " + Utility.convertWord(charge).toLowerCase() + "charge" + (charge == 1 ? "" : "s") + " remaining.", MessageColor.DARK_PURPLE));
//	}
//}
