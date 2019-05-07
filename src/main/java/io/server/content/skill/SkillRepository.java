package io.server.content.skill;

import java.util.ArrayList;
import java.util.List;

import io.server.Config;
import io.server.content.skill.impl.agility.Agility;
import io.server.content.skill.impl.crafting.impl.Gem;
import io.server.content.skill.impl.crafting.impl.Hide;
import io.server.content.skill.impl.fishing.Fishable;
import io.server.content.skill.impl.fishing.FishingSpot;
import io.server.content.skill.impl.fishing.FishingTool;
import io.server.content.skill.impl.fletching.impl.Arrow;
import io.server.content.skill.impl.fletching.impl.Bolt;
import io.server.content.skill.impl.fletching.impl.Carvable;
import io.server.content.skill.impl.fletching.impl.Crossbow;
import io.server.content.skill.impl.fletching.impl.Featherable;
import io.server.content.skill.impl.fletching.impl.Stringable;
import io.server.content.skill.impl.hunter.Hunter;
import io.server.content.skill.impl.hunter.net.impl.Butterfly;
import io.server.content.skill.impl.hunter.net.impl.Impling;
import io.server.content.skill.impl.mining.OreData;
import io.server.content.skill.impl.woodcutting.TreeData;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;
import io.server.util.Utility;

public class SkillRepository {

	/** Holds all the impling spawns. */
	public static final List<Integer> HUNTER_SPAWNS = new ArrayList<>();

	public static final List<Integer> SKILLING_ITEMS = new ArrayList<>();

	/** Holds all the impling onSpawn locations. */
	private static final Position[] HUNTER_SPAWN_POSITION = { new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329), new Position(2591, 4329) };

	/** Spawns all the skilling npcs. */
	public static void spawn() {
		for (Position position : HUNTER_SPAWN_POSITION) {
			if (!Hunter.SPAWNS.containsValue(position)) {
				int identification = Utility.randomElement(HUNTER_SPAWNS);
				Hunter.SPAWNS.put(identification, position);
				new Npc(identification, position, Config.NPC_WALKING_RADIUS, Direction.NORTH).register();
			}
		}
	}

	/** Loads all the skilling data. */
	public static void load() {
		Gem.load();
		Hide.load();
		Arrow.load();
		Carvable.load();
		Bolt.load();
		Crossbow.load();
		Featherable.load();
		Stringable.load();
		Impling.addList();
		Butterfly.addList();
		Agility.declare();
		FishingTool.declare();
		Fishable.declare();
		FishingSpot.declare();
		spawn();
		declareItems();
	}

	private static void declareItems() {
		for (TreeData tree : TreeData.values()) {
			SKILLING_ITEMS.add(tree.item);
		}
		for (OreData ore : OreData.values()) {
			SKILLING_ITEMS.add(ore.ore);
		}
		for (Fishable fish : Fishable.values()) {
			SKILLING_ITEMS.add(fish.getRawFishId());
		}
	}

	public static boolean isSkillingItem(int item) {
		for (int id : SKILLING_ITEMS) {
			if (item == id)
				return true;
		}
		return false;
	}

	public static boolean isSuccess(int skill, int levelRequired) {
		double successChance = Math
				.ceil(((double) skill * 50.0D - (double) levelRequired * 15.0D) / (double) levelRequired / 3.0D * 4.0D);
		int roll = Utility.random(99);
		return successChance >= roll;
	}

	public static boolean isSuccess(Player p, int skillId, int levelRequired) {
		double level = p.skills.getMaxLevel(skillId);
		double successChance = Math
				.ceil((((level * 50.0D) - ((double) levelRequired * 15.0D)) / (double) levelRequired / 3.0D) * 4.0D);
		int roll = Utility.random(99);
		return successChance >= roll;
	}

	public static boolean isSuccess(Player p, int skill, int levelRequired, int toolLevelRequired) {
		double level = (p.skills.getMaxLevel(skill) + toolLevelRequired) / 2.0D;
		double successChance = Math
				.ceil((((level * 50.0D) - ((double) levelRequired * 15.0D)) / (double) levelRequired / 3.0D) * 4.0D);
		int roll = Utility.random(99);
		return successChance >= roll;
	}
}
