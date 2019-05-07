package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

public class SendPrivateMessage extends OutgoingPacket {

	private final long name;
	private final PlayerRight rights;
	private final byte[] compressed;

	public SendPrivateMessage(long name, PlayerRight rights, byte[] compressed) {
		super(196, PacketType.VAR_BYTE);
		this.name = name;
		this.rights = rights;
		this.compressed = compressed;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeLong(name).writeInt(player.relations.getPrivateMessageId()).writeByte(rights.getCrown())
				.writeBytes(compressed, compressed.length);
		return true;
	}

}
