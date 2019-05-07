package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

@PacketListenerMeta({ ClientPackets.PRIVACY_OPTIONS })
public final class PrivacyOptionPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		final int publicMode = packet.readByte();
		final int privateMode = packet.readByte();
		final int tradeMode = packet.readByte();
		final int clanMode = packet.readByte();
		player.relations.setPrivacyChatModes(publicMode, privateMode, clanMode, tradeMode);
	}

}
