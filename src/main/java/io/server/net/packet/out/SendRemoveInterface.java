package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

public class SendRemoveInterface extends OutgoingPacket {

	private final boolean close;

	public SendRemoveInterface(boolean close) {
		super(219, 1);
		this.close = close;
	}

	public SendRemoveInterface() {
		this(true);
	}

	@Override
	public boolean encode(Player player) {
		byte val = (byte) (close ? 1 : 0);
		builder.writeByte(val);
		return true;
	}

}
