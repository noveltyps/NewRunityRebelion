package io.server.content.skill.impl.thieving;

import io.server.Config;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.data.LockType;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.position.Area;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Handles the pickpocketing action.
 *
 * @author Daniel
 */
public final class PickpocketAction extends Action<Player> {
	/** The pickpocket data. */
	private final PickpocketData pickpocket;

	/** The npc being pickpocketed. */
	private final Npc npc;

	/** Constructs a new <code>PickpocketData</code>. */
	public PickpocketAction(Player player, Npc npc, PickpocketData pickpocket) {
		super(player, 3);
		this.npc = npc;
		this.pickpocket = pickpocket;
	}

	/** The failure rate for pickpocketing. */
	private int failureRate(Player player) {
		double f1 = pickpocket.getLevel() / 10;
		double f2 = 100 / ((player.skills.getMaxLevel(Skill.THIEVING) + 1) - pickpocket.getLevel());
		return (int) Math.floor((f2 + f1) / 2);
	}

	@Override
	public void execute() {
		boolean failed = Utility.random(100) < failureRate(getMob());

		if (failed) {
			npc.interact(getMob());
			npc.face(npc.faceDirection);
			npc.animate(new Animation(422));
			npc.speak("What do you think you're doing?");
			getMob().action.clearNonWalkableActions();
			getMob().damage(new Hit(Utility.random(pickpocket.getDamage())));
			getMob().locking.lock(pickpocket.getStun(), LockType.STUN);
			getMob().send(new SendMessage("You failed to pickpocketing the " + npc.getName() + "."));
			cancel();
			return;
		}

		double experience = Area.inDonatorZone(getMob()) ? pickpocket.getExperience() * 2 : pickpocket.getExperience();
		getMob().skills.addExperience(Skill.THIEVING,
				(experience * Config.THIEVING_MODIFICATION) * new ExperienceModifier(getMob()).getModifier());
		getMob().send(new SendMessage("You have successfully pickpocket the " + npc.getName() + "."));
		getMob().inventory.add(Utility.randomElement(pickpocket.getLoot()));
		getMob().locking.unlock();
		cancel();
	}

	@Override
	public String getName() {
		return "Thieving pickpocket";
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