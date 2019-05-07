package io.server.game.world.entity.combat.attack.listener.other.prayer;

import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.mob.Mob;

public class RigourListener extends SimplifiedListener<Mob> {

	@Override
	public int modifyRangedLevel(Mob attacker, Mob defender, int level) {
		return level * 8 / 7;
	}

	@Override
	public int modifyDamage(Mob attacker, Mob defender, int damage) {
		if (attacker.getStrategy().getCombatType() != CombatType.RANGED)
			return damage;
		return damage * 120 / 117;
	}

	@Override
	public int modifyDefenceLevel(Mob attacker, Mob defender, int level) {
		return level * 6 / 5;
	}

}
