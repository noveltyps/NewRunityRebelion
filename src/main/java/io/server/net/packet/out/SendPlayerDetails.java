package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;

public class SendPlayerDetails extends OutgoingPacket {

	public SendPlayerDetails() {
		super(249, 3);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(1, ByteModification.ADD).writeShort(player.getIndex(), ByteModification.ADD, ByteOrder.LE);
		return true;
	}

}
