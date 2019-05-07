package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendMessage;

/**
 * The {@code GamePacket} responsible for chat messages.
 * 
 * @author Daniel
 */
@PacketListenerMeta(ClientPackets.INTERFACE_ON_GROUNDITEM)
public class InterfaceOnGroundItemListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		/*int yCoord = */packet.readShort(ByteOrder.LE);
		/*int itemId = */packet.readShort();
		/*int xCoord = */packet.readShort(ByteOrder.LE);
		/*int unknown = */packet.readShort(ByteModification.ADD);
		
		player.send(new SendMessage("Telegrab will be added soon!"));
		
		
	}
}