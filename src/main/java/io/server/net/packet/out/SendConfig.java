package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;

/**
 * The {@code OutgoingPacket} responsible for changing settings on a client.
 *
 * @author Daniel | Obey
 */
public class SendConfig extends OutgoingPacket {

	private final int id;
	private final int value;
	private final boolean intValue;

	public SendConfig(int id, int value, boolean intValue) {
		super(intValue ? 87 : 36, intValue ? 6 : 3);
		this.id = id;
		this.value = value;
		this.intValue = intValue;
	}

	public SendConfig(int id, int value) {
		this(id, value, value < Byte.MIN_VALUE || value > Byte.MAX_VALUE);
	}

	@Override
	public boolean encode(Player player) {
		if (value == -1) {
			return false;
		}
		if (intValue) {
			builder.writeShort(id, ByteOrder.LE).writeInt(value, ByteOrder.ME);
		} else {
			builder.writeShort(id, ByteOrder.LE).writeByte(value);
		}

		return true;
	}

}
