package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

/**
 * The {@code OutgoingPacket} that sends a string to a {@code Player}s
 * itemcontainer in the client.
 * 
 * @author Daniel | Obey
 */
public class SendString extends OutgoingPacket {

	private final Object string;
	private final int id;
	private final boolean override;

	public SendString(Object string, int id, boolean override) {
		super(126, PacketType.VAR_SHORT);
		this.string = string;
		this.id = id;
		this.override = override;
	}

	public SendString(Object string, int id) {
		this(string, id, false);
	}

	@Override
	public boolean encode(Player player) {
		if (!override) {
			if (id != 0 && !player.playerAssistant.checkSendString(String.valueOf(string), id)) {
				return false;
			}
		}
		builder.writeString(String.valueOf(string)).writeShort(id, ByteModification.ADD);
		return true;
	}

}
