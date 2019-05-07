package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

/**
 * The {@link GamePacket} responsible for closing interfaces.
 * 
 * @author Daniel
 */
@PacketListenerMeta(130)
public class CloseInterfacePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {

		switch (packet.getOpcode()) {

		case ClientPackets.CLOSE_WINDOW:
			player.interfaceManager.close(false);
			break;
		}
	}
}