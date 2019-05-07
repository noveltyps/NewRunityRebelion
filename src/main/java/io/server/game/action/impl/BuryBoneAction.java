package io.server.game.action.impl;

import java.util.Optional;

import io.server.Config;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.skill.SkillAction;
import io.server.content.skill.impl.prayer.BoneData;
import io.server.game.Animation;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;

/**
 * Handles burying a bone.
 * 
 * @author Michael | Chex
 */
public final class BuryBoneAction extends SkillAction {
	private final int slot;
	private final Item item;
	private final BoneData bone;

	public BuryBoneAction(Player player, BoneData bone, int slot) {
		super(player, Optional.empty(), true);
		this.slot = slot;
		this.bone = bone;
		this.item = player.inventory.get(slot);
	}

	@Override
	public boolean canInit() {
		return getMob().skills.getSkills()[skill()].stopwatch.elapsed(1200);
	}

	@Override
	public void init() {

	}

	@Override
	public void onExecute() {
		getMob().animate(new Animation(827));
		Player player = getMob().getPlayer();
		player.inventory.remove(item, slot, true);
		player.skills.addExperience(skill(), (experience() * new ExperienceModifier(player).getModifier()));
		player.send(new SendMessage("You bury the " + item.getName() + "."));
		AchievementHandler.activate(player, AchievementKey.BURY_BONES, 1);
		cancel();
	}

	@Override
	public void onCancel(boolean logout) {
		getMob().skills.getSkills()[skill()].stopwatch.reset();
	}

	@Override
	public Optional<SkillAnimation> animation() {
		return Optional.empty();
	}

	@Override
	public double experience() {
		return bone.getExperience() * Config.PRAYER_MODIFICATION;
	}

	@Override
	public int skill() {
		return Skill.PRAYER;
	}

	@Override
	public String getName() {
		return "Bone bury";
	}

	@Override
	public boolean prioritized() {
		return false;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}
}