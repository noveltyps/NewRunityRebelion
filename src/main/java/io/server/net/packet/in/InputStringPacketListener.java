package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.util.Utility;

/**
 * The {@link GamePacket} responsible for reciving a string sent by the client.
 * 
 * @author Michael | Chex
 */
@PacketListenerMeta(60)
public class InputStringPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		String input = Utility.longToString(packet.readLong()).replace("_", " ");

		if (player.enterInputListener.isPresent()) {
			player.enterInputListener.get().accept(input);
//			player.enterInputListener = Optional.empty();
		}
	}
}