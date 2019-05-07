package io.server.content.skill.impl;

import java.util.Optional;

import io.server.content.skill.SkillAction;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomGen;

/**
 * The skill action that represents an action where one item is removed from an
 * inventory and lost forever. This type of skill action is very basic and only
 * requires that a player have the item to destruct in their inventory.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code PRAYER}.
 * 
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see HarvestingSkillAction
 */
public abstract class DestructionSkillAction extends SkillAction {

	private static final int SUCCESS_FACTOR = 10;
	private final RandomGen random = new RandomGen();

	/**
	 * Creates a new {@link DestructionSkillAction}.
	 * 
	 * @param mob      the mob this skill action is for.
	 * @param position the position the player should face.
	 * @param instant  determines if this task should run instantly.
	 */
	public DestructionSkillAction(Mob mob, Optional<Position> position, boolean instant) {
		super(mob, position, instant);
	}

	@Override
	public boolean canRun() {
		String name = ItemDefinition.get(destructItem().getId()).getName();
		if (getMob().isPlayer() && !getMob().getPlayer().inventory.contains(destructItem().getId())) {
			getMob().getPlayer().send(new SendMessage("You do not have any " + name + " in your inventory."));
			return false;
		}
		return true;
	}

	public abstract double successFactor();

	@Override
	public final void onExecute() {
		int factor = (getMob().skills.getSkills()[skill()].getLevel() / SUCCESS_FACTOR);
		double boost = (factor * 0.01);
		if (random.success((successFactor() + boost))) {
			if (getMob().isPlayer()) {
				onDestruct(true);
				getMob().skills.addExperience(skill(), experience());
				this.cancel();
				return;
			}
		} else {
			onDestruct(false);
		}
	}

	@Override
	public final WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	/**
	 * The method executed upon destruction of the item.
	 * 
	 * @param success determines if the destruction was successful or not.
	 */
	public void onDestruct(boolean success) {

	}

	/**
	 * The item that will be removed upon destruction.
	 * 
	 * @return the item that will be removed.
	 */
	public abstract Item destructItem();

	@Override
	public boolean prioritized() {
		return false;
	}
}