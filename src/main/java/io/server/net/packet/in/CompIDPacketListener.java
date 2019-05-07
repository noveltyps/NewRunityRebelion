package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

/**
 * The {@code GamePacket} responsible for storing the clients unique ID.
 * 
 * @author Daniel
 */
@PacketListenerMeta(ClientPackets.COMP_ID_REQUEST)
public class CompIDPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) { }

}
