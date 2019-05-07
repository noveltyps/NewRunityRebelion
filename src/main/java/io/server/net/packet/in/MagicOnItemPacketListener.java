package io.server.net.packet.in;

import io.server.content.skill.impl.magic.spell.impl.HighAlchemy;
import io.server.content.skill.impl.magic.spell.impl.LowAlchemy;
import io.server.content.skill.impl.magic.spell.impl.SuperHeat;
import io.server.game.world.entity.mob.data.PacketType;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.net.codec.ByteModification;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

/**
 * The {@link GamePacket} responsible for using magic on inventory items.
 *
 * @author Daniel
 */
@PacketListenerMeta(ClientPackets.MAGIC_ON_ITEMS)
public class MagicOnItemPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		if (player.locking.locked(PacketType.USE_MAGIC))
			return;
		final int slot = packet.readShort();
		final int itemId = packet.readShort(ByteModification.ADD);
		final int childId = packet.readShort();
		final int spell = packet.readShort(ByteModification.ADD);

		if (player.positionChange) {
			return;
		}

		final Item item = player.inventory.get(slot);

		if (item == null || item.getId() != itemId) {
			return;
		}

		if (PlayerRight.isDeveloper(player)) {
			player.message("[MagicOnItemPacket] spell=" + spell + " itemId=" + itemId + " slot=" + slot + " childId="
					+ childId);
		}

		switch (spell) {
		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			player.spellCasting.enchantItem(itemId, spell);
			break;

		case 1162:
			player.spellCasting.cast(new LowAlchemy(), item);
			break;

		case 1178:
			player.spellCasting.cast(new HighAlchemy(), item);
			break;

		case 1173:
			player.spellCasting.cast(new SuperHeat(), item);
			break;
		}
	}
}
