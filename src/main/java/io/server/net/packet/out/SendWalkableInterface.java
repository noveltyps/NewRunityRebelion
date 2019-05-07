package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;

public class SendWalkableInterface extends OutgoingPacket {

	private final int id;

	public SendWalkableInterface(int id) {
		super(208, 2);
		this.id = id;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(id, ByteOrder.LE);
		return true;
	}

}
