package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

@PacketListenerMeta({ 3, 35, 36, 58, 77, 78, 85, 86, 156, 200, 226, 238, 230 })
public class UnusedPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
//        if (player.debug)
//            player.send(new SendMessage("[UnusedPacketListener] Opcode: " + packet.getOpcode()));
	}
}
