package io.server.content.skill.impl.fishing;

import io.server.Config;
import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.clanchannel.content.ClanTaskKey;
import io.server.content.dailyTasks.DailyTaskType;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.pet.PetData;
import io.server.content.pet.Pets;
import io.server.content.prestige.PrestigePerk;
import io.server.content.runeliteplugin.RuneliteSkillingOverlay;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * Created by Daniel on 2017-12-18.
 */
public class FishingAction extends Action<Player> {
	private final FishingTool tool;
	private final Fishable[] fishing;

	FishingAction(Player player, FishingTool tool, Fishable[] fishing) {
		super(player, 6, false);
		this.tool = tool;
		this.fishing = fishing;
	}
	
	public int currentFish;

	private boolean fish(Player player, Fishable[] fishing) {
		if (fishing == null)
			return false;

		Fishable[] fish = new Fishable[5];

		byte c = 0;

		for (Fishable aFishing : fishing) {
			if (Fishing.canFish(player, aFishing, false)) {
				fish[c] = aFishing;
				c = (byte) (c + 1);
			}
		}
		if (c == 0) {
			return false;
		}

		Fishable f = fish[Utility.random(c)];

		if (player.inventory.getFreeSlots() == 0) {
			player.dialogueFactory.sendStatement("You can't carry anymore fish.").execute();
			return false;
		}

		if (Fishing.success(player, f)) {
			if (f.getBaitRequired() != -1) {
				player.inventory.remove(new Item(f.getBaitRequired(), 1));
				if (!player.inventory.contains(f.getBaitRequired(), 1)) {
					player.dialogueFactory.sendStatement("You have run out of bait.").execute();
					return false;
				}
			}
			
			if (getMob().getDailyTask().getType() == DailyTaskType.SKILLING_TASK && getMob().getDailyTask().getSkillID() == 10)
				getMob().getDailyTask().process(getMob());

			int id = f.getRawFishId();
			int money = f.getMoney();
			String name = ItemDefinition.get(id).getName();
			player.inventory.add(new Item(id, 1));
			player.inventory.add(new Item(995, money));
			player.skills.addExperience(Skill.FISHING,
					(f.getExperience() * Config.FISHING_MODIFICATION) * new ExperienceModifier(player).getModifier());
			player.message("You manage to catch a " + name + ".");
			RandomEventHandler.trigger(player);
			new RuneliteSkillingOverlay(getMob()).onUpdate(getMob().currentCaught, "Fishes Caught");
			getMob().currentCaught++;
			Pets.onReward(player, PetData.HERON.getItem(), 297_647);

			if (player.prestige.hasPerk(PrestigePerk.MASTERBAIRTER) && RandomUtils.success(.15)) {
				player.inventory.addOrDrop(new Item(id, 1));
			}

			if (id == 383) {
				player.forClan(channel -> channel.activateTask(ClanTaskKey.SHARK, player.getName()));
			} else if (id == 11934) {
				player.forClan(channel -> channel.activateTask(ClanTaskKey.DARK_CRAB, player.getName()));
			}
		}
		return true;
	}

	@Override
	protected boolean canSchedule() {
		return !getMob().skills.get(Skill.FISHING).isDoingSkill();
	}

	@Override
	protected void onSchedule() {
		getMob().animate(tool.getAnimationId());
	}

	@Override
	public void execute() {
		if (!getMob().skills.get(Skill.FISHING).isDoingSkill()) {
			cancel();
			return;
		}

		getMob().animate(tool.getAnimationId());

		if (!fish(getMob(), fishing)) {
			cancel();
		}
	}

	@Override
	protected void onCancel(boolean logout) {
		getMob().resetFace();
		getMob().skills.get(Skill.FISHING).setDoingSkill(false);
		new RuneliteSkillingOverlay(getMob()).onClose();
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	@Override
	public String getName() {
		return "fishing-action";
	}
}
