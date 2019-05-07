package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;

public class SendChatBoxInterface extends OutgoingPacket {

	private final int interfaceId;

	public SendChatBoxInterface(int interfaceId) {
		super(164, 2);
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean encode(Player player) {
		player.interfaceManager.setDialogue(1);
		builder.writeShort(interfaceId, ByteOrder.LE);
		return true;
	}
}
