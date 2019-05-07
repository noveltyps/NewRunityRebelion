package io.server.content.activity.impl.bosscontrol;

import io.server.content.activity.ActivityListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;

/**
 * Created by Daniel on 2017-09-29.
 */
public class BossControlListener extends ActivityListener<BossControl> {

	BossControlListener(BossControl activity) {
		super(activity);
	}

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
	}

	@Override
	public void onDeath(Mob attacker, Mob defender, Hit hit) {
	}
}
