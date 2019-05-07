package io.server.net.packet.in;

import io.server.content.itemaction.ItemActionRepository;
import io.server.game.event.impl.DropItemEvent;
import io.server.game.plugin.PluginManager;
import io.server.game.world.entity.mob.data.PacketType;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.items.containers.pricechecker.PriceType;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.position.Area;
import io.server.net.codec.ByteModification;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * The {@code GamePacket} responsible for dropping items.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(ClientPackets.DROP_ITEM)
public class DropItemPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		if (player.isTeleporting() || player.isDead() || player.locking.locked(PacketType.DROP_ITEM)) {
			return;
		}

		final int itemId = packet.readShort(false, ByteModification.ADD);
		packet.readByte(false);
		packet.readByte(false);
		final int slot = packet.readShort(false, ByteModification.ADD);
		final Item item = player.inventory.get(slot);

		if (ItemDefinition.get(itemId) == null)
			return;

		player.getCombat().reset();

		if (!player.interfaceManager.isClear())
			player.interfaceManager.close(false);

		if (player.idle)
			player.idle = false;

		if (item == null)
			return;

		if (item.getId() != itemId)
			return;

		if (PluginManager.getDataBus().publish(player, new DropItemEvent(item, slot, player.getPosition().copy())))
			return;

		if (ItemActionRepository.drop(player, item)) {
			if (PlayerRight.isDeveloper(player) && player.debug) {
				player.send(new SendMessage(String.format("[%s]: item=%d amount=%d slot=%d",
						ItemActionRepository.class.getSimpleName(), item.getId(), item.getAmount(), slot)));
			}
			return;
		}

        if (player.playTime < 3000) {
            player.message("You can't drop items until you have 30 mins of playtime. " + Utility.getTime((3000 - player.playTime) * 3 / 5) + " mins left.");
            return;
        }

		boolean inWilderness = Area.inWilderness(player);
		if (inWilderness && item.getValue(PriceType.VALUE) >= 500_000) {
			player.dialogueFactory.sendStatement("This is a valuable item, are you sure you want to",
					"drop it? In a PvP area, this item will be seen", "by everyone when dropped.");
			player.dialogueFactory.sendOption("Yes, drop it.", () -> {
				player.inventory.remove(item, slot, true);
				GroundItem.createGlobal(player, item);
			}, "Nevermind", () -> player.dialogueFactory.clear());
			player.dialogueFactory.execute();
			return;
		} else if (inWilderness) {
			player.inventory.remove(item, slot, true);
			GroundItem.createGlobal(player, item);
			return;
		}

		player.inventory.remove(item, slot, true);
		GroundItem.create(player, item);
	}
}
