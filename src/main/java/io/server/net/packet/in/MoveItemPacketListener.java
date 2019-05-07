package io.server.net.packet.in;

import io.server.game.world.InterfaceConstants;
import io.server.game.world.entity.mob.data.PacketType;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.containers.inventory.Inventory;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

@PacketListenerMeta(ClientPackets.MOVE_ITEM)
public class MoveItemPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		if (player.locking.locked(PacketType.MOVE_ITEMS))
			return;

		final int interfaceId = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		final int inserting = packet.readByte(ByteModification.NEG);
		final int fromSlot = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		final int toSlot = packet.readShort(ByteOrder.LE);
		
		if (player.idle) {
			player.idle = false;
		}

		switch (interfaceId) {
		case InterfaceConstants.INVENTORY_INTERFACE:
		case InterfaceConstants.INVENTORY_STORE:
			player.inventory.swap(fromSlot, toSlot);
			break;

		case InterfaceConstants.WITHDRAW_BANK:
			player.bank.moveItem(inserting, fromSlot, toSlot);
			break;

		default:
			System.out.println("Unkown Item movement itemcontainer id: " + interfaceId);
			break;
		}
	}
}
