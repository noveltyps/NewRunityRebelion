package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

/**
 * The {@link GamePacket} responsible logging out a player after a certain
 * amount of time.
 * 
 * @author Daniel
 */
@PacketListenerMeta(202)
public class IdleLogoutPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		if (player.idle)
			return;

		player.idle = true;
		player.send(new SendMessage("There has not been any detection from your account. You have now been listed as idle.",
						MessageColor.DEVELOPER));
		player.send(
				new SendMessage("You will not receive royalty points until you are active.", MessageColor.DEVELOPER));
	}
}