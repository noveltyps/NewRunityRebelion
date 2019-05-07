package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

/**
 * The {@code OutgoingPacket} resets the camera position for {@code Player}.
 * 
 * @author Daniel | Obey
 */
public class SendCameraReset extends OutgoingPacket {

	public SendCameraReset() {
		super(107, 0);
	}

	@Override
	public boolean encode(Player player) {
		return true;
	}

}
