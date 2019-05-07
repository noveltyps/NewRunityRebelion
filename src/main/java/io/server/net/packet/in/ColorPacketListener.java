package io.server.net.packet.in;

import io.server.content.tittle.PlayerTitle;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

@PacketListenerMeta(187)
public class ColorPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		int identification = packet.readShort(ByteOrder.LE);
		int value = packet.readInt();

		if (player.right.equals(PlayerRight.OWNER) && player.debug) {
			player.send(new SendMessage("[ColorPacket] - Identification: " + identification + " Value: " + value,
					MessageColor.DEVELOPER));
		}

		switch (identification) {

		case 0:
			player.playerTitle = PlayerTitle.create(player.playerTitle.getTitle(), value);
			player.updateFlags.add(UpdateFlag.APPEARANCE);
			break;

		case 1:
			// yell
			break;
		}
	}
}
