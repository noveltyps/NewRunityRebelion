package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

public class SendMiniMeData extends OutgoingPacket {

	private final int model;

	public SendMiniMeData(int model) {
		super(157, 2);
		this.model = model;
	}

	@Override
	protected boolean encode(Player player) {
		builder.writeShort(model);
		return true;
	}

}
