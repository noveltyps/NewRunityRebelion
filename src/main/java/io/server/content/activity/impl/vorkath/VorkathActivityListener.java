package io.server.content.activity.impl.vorkath;

import io.server.content.activity.ActivityListener;
import io.server.game.world.Interactable;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.position.Position;
import io.server.util.Utility;

public class VorkathActivityListener extends ActivityListener<VorkathActivity> {

	VorkathActivityListener(VorkathActivity vorkathActivity) {
		super(vorkathActivity);
	}

	@Override
	public boolean withinDistance(Mob attacker, Mob defender) {
		if (!attacker.isPlayer())
			return true;
		FightType fightType = attacker.getCombat().getFightType();
		int distance = attacker.getStrategy().getAttackDistance(attacker, fightType);
		Interactable vorkathh = Interactable.create(new Position(2269, 4062, attacker.getHeight()), 4, 4);
		return Utility.getDistance(attacker, vorkathh) <= distance
				&& attacker.getStrategy().withinDistance(attacker, activity.vorkath);
	}

	@Override
	public boolean canAttack(Mob attacker, Mob defender) {
		return activity.vorkath == null || !activity.vorkath.isDead();
	}

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		if (!attacker.isPlayer() && !defender.isNpc()) {
			return;
		}
	}

	@Override
	public void onDeath(Mob attacker, Mob defender, Hit hit) {
	}
}