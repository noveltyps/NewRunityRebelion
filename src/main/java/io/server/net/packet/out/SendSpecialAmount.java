package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

/**
 * Handles sending the special attack amount (used for the orb).
 *
 * @author Daniel
 */
public final class SendSpecialAmount extends OutgoingPacket {

	public SendSpecialAmount() {
		super(137, 1);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(player.getSpecialPercentage().get());
		return true;
	}

}
