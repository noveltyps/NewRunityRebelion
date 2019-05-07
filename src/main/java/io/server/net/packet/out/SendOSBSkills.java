package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

/**
 * @author Ethan Kyle Millard <skype:pumpklins>
 * @since Mon, May 07, 2018 @ 4:14 PM
 */
public class SendOSBSkills extends OutgoingPacket {

	public SendOSBSkills(int opcode, int capacity) {
		super(100, capacity);
	}

	@Override
	protected boolean encode(Player player) {
		return false;
	}
}
