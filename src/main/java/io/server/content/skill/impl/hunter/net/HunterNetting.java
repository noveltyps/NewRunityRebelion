package io.server.content.skill.impl.hunter.net;

import java.util.Optional;

import io.server.Config;
import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.skill.impl.hunter.Hunter;
import io.server.content.skill.impl.hunter.net.impl.Butterfly;
import io.server.content.skill.impl.hunter.net.impl.Impling;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.action.impl.ImplingTeleportAction;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.npc.drop.NpcDrop;
import io.server.game.world.entity.mob.npc.drop.NpcDropManager;
import io.server.game.world.entity.mob.npc.drop.NpcDropTable;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class HunterNetting {

	public static void catchImpling(Player player, Npc npc, Impling impling, Position original) {
		if (!player.equipment.contains(10010) && !player.inventory.contains(10010)) {
			player.send(new SendMessage("You need a butterfly net to capture this impling."));
			return;
		}

		if (!player.inventory.contains(11260)) {
			player.send(new SendMessage("You need a impling jar to hold this impling."));
			return;
		}

		if (player.skills.getLevel(Skill.HUNTER) < impling.getLevel()) {
			player.send(
					new SendMessage("You need a hunter level of " + impling.getLevel() + " to catch this impling."));
			return;
		}

		Item item = new Item(impling.getReward(), 1);

		if (!player.inventory.hasCapacityFor(item)) {
			player.send(new SendMessage("You do not have enough inventory space to hold this impling."));
			return;
		}

		if (player.skills.getLevel(Skill.STRENGTH) >= 1) {
			player.animate(new Animation(6606));
			player.action.execute(catchImpling(player, npc, original, new Item(11260), item, impling.getExperience()),
					true);
		}
	}

	public static void catchButterfly(Player player, Npc npc, Butterfly butterfly, Position original) {
		if (!player.equipment.contains(10010) && !player.inventory.contains(10010)) {
			player.send(new SendMessage("You need a butterfly net to capture this butterfly."));
			return;
		}

		if (!player.inventory.contains(10012)) {
			player.send(new SendMessage("You need a butterfly jar to hold this impling."));
			return;
		}

		if (player.skills.getLevel(Skill.HUNTER) < butterfly.getLevel()) {
			player.send(new SendMessage(
					"You need a hunter level of " + butterfly.getLevel() + " to catch this butterfly."));
			return;
		}

		Item item = new Item(butterfly.getItem(), 1);

		if (!player.inventory.hasCapacityFor(item)) {
			player.send(new SendMessage("You do not have enough inventory space to hold this butterfly."));
			return;
		}

		if (player.skills.getLevel(Skill.STRENGTH) >= 1) {
			player.animate(new Animation(6606));
			player.action.execute(catchImpling(player, npc, original, new Item(11260), item, butterfly.getExperience()),
					true);
		}
	}

	private static Action<Player> catchImpling(Player player, Npc npc, Position original, Item first, Item second,
			int experience) {
		return new Action<Player>(player, 1) {

			@Override
			public void execute() {
				boolean success = false;

				if (npc.getPosition().equals(original)) {
					success = true;
				}

				if (Utility.random(3) == 1) {
					success = true;
				}

				if (success) {
					npc.locking.lock();
					npc.move(new Position(1, 1));
					Hunter.SPAWNS.remove(npc.id, npc.spawnPosition);
					npc.action.execute(new ImplingTeleportAction(npc), true);
					player.inventory.replace(first.getId(), second.getId(), true);
					player.skills.addExperience(Skill.HUNTER,
							(experience * Config.HUNTER_MODIFICATION) * new ExperienceModifier(player).getModifier());
					player.send(new SendMessage("You catch the " + npc.getName() + " and place it in the jar."));
					RandomEventHandler.trigger(player);
				} else {
					player.send(new SendMessage("You fail to catch the " + npc.getName() + "."));
				}
				cancel();
			}

			@Override
			public String getName() {
				return "Catch impling";
			}

			@Override
			public boolean prioritized() {
				return false;
			}

			@Override
			public WalkablePolicy getWalkablePolicy() {
				return WalkablePolicy.NON_WALKABLE;
			}
		};
	}

	public static void lootJar(Player player, Item item) {
		Optional<Impling> imp = Impling.forItem(item.getId());
		if (imp.isPresent()) {
			NpcDropTable table = NpcDropManager.NPC_DROPS.get(imp.get().getImpling());
			player.inventory.remove(item);
			player.inventory.add(11260);
			int randomIdx = Utility.random(0, table.drops.length-1, true);
			boolean valid = table != null
					&& (randomIdx < table.drops.length && (NpcDefinition.get(table.npcIds[0]) != null));
			NpcDrop drop = valid ? table.drops[randomIdx] : null;
			Item item2 = valid ? new Item(drop.item, drop.maximum) : null;
			player.inventory.add(item2);
		}
	}
}
