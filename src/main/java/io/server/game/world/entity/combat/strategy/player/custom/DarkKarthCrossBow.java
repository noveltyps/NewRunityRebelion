package io.server.game.world.entity.combat.strategy.player.custom;

import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.NpcAssistant;
import io.server.game.world.entity.mob.player.Player;

public class DarkKarthCrossBow extends PlayerRangedStrategy {
	private static final DarkKarthCrossBow INSTANCE = new DarkKarthCrossBow();

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return increase10percent(defender, roll);
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int roll) {
		return increase10percent(defender, roll);
	}

	private static int increase10percent(Mob defender, int roll) {
		if (NpcAssistant.isDragon(defender.id))
			return roll * 11 / 10;
		return roll;
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
			return 2;
	}

	public static DarkKarthCrossBow get() {
		return INSTANCE;
	}

}
