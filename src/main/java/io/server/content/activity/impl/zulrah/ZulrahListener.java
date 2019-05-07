package io.server.content.activity.impl.zulrah;

import io.server.content.activity.ActivityListener;
import io.server.game.world.entity.mob.Mob;

class ZulrahListener extends ActivityListener<ZulrahActivity> {

	ZulrahListener(ZulrahActivity activity) {
		super(activity);
	}

	@Override
	public boolean canOtherAttack(Mob attacker, Mob defender) {
		return true;
	}

	@Override
	public boolean canAttack(Mob attacker, Mob defender) {
		if (attacker.isNpc() && attacker.getNpc().id != 2045) {
			return activity.attackable;
		}
		return true;
	}
}
