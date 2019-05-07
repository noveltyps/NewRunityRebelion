package io.server.content.skill.impl.mining;

import java.util.Arrays;

import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.content.runeliteplugin.RuneliteSkillingOverlay;
import io.server.content.skill.SkillRepository;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.object.GameObject;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;
import io.server.util.chance.Chance;
import io.server.util.chance.WeightedChance;

/**
 * Handles the mining skill.
 *
 * @author Daniel
 */
public final class Mining extends Skill {

	static final Chance<Item> GEM_ITEMS = new Chance<>(Arrays.asList(new WeightedChance<>(6, new Item(1623, 1)), // SAPPHIRE
			new WeightedChance<>(5, new Item(1621, 1)), // EMERALD
			new WeightedChance<>(4, new Item(1619, 1)), // RUBY
			new WeightedChance<>(3, new Item(1617, 1)), // DIAMOND
			new WeightedChance<>(1, new Item(1631, 1)) // DRAGON_STONE
	));

	/** Constructs a new {@link Mining} skill. */
	public Mining(int level, double experience) {
		super(Skill.MINING, level, experience);
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		GameObject object = event.getObject();
		OreData ore = OreData.forId(object.getDefinition().getId());

		if (ore == null) {
			return false;
		}

		if (!object.active()) {
			return false;
		}

		switch (event.getType()) {
		case FIRST_CLICK_OBJECT:
			attempt(player, object, ore);

			break;
		case SECOND_CLICK_OBJECT:
			player.send(new SendMessage("You examine the rock for ores..."));
			World.schedule(2,
					() -> player.send(new SendMessage("This rock contains " + Utility.formatEnum(ore.name()) + ".")));
			break;
		default:
			player.send(new SendMessage("Nothing interesting happens.."));
			break;
		}
		return true;
	}

	private void attempt(Player player, GameObject object, OreData ore) {
		PickaxeData pickaxe = PickaxeData.getBestPickaxe(player).orElse(null);

		if (pickaxe == null) {
			player.message("You don't have a pickaxe.");
			return;
		}

		if (!player.skills.get(Skill.MINING).reqLevel(pickaxe.level)) {
			player.message("You need a level of " + pickaxe.level + " to use this pickaxe!");
			return;
		}

		if (!player.skills.get(Skill.MINING).reqLevel(ore.level)) {
			player.message("You need a mining level of " + ore.level + " to mine this ore!");
			return;
		}

		start(player, object, ore, pickaxe);
	}

	private void start(Player player, GameObject object, OreData ore, PickaxeData pickaxe) {
		if (!object.getGenericAttributes().has("ores")) {
			object.getGenericAttributes().set("ores", ore.oreCount);
		}

		player.action.execute(new MiningAction(player, object, ore, pickaxe));
		player.skills.get(Skill.MINING).setDoingSkill(true);
		player.message("You swing your pick at the rock...");
		new RuneliteSkillingOverlay(player).onOpen(player.random, "Mining", "Ores");
	}

	public static boolean success(Player player, OreData ore, PickaxeData pickaxe) {
		return SkillRepository.isSuccess(player, Skill.MINING, ore.level, pickaxe.level);
	}
}
