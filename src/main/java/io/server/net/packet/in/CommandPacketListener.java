package io.server.net.packet.in;

import io.server.content.clanchannel.channel.ClanChannel;
import io.server.content.command.Command;
import io.server.content.command.CommandManager;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendMessage;

/**
 * The {@code GamePacket} responsible for handling user commands send from the
 * client.
 *
 * @author Michael | Chex
 */
@PacketListenerMeta(ClientPackets.PLAYER_COMMAND)
public final class CommandPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		String input = packet.getRS2String();
		String[] parts = input.contains("-") ? input.split("-") : input.split(" ");
		parts[0] = parts[0].toLowerCase();
		
		if (player.isTeleporting() || player.isDead())
			return;

		if (input.startsWith("/")) {
			ClanChannel.handleMessage(player, input.replaceAll("/", ""));
			return;
		}
		Command plugin = CommandManager.PLUGIN_INPUT.get(parts[0]);
		if (plugin != null) {
			if (plugin.canUse(player)) {
				plugin.execute(player, input, parts);
			} else {
				player.send(new SendMessage("You are not allowed to use this command!"));
			}
		}
	}

}
