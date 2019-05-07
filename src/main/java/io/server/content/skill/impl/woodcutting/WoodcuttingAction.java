package io.server.content.skill.impl.woodcutting;

import io.server.Config;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
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
import io.server.game.task.impl.ObjectReplacementEvent;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.object.GameObject;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;

/**
 * Handles the woodcutting action event.
 *
 * @author Daniel
 */
public class WoodcuttingAction extends Action<Player> {
	private final GameObject object;
	private final TreeData tree;
	private final AxeData axe;
	
	WoodcuttingAction(Player mob, GameObject object, TreeData tree, AxeData axe) {
		super(mob, 3, false);
		this.object = object;
		this.tree = tree;
		this.axe = axe;
	}

	private boolean chop() {
		if (getMob().inventory.getFreeSlots() == 0) {
			getMob().dialogueFactory.sendStatement("You can't carry anymore log.").execute();
			return false;
		}
		getMob().animate(axe.animation);
		if (Woodcutting.success(getMob(), tree, axe)) {
			if (object == null || !object.active()) {
				return false;
			}
			
			if (getMob().getDailyTask().getType() == DailyTaskType.SKILLING_TASK && getMob().getDailyTask().getSkillID() == 8)
				getMob().getDailyTask().process(getMob());

			int amount = object.getGenericAttributes().get("logs", Integer.class);

			if (amount == -1) {
				getMob().resetAnimation();
				System.err.println("Stops Here");
				return false;
			}

			object.getGenericAttributes().modify("logs", amount - 1);

			if (object.getId() != 2092) // AFK tree doesn't give birds nests, randoms
			{
				BirdsNest.drop(getMob());
				RandomEventHandler.trigger(getMob());
				getMob().send(new SendMessage("You get some " + ItemDefinition.get(tree.item).getName() + "."));
			} else {
				// bonus logs cus why not
				int bonus = RandomUtils.inclusive(0, 6);
				for (int i = 0; i < bonus; i++) {
					getMob().forClan(channel -> channel.activateTask(ClanTaskKey.AFK_LOG, getMob().getName()));
					getMob().inventory.add(tree.item, 1);
					getMob().inventory.refresh();
					getMob().inventory.add(995, tree.getMoney());
				}
				object.getGenericAttributes().modify("logs", amount - 1 - bonus);
			}
			new RuneliteSkillingOverlay(getMob()).onUpdate(getMob().currentCut, "Logs Cut");
			getMob().currentCut++;
			Pets.onReward(getMob(), PetData.BEAVER.getItem(), tree.petRate);
			getMob().inventory.add(tree.item, 1);
			getMob().skills.addExperience(Skill.WOODCUTTING, (tree.experience * Config.WOODCUTTING_MODIFICATION)
					* new ExperienceModifier(getMob()).getModifier());

			if (getMob().prestige.hasPerk(PrestigePerk.DOUBLE_WOOD) && RandomUtils.success(.15)) {
				getMob().inventory.addOrDrop(new Item(tree.item, 1));
			}

			if (getMob().equipment.contains(13241)) {
				getMob().skills.addExperience(Skill.WOODCUTTING, (tree.experience * Config.MINING_MODIFICATION * 5)
						* new ExperienceModifier(getMob()).getModifier());
				getMob().skills.addExperience(Skill.FIREMAKING, (tree.experience * Config.FIREMAKING_MODIFICATION * 5)
						* new ExperienceModifier(getMob()).getModifier());
				getMob().message("You are now recieving 5x Woodcutting Experience.");
			}
			if (tree == TreeData.NORMAL_TREE) {
				getMob().inventory.add(995, tree.getMoney());
			}
			if (tree == TreeData.WILLOW_TREE || tree == TreeData.WILLOW_TREE1) {
				getMob().inventory.add(995, tree.getMoney());
			}
			if (tree == TreeData.MAPLE_TREE) {
				getMob().inventory.add(995, tree.getMoney());
			}

			/*
			 * for(TreeData data : TreeData.values()) { if(data.)
			 * getMob().inventory.add(995, tree.getMoney()); }
			 */

			if (tree == TreeData.OAK_TREE || tree == TreeData.OAK_TREE) {
				AchievementHandler.activate(getMob(), AchievementKey.CUT_A_OAK_TREE, 1);
				getMob().inventory.add(995, tree.getMoney());
			}
			/** CUT 100 TREES **/
			if (tree == TreeData.OAK_TREE || tree == TreeData.ACHEY_TREE || tree == TreeData.NORMAL_TREE
					|| tree == TreeData.WILLOW_TREE || tree == TreeData.WILLOW_TREE1 || tree == TreeData.YEW_TREE) {
				AchievementHandler.activate(getMob(), AchievementKey.CUT100TREES, 1);
			}

			if (tree == TreeData.WILLOW_TREE || tree == TreeData.WILLOW_TREE1) {
				getMob().forClan(channel -> channel.activateTask(ClanTaskKey.CHOP_WILLOW_LOG, getMob().getName()));
				getMob().inventory.add(995, tree.getMoney());
			}
			if (tree == TreeData.YEW_TREE) {
				getMob().forClan(channel -> channel.activateTask(ClanTaskKey.YEW_LOG, getMob().getName()));
				AchievementHandler.activate(getMob(), AchievementKey.CUT_YEWTREE, 1);
				getMob().inventory.add(995, tree.getMoney());
			}
			if (tree == TreeData.MAGIC_TREE) {
				getMob().forClan(channel -> channel.activateTask(ClanTaskKey.MAGIC_LOG, getMob().getName()));
				AchievementHandler.activate(getMob(), AchievementKey.CUT_MAGICTREE, 1);
				getMob().inventory.add(995, tree.getMoney());

			}

			getMob().forClan(channel -> channel.activateTask(ClanTaskKey.CHOP_ANY_LOG, getMob().getName()));

			if (object.active() && object.getGenericAttributes().get("logs", Integer.class) <= -1) {
				this.cancel();
				getMob().resetAnimation();
				AchievementHandler.activate(getMob(), AchievementKey.WOODCUTTING, 1);
				object.getGenericAttributes().set("logs", -1);
				World.schedule(new ObjectReplacementEvent(object, tree.replacement, tree.respawn, () -> {
					object.getGenericAttributes().set("logs", tree.logs);
				}));
			}
		}
		return true;
	}

	@Override
	protected boolean canSchedule() {
		return !getMob().skills.get(Skill.WOODCUTTING).isDoingSkill();
	}

	@Override
	protected void onSchedule() {
		if (!object.getGenericAttributes().has("logs")) {
			object.getGenericAttributes().set("logs", tree.logs);
		}

		getMob().animate(axe.animation);
	}

	@Override
	public void execute() {
		if (!getMob().skills.get(Skill.WOODCUTTING).isDoingSkill()) {
			cancel();
			return;
		}
		if (object == null || !object.active() || object.getGenericAttributes() == null) {
			cancel();
			return;
		}

		if (!chop()) {
			cancel();
			new RuneliteSkillingOverlay(getMob()).onClose();
		}
	}

	@Override
	protected void onCancel(boolean logout) {
		getMob().resetFace();
		getMob().skills.get(Skill.WOODCUTTING).setDoingSkill(false);
		new RuneliteSkillingOverlay(getMob()).onClose();
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	@Override
	public String getName() {
		return "woodcutting-action";
	}
}
