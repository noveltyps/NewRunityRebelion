package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

@PacketListenerMeta({ 0 })
public class IdlePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {

	}

}
