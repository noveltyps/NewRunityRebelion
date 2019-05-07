package io.server.net.packet.in;

import io.server.content.event.EventDispatcher;
import io.server.content.event.impl.ClickButtonInteractionEvent;
import io.server.game.event.impl.ButtonClickEvent;
import io.server.game.plugin.PluginManager;
import io.server.game.world.entity.mob.data.PacketType;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendMessage;

/**
 * The {@code GamePacket} responsible for clicking buttons on the client.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(ClientPackets.BUTTON_CLICK)
public class ButtonClickPacketListener implements PacketListener {

	@SuppressWarnings("static-access")
	@Override
	public void handlePacket(final Player player, GamePacket packet) {
		final int button = packet.readShort();

		if (player.right.isDeveloper(player)) {
			
		}
		if (player.isDead() || player.isTeleporting()) {
			return;
		}

		if (player.locking.locked(PacketType.CLICK_BUTTON, button)) {
			return;
		}

		if (PlayerRight.isDeveloper(player)) {
			player.send(new SendMessage(
					String.format("[%s]: button=%d", ButtonClickPacketListener.class.getSimpleName(), button)));
		}

		if (EventDispatcher.execute(player, new ClickButtonInteractionEvent(button))) {
			return;
		}

		PluginManager.getDataBus().publish(player, new ButtonClickEvent(button));
	}
}
