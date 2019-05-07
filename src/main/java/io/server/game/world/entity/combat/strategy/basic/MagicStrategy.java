package io.server.game.world.entity.combat.strategy.basic;

import io.server.Config;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.pathfinding.path.SimplePathChecker;
import io.server.util.Utility;

/**
 * @author Michael | Chex
 */
public abstract class MagicStrategy<T extends Mob> extends CombatStrategy<T> {

	@Override
	public boolean withinDistance(T attacker, Mob defender) {
		FightType fightType = attacker.getCombat().getFightType();
		int distance = getAttackDistance(attacker, fightType);
		return Utility.within(attacker, defender, distance) && SimplePathChecker.checkProjectile(attacker, defender);
	}

	@Override
	public int modifyDamage(T attacker, Mob defender, int damage) {
		if (defender.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) 
			return (damage *= 0.6);
		return damage;
	}

	protected static void addCombatExperience(Player player, double base, Hit... hits) {
		int exp = 0;
		for (Hit hit : hits) {
			exp += hit.getDamage();
		}
		if(exp < 0) {
			System.err.println("Returning negative exp - adam");
			return;
		}
		exp *= Config.COMBAT_MODIFICATION;
		exp += base;
		player.skills.addExperience(Skill.MAGIC, exp);
		player.skills.addExperience(Skill.HITPOINTS, exp / 3);
	}

}
