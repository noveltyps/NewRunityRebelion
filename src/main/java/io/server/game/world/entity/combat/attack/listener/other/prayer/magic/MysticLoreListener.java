package io.server.game.world.entity.combat.attack.listener.other.prayer.magic;

import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.mob.Mob;

public class MysticLoreListener extends SimplifiedListener<Mob> {

	@Override
	public int modifyMagicLevel(Mob attacker, Mob defender, int level) {
		return level * 11 / 10;
	}

}