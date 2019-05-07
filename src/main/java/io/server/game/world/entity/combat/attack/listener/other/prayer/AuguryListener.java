package io.server.game.world.entity.combat.attack.listener.other.prayer;

import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.mob.Mob;

public class AuguryListener extends SimplifiedListener<Mob> {

	@Override
	public int modifyMagicLevel(Mob attacker, Mob defender, int level) {
		return level * 6 / 5;
	}

	@Override
	public int modifyDefenceLevel(Mob attacker, Mob defender, int damage) {
		return damage * 6 / 5;
	}

}
