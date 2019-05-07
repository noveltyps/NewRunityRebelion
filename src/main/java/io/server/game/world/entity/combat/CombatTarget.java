package io.server.game.world.entity.combat;

import java.util.concurrent.TimeUnit;

import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.pathfinding.path.SimplePathChecker;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.util.Utility;

public class CombatTarget {

	/** The aggression timeout in minutes. */
	private static final int AGGRESSION_TIMEOUT = 20;
	private final Mob mob;
	private Mob target;
	private int distance;

	CombatTarget(Mob mob) {
		this.mob = mob;
		this.distance = Integer.MAX_VALUE;
	}

	/** Checks the aggression for this mob if a target is set. */
	void checkAggression(int level, Position spawn) {
		/* No target */
		if (target == null)
			return;

		/* The target is unreachable */
		if (distance == Integer.MAX_VALUE)
			return;

		/* The mob is too far from spawn */
		if (!Area.inGodwars(mob) && Utility.getDistance(mob, spawn) > Region.VIEW_DISTANCE) {
			Mob trgt = target;
			distance = Integer.MAX_VALUE;
			mob.getCombat().reset();
			if (mob.isNpc() && mob.getNpc().boundaries.length > 0) {
				Position pos = Utility.randomElement(mob.getNpc().boundaries);
				mob.interact(trgt);
				mob.walkExactlyTo(pos, () -> {
					mob.resetFace();
					resetTarget();
				});
			}
			return;
		}

		if (target.skills.getCombatLevel() > level * 2 && !Area.inWilderness(mob))
			return;

		int dist = Utility.getDistance(target, mob);
		int aggressionRadius = mob.width() + 5;

		if (Area.inGodwars(mob)) {
			aggressionRadius = Region.SIZE;
		}

		/* The mob is too far from target */
		if (dist > aggressionRadius)
			return;

		/* The mob is already in combat with the target */
		if (mob.getCombat().isAttacking(target))
			return;

		if (!mob.getCombat().attack(target)) {
			mob.getCombat().reset();
		}
	}

	/**
	 * Compares the given mob with the current target. If the give mob is closer
	 * than the current target, the target will be set to the given mob.
	 *
	 * @param other the mob to compare to the target
	 */
	void compare(Mob other) {
		int dist = Utility.getDistance(mob, other);

		/* The npc is too far from target */
		if (dist > Region.VIEW_DISTANCE || dist >= distance)
			return;

		/* Found a closer target */
		if (!isTarget(other) && SimplePathChecker.checkProjectile(mob, other)) {
			target = other;
			distance = dist;
		}
	}

	public static void checkAggression(Player player) {
		if (!player.isVisible())
			return;

		if (player.viewport.getNpcsInViewport().isEmpty())
			return;

		if (player.getCombat().inCombat() && !Area.inMulti(player))
			return;

		for (Npc npc : player.viewport.getNpcsInViewport()) {
			if (npc == null || !npc.isValid())
				continue;

			if (!npc.definition.isAttackable() || !npc.definition.isAggressive())
				continue;

			if (npc.isDead() || !npc.isVisible() || npc.forceWalking)
				continue;

			if (npc.locking.locked())
				continue;

			if (player.aggressionTimer.elapsed(AGGRESSION_TIMEOUT, TimeUnit.MINUTES) && !Area.inGodwars(npc)) {
				if (npc.getCombat().isAttacking(player) && !player.getCombat().isAttacking(npc))
					npc.getCombat().reset();
				continue;
			}

			npc.getCombat().compare(player);
		}
	}

	public void resetTarget() {
		if (mob.isPlayer()) {
			mob.getPlayer().playerAssistant.sendOpponentStatsInterface(false, null);
		}
		target = null;
		distance = Integer.MAX_VALUE;
	}

	public boolean isTarget(Mob mob) {
		return mob.equals(target);
	}

	public Mob getTarget() {
		return target;
	}

	public void setTarget(Mob target) {
		if (mob.isPlayer() && target.isPlayer()) {
			mob.getPlayer().playerAssistant.sendOpponentStatsInterface(true, target.getPlayer());
		}
		this.target = target;
		distance = Utility.getDistance(mob, target);
	}

}
