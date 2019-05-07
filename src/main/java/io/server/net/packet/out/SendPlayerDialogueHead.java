package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;

public class SendPlayerDialogueHead extends OutgoingPacket {

	private final int interfaceId;

	public SendPlayerDialogueHead(int interfaceId) {
		super(185, 2);
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(interfaceId, ByteModification.ADD, ByteOrder.LE);
		return true;
	}

}
