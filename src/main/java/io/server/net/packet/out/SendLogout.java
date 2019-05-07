package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

public class SendLogout extends OutgoingPacket {

	public SendLogout() {
		super(109, 0);
	}

	@Override
	public boolean encode(Player player) {
		return true;
	}

}
