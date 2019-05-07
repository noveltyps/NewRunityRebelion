package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

/**
 * The {@code OutgoingPacket} responsible for sending game messages.
 * 
 * @author Daniel
 */
public class SendGameMessage extends OutgoingPacket {

	private final int id;
	private final int time;
	private final Object context;

	public SendGameMessage(int id, int time, Object context) {
		super(114, PacketType.VAR_BYTE);
		this.id = id;
		this.time = time;
		this.context = context;
	}

	public SendGameMessage(int id, Object context) {
		super(114, PacketType.VAR_BYTE);
		this.id = id;
		this.time = -1;
		this.context = context;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(id).writeShort(time * 3).writeString(String.valueOf(context));
		return true;
	}

}
