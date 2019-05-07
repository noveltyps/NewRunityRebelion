package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

public final class SendRunEnergy extends OutgoingPacket {

	public SendRunEnergy() {
		super(110, 1);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(player.runEnergy);
		return true;
	}

}
