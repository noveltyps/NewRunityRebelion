package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

/**
 * Handles sending the fade screen packet.
 *
 * @author Daniel
 */
public class SendFadeScreen extends OutgoingPacket {

	private final String message;
	private final int state;
	private final int seconds;

	public SendFadeScreen(String message, int state, int seconds) {
		super(189, PacketType.VAR_SHORT);
		this.message = message;
		this.state = state;
		this.seconds = seconds;
	}

	@Override
	public boolean encode(Player player) {
		player.interfaceManager.close();
		builder.writeString(message).writeByte(state).writeByte(seconds);
		return true;
	}

}
