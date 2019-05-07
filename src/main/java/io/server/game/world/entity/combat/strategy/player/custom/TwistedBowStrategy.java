package io.server.game.world.entity.combat.strategy.player.custom;

import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;

/**
 * @author Adam_#6723
 */
public class TwistedBowStrategy extends PlayerRangedStrategy {
	private static final TwistedBowStrategy INSTANCE = new TwistedBowStrategy();

	private TwistedBowStrategy() {
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		int level = defender.skills.getMaxLevel(Skill.MAGIC);
		if (level > 360)
			level = 360;
		int a = (3 * level) / 10 - 100;
		int mod = 140 + (3 * level - 10) / 100 - (a * a) / 100;
		if (mod > 140)
			mod = 140;
		return roll * mod / 100;
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int roll) {
		int level = defender.skills.getMaxLevel(Skill.MAGIC);
		if (level > 360)
			level = 360;
		int a = (3 * level) / 10 - 140;
		int mod = 250 + (3 * level - 14) / 100 - (a * a) / 100;
		if (mod > 250)
			mod = 250;
		return roll * mod / 100;
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 5;
	}

	public static TwistedBowStrategy get() {
		return INSTANCE;
	}

}