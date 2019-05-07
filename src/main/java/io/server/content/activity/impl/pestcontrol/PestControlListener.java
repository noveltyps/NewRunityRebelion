package io.server.content.activity.impl.pestcontrol;

import io.server.content.activity.ActivityListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;

/**
 * Created by Dani.
 */
public class PestControlListener extends ActivityListener<PestControl> {

	PestControlListener(PestControl activity) {
		super(activity);
	}

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
	}

	@Override
	public void onDeath(Mob attacker, Mob defender, Hit hit) {
	}
}
