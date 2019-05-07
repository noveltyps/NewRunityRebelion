package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.packet.OutgoingPacket;

public class SendPoison extends OutgoingPacket {

	public enum PoisonType {
		NO_POISON, REGULAR, VENOM
	}

	private final PoisonType type;

	public SendPoison(PoisonType type) {
		super(182, 1);
		this.type = type;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(type.ordinal(), ByteModification.NEG);
		return true;
	}

}
