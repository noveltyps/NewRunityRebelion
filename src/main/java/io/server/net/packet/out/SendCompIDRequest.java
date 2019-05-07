package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

public class SendCompIDRequest extends OutgoingPacket {

	public SendCompIDRequest() {
		super(124, PacketType.EMPTY);
	}

	@Override
	public boolean encode(Player player) {
		return true;
	}
	
}
