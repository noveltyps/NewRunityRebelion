package io.server.content.skill.impl.fishing;

import io.server.content.event.impl.NpcInteractionEvent;
import io.server.content.runeliteplugin.RuneliteSkillingOverlay;
import io.server.content.skill.SkillRepository;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.util.Utility;

/**
 * Handles the fishing skill.
 *
 * @author Daniel
 */
public class Fishing extends Skill {

	public Fishing(int level, double experience) {
		super(Skill.FISHING, level, experience);
	}

	static boolean canFish(Player player, Fishable fish, boolean message) {
		if (player.skills.get(Skill.FISHING).getLevel() < fish.getRequiredLevel()) {
			if (message)
				player.message("You need a fishing level of " + fish.getRequiredLevel() + " to fish here.");
			return false;
		}
		return hasFishingItems(player, fish, message);
	}

	private static boolean hasFishingItems(Player player, Fishable fish, boolean message) {
		int tool = fish.getToolId();
		int bait = fish.getBaitRequired();
		if (!player.inventory.contains(new Item(tool, 1)) && message) {
			String name = ItemDefinition.get(tool).getName();
			player.message("You need " + Utility.getAOrAn(name) + " " + name + " to fish here.");
			return false;
		}
		if (bait > -1 && !player.inventory.contains(new Item(bait, 1))) {
			String name = ItemDefinition.get(bait).getName();
			if (message) {
				player.message("You need " + Utility.getAOrAn(name) + " " + name + " to fish here.");
			}
			return false;
		}
		return true;
	}

	public static boolean success(Player player, Fishable fish) {
		return SkillRepository.isSuccess(player, Skill.FISHING, fish.getRequiredLevel());
	}

	@Override
	protected boolean clickNpc(Player player, NpcInteractionEvent event) {
		Npc npc = event.getNpc();
		int opcode = event.getOpcode();
		FishingSpot spot = FishingSpot.forId(npc.id);

		if (spot == null) {
			return false;
		}
		if (player.skills.get(Skill.FISHING).isDoingSkill()) {
			return true;
		}

		int amount = 0;
		Fishable[] fish;
		Fishable[] fishable = new Fishable[3];

		switch (opcode) {
		case 0:
			fish = spot.getFirstOption();
			for (int i = 0; i < fish.length; i++) {
				if (canFish(player, fish[i], i == 0)) {
					fishable[i] = fish[i];
					amount++;
				}
			}
			break;
		case 1:
			fish = spot.getSecondOption();
			for (int i = 0; i < fish.length; i++) {
				if (canFish(player, fish[i], i == 0)) {
					fishable[i] = fish[i];
					amount++;
				}
			}
		}

		if (amount == 0)
			return true;

		Fishable[] fishing = new Fishable[amount];
		System.arraycopy(fishable, 0, fishing, 0, amount);
		start(player, fishing, 0);
		return true;
	}
	

	public void start(Player player, Fishable[] fishing, int option) {
		if (fishing == null || fishing[option] == null || fishing[option].getToolId() == -1) {
			return;
		}

		FishingTool tool = FishingTool.forId(fishing[option].getToolId());

		if (!hasFishingItems(player, fishing[option], true)) {
			return;
		}

		player.action.execute(new FishingAction(player, tool, fishing));
		player.skills.get(Skill.FISHING).setDoingSkill(true);
		new RuneliteSkillingOverlay(player).onOpen(player.random, "Fishing", "Fishes");
	}
}
