package io.server.net.packet;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.session.GameSession;

public abstract class OutgoingPacket {

	private final int opcode;
	private final PacketType type;
	protected final PacketBuilder builder;

	public OutgoingPacket(int opcode, int capacity) {
		this(opcode, PacketType.FIXED, capacity);
	}

	public OutgoingPacket(int opcode, PacketType type) {
		this.opcode = opcode;
		this.type = type;
		this.builder = PacketBuilder.alloc();
	}

	public OutgoingPacket(int opcode, PacketType type, int size) {
		this.opcode = opcode;
		this.type = type;
		this.builder = PacketBuilder.alloc(size, size);
	}

	protected abstract boolean encode(Player player);

	public void execute(Player player, boolean queue) {
		if (!encode(player)) {
			return;
		}

		if (!player.getSession().isPresent()) {
			return;
		}

		GameSession session = player.getSession().get();

		if (queue) {
			session.queueServerPacket(builder.toPacket(opcode, type));
		} else {
			session.flushPacket(builder.toPacket(opcode, type));
		}

	}

}
