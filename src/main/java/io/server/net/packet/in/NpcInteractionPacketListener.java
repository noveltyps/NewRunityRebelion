package io.server.net.packet.in;

import io.server.content.event.EventDispatcher;
import io.server.content.event.impl.FirstNpcClick;
import io.server.content.event.impl.SecondNpcClick;
import io.server.game.action.impl.NpcFaceAction;
import io.server.game.event.impl.NpcClickEvent;
import io.server.game.plugin.PluginManager;
import io.server.game.world.World;
import io.server.game.world.entity.combat.magic.CombatSpell;
import io.server.game.world.entity.mob.data.PacketType;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendMessage;

/**
 * The {@link GamePacket} responsible for the different options while clicking
 * an npc.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta({ ClientPackets.ATTACK_NPC, ClientPackets.MAGIC_ON_NPC, ClientPackets.NPC_ACTION_1,
		ClientPackets.NPC_ACTION_2, ClientPackets.NPC_ACTION_3, ClientPackets.NPC_ACTION_4 })
public class NpcInteractionPacketListener implements PacketListener {

	@Override
	public void handlePacket(final Player player, GamePacket packet) {
		if (player.isTeleporting() || player.isDead() ||  player.locking.locked(PacketType.CLICK_NPC))
			return;

		switch (packet.getOpcode()) {
		case ClientPackets.ATTACK_NPC:
			handleAttackNpc(player, packet);
			break;
		case ClientPackets.MAGIC_ON_NPC:
			handleMagicOnNpc(player, packet);
			break;
		case ClientPackets.NPC_ACTION_1:
			handleFirstClickNpc(player, packet);
			break;
		case ClientPackets.NPC_ACTION_2:
			handleSecondClickNpc(player, packet);
			break;
		case ClientPackets.NPC_ACTION_3:
			handleThirdClickNpc(player, packet);
			break;
		case ClientPackets.NPC_ACTION_4:
			handleFourthClickNpc(player, packet);
			break;
		}
	}

	private static void handleAttackNpc(Player player, GamePacket packet) {
		final int slot = packet.readShort(false, ByteModification.ADD);

		World.getNpcBySlot(slot).ifPresent(npc -> {
			Position position = npc.getPosition();
			Region region = World.getRegions().getRegion(position);
			if (!region.containsNpc(position.getHeight(), npc))
				return;
			if (!npc.isValid())
				return;
			player.getCombat().attack(npc);
		});
	}

	private static void handleMagicOnNpc(Player player, GamePacket packet) {
		final int slot = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		final int spell = packet.readShort(ByteModification.ADD);
		final CombatSpell definition = CombatSpell.get(spell);

		World.getNpcBySlot(slot).ifPresent(npc -> {
			if (!npc.isValid())
				return;
			if (definition == null)
				return;
			if (player.spellbook != definition.getSpellbook())
				return;

			if (!npc.definition.isAttackable()) {
				player.send(new SendMessage("This npc can not be attacked!"));
				return;
			}
			player.setSingleCast(definition);
			if (!player.getCombat().attack(npc)) {
				player.setSingleCast(null);
				player.resetFace();
			}
		});
	}

	private static void handleFirstClickNpc(Player player, GamePacket packet) {
		final int slot = packet.readShort(ByteOrder.LE);

		World.getNpcBySlot(slot).ifPresent(npc -> {
			Position position = npc.getPosition();
			Region region = position.getRegion();

			if (!region.containsNpc(position.getHeight(), npc)) {
				return;
			}

			if (!npc.isValid()) {
				return;
			}

			if (npc.id == 394 && player.getPosition().isWithinDistance(npc.getPosition(), 2)) {
				PluginManager.getDataBus().publish(player, new NpcClickEvent(1, npc));
				return;
			}

			player.walkTo(npc, () -> {
				npc.action.execute(new NpcFaceAction(npc, player.getPosition(), 0), true);

				if (EventDispatcher.execute(player, new FirstNpcClick(npc))) {
					return;
				}

				PluginManager.getDataBus().publish(player, new NpcClickEvent(1, npc));
			});

		});
	}

	private static void handleSecondClickNpc(Player player, GamePacket packet) {
		final int slot = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		World.getNpcBySlot(slot).ifPresent(npc -> {
			Position position = npc.getPosition();
			Region region = World.getRegions().getRegion(position);

			if (!region.containsNpc(position.getHeight(), npc)) {
				return;
			}

			if (!npc.isValid()) {
				return;
			}

			player.walkTo(npc, () -> {
				npc.action.execute(new NpcFaceAction(npc, player.getPosition(), 1), true);

				if (EventDispatcher.execute(player, new SecondNpcClick(npc))) {
					return;
				}

				PluginManager.getDataBus().publish(player, new NpcClickEvent(2, npc));
			});

		});
	}

	private static void handleThirdClickNpc(Player player, GamePacket packet) {
		final int slot = packet.readShort();

		World.getNpcBySlot(slot).ifPresent(npc -> {
			Position position = npc.getPosition();
			Region region = World.getRegions().getRegion(position);

			if (!region.containsNpc(position.getHeight(), npc)) {
				return;
			}

			if (!npc.isValid()) {
				return;
			}

			player.walkTo(npc, () -> {
				npc.action.execute(new NpcFaceAction(npc, player.getPosition(), 2), true);

				PluginManager.getDataBus().publish(player, new NpcClickEvent(3, npc));
			});
		});
	}

	private static void handleFourthClickNpc(Player player, GamePacket packet) {
		final int slot = packet.readShort(ByteOrder.LE);

		World.getNpcBySlot(slot).ifPresent(npc -> {
			Position position = npc.getPosition();
			Region region = World.getRegions().getRegion(position);

			if (!region.containsNpc(position.getHeight(), npc)) {
				return;
			}

			if (!npc.isValid()) {
				return;
			}

			player.walkTo(npc, () -> {
				npc.action.execute(new NpcFaceAction(npc, player.getPosition(), 3), true);

				PluginManager.getDataBus().publish(player, new NpcClickEvent(4, npc));
			});
		});
	}

}
