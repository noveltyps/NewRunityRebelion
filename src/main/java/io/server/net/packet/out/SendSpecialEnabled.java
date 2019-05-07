package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.packet.OutgoingPacket;

public class SendSpecialEnabled extends OutgoingPacket {

	private final int id;

	public SendSpecialEnabled(int id) {
		super(183, 1);
		this.id = id;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(id, ByteModification.NEG);
		return true;
	}
}
