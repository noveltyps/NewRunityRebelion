package io.server.game.world.entity.combat.attack;

import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.FormulaModifier;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.HitIcon;
import io.server.game.world.entity.combat.hit.Hitsplat;
import io.server.game.world.entity.mob.Mob;
import io.server.util.RandomUtils;

/**
 * Supplies factory methods useful for combat.
 *
 * @author Michael | Chex Chex Updated this class.
 */
public final class FormulaFactory {

	public static Hit nextMeleeHit(Mob attacker, Mob defender) {
		int max = getMaxHit(attacker, defender, CombatType.MELEE);
		return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MELEE);
	}

	public static Hit nextMeleeHit(Mob attacker, Mob defender, int max) {
		return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MELEE);
	}

	public static Hit nextRangedHit(Mob attacker, Mob defender, int max) {
		return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.RANGED);
	}

	public static Hit nextMagicHit(Mob attacker, Mob defender, int max) {
		return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MAGIC);
	}

	private static Hit nextHit(Mob attacker, Mob defender, int max, Hitsplat hitsplat, HitIcon icon) {
		Hit hit = new Hit(max < 0 ? -1 : 0, hitsplat, icon, false);

		/*
		 * Use attacker's strategy for combat type since formulas are dependent on the
		 * main combat type of the attack. This allows for melee-based magic attacks,
		 * ranged-based melee attacks, etc.
		 */
		CombatType type = attacker.getStrategy().getCombatType();
		
		int baseDamage = max;
		
		if (isAccurate(attacker, defender, type)) {
			
			max = attacker.getStrategy().modifyDamage(attacker, defender, baseDamage);
			
			if (max > 0) {
							
				int verdict = RandomUtils.inclusive(0, max);
				
				if (verdict > defender.getCurrentHealth()) 
					verdict = defender.getCurrentHealth();

				hit.setDamage(verdict);
			}
			hit.setAccurate(true);
		}

		return hit;
	}

	private static boolean isAccurate(Mob attacker, Mob defender, CombatType type) {
		double attackRoll = rollOffensive(attacker, defender, type.getFormula());
		double defenceRoll = rollDefensive(attacker, defender, type.getFormula()); 
		//System.out.println("Chance... "+attackRoll+" - "+defenceRoll+" Sucessful="+RandomUtils.success(attackRoll / (attackRoll + defenceRoll)));
		return RandomUtils.success(attackRoll / (attackRoll + defenceRoll));
	}

	public static int rollOffensive(Mob attacker, Mob defender, FormulaModifier<Mob> formula) {
		int roll = formula.modifyAccuracy(attacker, defender, 0);
		int bonus = formula.modifyOffensiveBonus(attacker, defender, 0);
		return attacker.getCombat().modifyAccuracy(defender, roll * (bonus + 64));
	}

	public static int rollDefensive(Mob attacker, Mob defender, FormulaModifier<Mob> formula) {
		int roll = formula.modifyDefensive(attacker, defender, 0);
		int bonus = formula.modifyDefensiveBonus(attacker, defender, 0);
		return attacker.getCombat().modifyDefensive(defender, roll * (bonus + 64));
	}

	public static int getMaxHit(Mob attacker, Mob defender, CombatType type) {
		FormulaModifier<Mob> formula = type.getFormula();
		int level = formula.modifyAggressive(attacker, defender, 0);
		int bonus = formula.modifyAggressiveBonus(attacker, defender, 0);
		return maxHit(level, bonus);
	}

	public static int getModifiedMaxHit(Mob attacker, Mob defender, CombatType type) {
		FormulaModifier<Mob> formula = type.getFormula();
		int level = formula.modifyAggressive(attacker, defender, 0);
		int bonus = formula.modifyAggressiveBonus(attacker, defender, 0);
		return formula.modifyDamage(attacker, defender, maxHit(level, bonus));
	}

	private static int maxHit(int level, int bonus) {
		return (320 + level * (bonus + 64)) / 640;
	}

}
