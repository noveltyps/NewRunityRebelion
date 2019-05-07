package io.server.game.world.entity.mob.movement.waypoint;

import io.server.content.activity.Activity;
import io.server.content.activity.impl.kraken.KrakenActivity;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.movement.Movement;
import io.server.util.Utility;

public class CombatWaypoint extends Waypoint {

	public CombatWaypoint(Mob mob, Mob target) {
		super(mob, target);
	}

	@Override
	public void onDestination() {
		mob.movement.reset();
	}

	@Override
	protected boolean withinDistance() {
		if (target.equals(mob.getCombat().getDefender())) {
			return mob.isPlayer() && Activity.evaluate(mob.getPlayer(), it -> {
				if (it instanceof KrakenActivity) {
					Mob kraken = ((KrakenActivity) it).kraken;
					return Utility.getDistance(mob, kraken) <= getRadius()
							&& mob.getStrategy().withinDistance(mob, kraken);
				}
				return false;
			}) || Utility.getDistance(mob, target) <= getRadius()
					&& mob.getStrategy().withinDistance(mob, (Mob) target);
		}
		return super.withinDistance();
	}

	@Override
	protected int getRadius() {
		if (target.equals(mob.getCombat().getDefender())) {
			FightType fightType = mob.getCombat().getFightType();
			Movement movement = mob.movement;
			int radius = mob.getStrategy().getAttackDistance(mob, fightType);

			if (movement.needsPlacement() && !mob.locking.locked()) {
				radius++;
				if (movement.isRunning())
					radius++;
			}
			return radius;
		}
		return 1;
	}

}