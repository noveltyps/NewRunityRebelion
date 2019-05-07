package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

/**
 * Makes a tab blink/flash.
 *
 * @author Daniel
 */
public class SendTabBlink extends OutgoingPacket {

	private int tab;

	public SendTabBlink(int tab) {
		super(24, 1);
		this.tab = tab;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(tab);
		return true;
	}

}
