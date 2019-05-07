package io.server.net.packet.in;

import io.server.game.event.impl.log.ChatLogEvent;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.punishments.PunishmentExecuter;
import io.server.game.world.entity.mob.player.relations.ChatColor;
import io.server.game.world.entity.mob.player.relations.ChatEffect;
import io.server.game.world.entity.mob.player.relations.ChatMessage;
import io.server.net.codec.ByteModification;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendMessage;
import io.server.util.ChatCodec;

/**
 * The {@code GamePacket} responsible for chat messages.
 * 
 * @author Daniel
 */
@PacketListenerMeta(ClientPackets.CHAT)
public class ChatMessagePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		final int effect = packet.readByte(false, ByteModification.SUB);
		final int color = packet.readByte(false, ByteModification.SUB);
		final int size = packet.getSize() - 2;

		if(PunishmentExecuter.muted(player.getUsername()) || PunishmentExecuter.IPMuted(player.lastHost)) {
			player.send(new SendMessage("You are muted and cannot chat."));
			return;
		}
		
		if (effect < 0 || effect >= ChatEffect.values().length || color < 0 || color >= ChatColor.values().length || size <= 0)
			return;
		
		
		if (player.locking.locked()) 
			return;
		
		if (player.idle) 
			player.idle = false;

		final String decoded = ChatCodec.decode(packet.readBytesReverse(size, ByteModification.ADD));

		player.chat(ChatMessage.create(decoded, ChatColor.values()[color], ChatEffect.values()[effect]));
		World.getDataBus().publish(new ChatLogEvent(player, decoded));
	}
}