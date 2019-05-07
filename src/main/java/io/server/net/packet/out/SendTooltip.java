package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

public class SendTooltip extends OutgoingPacket {

	private final String string;
	private final int id;

	public SendTooltip(int id, String string) {
		super(203, PacketType.VAR_SHORT);
		this.string = string;
		this.id = id;
	}

	public SendTooltip(String string, int id) {
		this(id, string);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeString(string).writeShort(id, ByteModification.ADD);
		return true;
	}

}
