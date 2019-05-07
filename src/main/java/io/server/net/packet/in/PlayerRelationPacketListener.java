package io.server.net.packet.in;

import java.util.Optional;

import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.relations.PrivateChatMessage;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.util.ChatCodec;
import io.server.util.Utility;

/**
 * The {@link GamePacket}'s responsible for player communication.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta({ ClientPackets.ADD_FRIEND, ClientPackets.PRIVATE_MESSAGE, ClientPackets.REMOVE_FRIEND,
		ClientPackets.REMOVE_IGNORE, ClientPackets.ADD_IGNORE })
public final class PlayerRelationPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		final long username = packet.readLong();
		switch (packet.getOpcode()) {

		case ClientPackets.ADD_FRIEND:
			player.relations.addFriend(username);
			break;

		case ClientPackets.REMOVE_FRIEND:
			player.relations.deleteFriend(username);
			break;

		case ClientPackets.ADD_IGNORE:
			player.relations.addIgnore(username);
			break;

		case ClientPackets.REMOVE_IGNORE:
			player.relations.deleteIgnore(username);
			break;

		case ClientPackets.PRIVATE_MESSAGE:
			final Optional<Player> result = World
					.search(Utility.formatText(Utility.longToString(username)).replace("_", " "));

			if (!result.isPresent()) {
				break;
			}

			final Player other = result.get();

			final byte[] input = packet.readBytes(packet.getSize() - Long.BYTES);
			final String decoded = ChatCodec.decode(input);
			final byte[] compressed = ChatCodec.encode(decoded);
			player.relations.message(other, new PrivateChatMessage(decoded, compressed));
			break;
		}

	}

}
