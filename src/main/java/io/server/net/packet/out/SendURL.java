package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

/**
 * The {@code OutgoingPacket} that opens a URL from client.
 * 
 * @author Daniel | Obey
 */
public class SendURL extends OutgoingPacket {

	private final String link;

	public SendURL(String link) {
		super(138, PacketType.VAR_BYTE);
		this.link = link;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeString(link);
		return true;
	}

}
