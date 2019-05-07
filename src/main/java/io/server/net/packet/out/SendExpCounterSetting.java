package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

public class SendExpCounterSetting extends OutgoingPacket {

	private final int type;
	private final int modification;

	public SendExpCounterSetting(int type, int modification) {
		super(103, 8);
		this.type = type;
		this.modification = modification;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeInt(type).writeInt(modification);
		return true;
	}

}
