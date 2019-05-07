package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;

public final class SendToggle extends OutgoingPacket {

	private final int id;
	private final int value;

	public SendToggle(int id, int value) {
		super(87, 6);
		this.id = id;
		this.value = value;
	}

	@Override
	protected boolean encode(Player player) {
		builder.writeShort(id, ByteOrder.LE).writeInt(value, ByteOrder.ME);
		return true;
	}

}
