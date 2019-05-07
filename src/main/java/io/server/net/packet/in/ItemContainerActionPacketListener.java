package io.server.net.packet.in;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import io.server.content.event.EventDispatcher;
import io.server.content.event.impl.ItemContainerInteractionEvent;
import io.server.game.event.impl.ItemContainerContextMenuEvent;
import io.server.game.plugin.PluginManager;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

/**
 * The {@link GamePacket}'s responsible for the options for Items inside a
 * container itemcontainer.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta({ 140, 141, 145, 117, 43, 129, 135, 208 })
public class ItemContainerActionPacketListener implements PacketListener {

	private static final int FIRST_ITEM_ACTION_OPCODE = 145;
	private static final int SECOND_ITEM_ACTION_OPCODE = 117;
	private static final int THIRD_ITEM_ACTION_OPCODE = 43;
	private static final int FOURTH_ITEM_ACTION_OPCODE = 129;
	private static final int FIFTH_ITEM_ACTION_OPCODE = 135;
	private static final int SIXTH_ITEM_ACTION_OPCODE = 208;
	private static final int ALL_BUT_ONE_ACTION_OPCODE = 140;
	private static final int MODIFIABLE_X_ACTION_OPCODE = 141;

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		switch (packet.getOpcode()) {

		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;

		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;

		case THIRD_ITEM_ACTION_OPCODE:
			thirdAction(player, packet);
			break;

		case FOURTH_ITEM_ACTION_OPCODE:
			fourthAction(player, packet);
			break;

		case FIFTH_ITEM_ACTION_OPCODE:
			fifthAction(player, packet);
			break;

		case SIXTH_ITEM_ACTION_OPCODE:
			sixthAction(player, packet);
			break;

		case ALL_BUT_ONE_ACTION_OPCODE:
			allButOne(player, packet);
			break;

		case MODIFIABLE_X_ACTION_OPCODE:
			modifiableXAction(player, packet);
			break;
		}
	}

	/**
	 * Handles the randomevent when a player clicks on the first option of an item
	 * container itemcontainer.
	 *
	 * @param player The player clicking the option.
	 * @param packet The packet for this action.
	 */
	private void firstAction(Player player, GamePacket packet) {
		final int interfaceId = packet.readShort(ByteModification.ADD);
		final int removeSlot = packet.readShort(ByteModification.ADD);
		final int removeId = packet.readShort(ByteModification.ADD);
		
		logAction(player, "firstAction - InterfaceId="+interfaceId+" SlotId="+removeSlot+" ItemId="+removeId);
		
		if (EventDispatcher.execute(player, new ItemContainerInteractionEvent(1, interfaceId, removeSlot, removeId))) {
			return;
		}

		PluginManager.getDataBus().publish(player,
				new ItemContainerContextMenuEvent(1, interfaceId, removeSlot, removeId));
	}

	/**
	 * Handles the randomevent when a player clicks on the second option of an item
	 * container itemcontainer.
	 *
	 * @param player The player clicking the option.
	 * @param packet The packet for this option.
	 */
	private void secondAction(Player player, GamePacket packet) {
		final int interfaceId = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		final int removeId = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		final int removeSlot = packet.readShort(ByteOrder.LE);
		
		
		logAction(player, "secondAction - InterfaceId="+interfaceId+" SlotId="+removeSlot+" ItemId="+removeId);
		
		if (EventDispatcher.execute(player, new ItemContainerInteractionEvent(2, interfaceId, removeSlot, removeId))) {
			return;
		}

		PluginManager.getDataBus().publish(player,
				new ItemContainerContextMenuEvent(2, interfaceId, removeSlot, removeId));
	}

	/**
	 * Handles the randomevent when a player clicks on the third option of an item
	 * container itemcontainer.
	 *
	 * @param player The player clicking the option.
	 * @param packet The packet for this option.
	 */
	private void thirdAction(Player player, GamePacket packet) {
		final int interfaceId = packet.readShort(ByteOrder.LE);
		final int removeId = packet.readShort(ByteModification.ADD);
		final int removeSlot = packet.readShort(ByteModification.ADD);
		
		
		logAction(player, "thirdAction - InterfaceId="+interfaceId+" SlotId="+removeSlot+" ItemId="+removeId);
		if (EventDispatcher.execute(player, new ItemContainerInteractionEvent(3, interfaceId, removeSlot, removeId))) {
			return;
		}

		PluginManager.getDataBus().publish(player,
				new ItemContainerContextMenuEvent(3, interfaceId, removeSlot, removeId));
	}

	/**
	 * Handles the randomevent when a player clicks on the fourth option of an item
	 * container itemcontainer.
	 *
	 * @param player The player clicking the option.
	 */
	private void fourthAction(Player player, GamePacket packet) {
		final int interfaceId = packet.readShort(ByteOrder.LE);
		final int removeId = packet.readShort(ByteModification.ADD);
		final int removeSlot = packet.readShort(ByteModification.ADD);
		
		logAction(player, "fourthAction - InterfaceId="+interfaceId+" SlotId="+removeSlot+" ItemId="+removeId);
		
		if (EventDispatcher.execute(player, new ItemContainerInteractionEvent(4, interfaceId, removeSlot, removeId))) {
			return;
		}

		PluginManager.getDataBus().publish(player,
				new ItemContainerContextMenuEvent(4, interfaceId, removeSlot, removeId));
	}

	/**
	 * Handles the action when a player clicks on the fifth option of an item
	 * container itemcontainer.
	 *
	 * @param player The player clicking the option.
	 * @param packet The packet for this option.
	 */
	private void fifthAction(Player player, GamePacket packet) {
		final int removeSlot = packet.readShort(ByteOrder.LE);
		final int interfaceId = packet.readShort(ByteModification.ADD);
		final int removeId = packet.readShort(ByteOrder.LE);

		player.attributes.set("XREMOVE_SLOT", removeSlot);
		player.attributes.set("XREMOVE_INTERFACE", interfaceId);
		player.attributes.set("XREMOVE_REMOVE", removeId);
		
		logAction(player, "fifthAction - InterfaceId="+interfaceId+" SlotId="+removeSlot+" ItemId="+removeId);
		
		if (EventDispatcher.execute(player, new ItemContainerInteractionEvent(5, interfaceId, removeSlot, removeId))) {
			return;
		}

		PluginManager.getDataBus().publish(player,
				new ItemContainerContextMenuEvent(5, interfaceId, removeSlot, removeId));
	}

	/**
	 * Handles the randomevent when a player clicks on the sixth option of an item
	 * container itemcontainer.
	 *
	 * @param player The player clicking the option.
	 * @param packet The packet for this option.
	 */

	private void sixthAction(Player player, GamePacket packet) {
		final int amount = packet.readInt();
		if (player.enterInputListener.isPresent()) {
			player.enterInputListener.get().accept(Integer.toString(amount));
			return;
		}		
	if (player.attributes.get("XREMOVE_INTERFACE", Integer.class) == null
				|| player.attributes.get("XREMOVE_SLOT", Integer.class) == null
				|| player.attributes.get("XREMOVE_REMOVE", Integer.class) == null) {
            return;
        } 
		final int interfaceId = player.attributes.get("XREMOVE_INTERFACE", Integer.class);
		final int removeSlot = player.attributes.get("XREMOVE_SLOT", Integer.class);
		final int removeId = player.attributes.get("XREMOVE_REMOVE", Integer.class);
		
logAction(player, "sixthAction - InterfaceId="+interfaceId+" SlotId="+removeSlot+" ItemId="+removeId);
		
		/**
		 * I just added null checks
		 * 
		 */

		/*Inventory inv = player.inventory;
		
		if (inv.get(removeSlot) == null)
			return;
		*/
		/*
		if (inv.get(removeSlot).getId() != removeId || inv.get(removeId) == null || removeId == -1) {
			World.sendStaffMessage("Staff Notice: "+player.getName() + " is exploiting opcode 208");
			World.sendStaffMessage(player.getName() + " [ABUSE] Possibly duping, check him out." );
			return;
		}*/
		
		//if (player.getPlayer().inventory.get(removeSlot) != null &&
		if (EventDispatcher.execute(player, new ItemContainerInteractionEvent(6, interfaceId, removeSlot, removeId))) {
			return;
		}

		PluginManager.getDataBus().publish(player, new ItemContainerContextMenuEvent(6, interfaceId, removeSlot, removeId));
	}

	/**
	 * Handles the randomevent when a player clicks on the sixth option of an item
	 * container itemcontainer.
	 *
	 * @param player The player clicking the option.
	 * @param packet The packet for this option.
	 */
	private void allButOne(Player player, GamePacket packet) {
		final int removeSlot = packet.readShort(ByteModification.ADD);
		final int interfaceId = packet.readShort();
		final int removeId = packet.readShort(ByteModification.ADD);

		PluginManager.getDataBus().publish(player,
				new ItemContainerContextMenuEvent(7, interfaceId, removeSlot, removeId));
	}

	/**
	 * Handles the randomevent when a player clicks on the sixth option of an item
	 * container itemcontainer.
	 *
	 * @param player The player clicking the option.
	 * @param packet The packet for this option.
	 */
	private void modifiableXAction(Player player, GamePacket packet) {
		final int removeSlot = packet.readShort(ByteModification.ADD);
		final int interfaceId = packet.readShort();
		final int removeId = packet.readShort(ByteModification.ADD);
		final int amount = packet.readInt();

        logAction(player, "modifiableXAction - InterfaceId="+interfaceId+" SlotId="+removeSlot+" ItemId="+removeId+" amount="+amount);
		
		PluginManager.getDataBus().publish(player,
				new ItemContainerContextMenuEvent(8, interfaceId, removeSlot, removeId, amount));
	}
	
	
	public static void logAction(Player player, String message) {
		
		CompletableFuture.runAsync(() -> {
			
			String fileName = "./data/ItemContainerLog.txt";
			
			try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true))) {
				out.write(""+new SimpleDateFormat().format(new Date())+ "NAME[" + player.getName() + "]" + " IP="+player.lastHost+" "+message);
				out.newLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
