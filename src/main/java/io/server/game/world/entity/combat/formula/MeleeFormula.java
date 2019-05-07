package io.server.game.world.entity.combat.formula;

import io.server.game.world.entity.combat.FormulaModifier;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.containers.equipment.Equipment;

/**
 * 
 * @edited Adam_#6723
 */
public final class MeleeFormula implements FormulaModifier<Mob> {

	@Override
	public int modifyAccuracy(Mob attacker, Mob defender, int roll) {
		FightType fightType = attacker.getCombat().getFightType();
		int level = attacker.skills.getLevel(Skill.ATTACK);
		int effectiveAccuracy = attacker.getCombat().modifyAttackLevel(defender, level);
		return 10 + effectiveAccuracy + fightType.getStyle().getAccuracyIncrease();
	}
	
	@Override
	public int modifyDamage(Mob attacker, Mob defender, int damage) {
		return damage;
	}
	

	@Override
	public int modifyAggressive(Mob attacker, Mob defender, int roll) {
		FightType fightType = attacker.getCombat().getFightType();
		int level = attacker.skills.getLevel(Skill.STRENGTH);
		int effectiveStrength = attacker.getCombat().modifyStrengthLevel(defender, level);
		return 10 + effectiveStrength + fightType.getStyle().getStrengthIncrease();
	}

	@Override
	public int modifyDefensive(Mob attacker, Mob defender, int roll) {
		FightType fightType = defender.getCombat().getFightType();
		int level = defender.skills.getLevel(Skill.DEFENCE);
		int effectiveDefence = defender.getCombat().modifyDefenceLevel(attacker, level);
		return 10 + effectiveDefence + fightType.getStyle().getDefensiveIncrease();
	}

	@Override
	public int modifyOffensiveBonus(Mob attacker, Mob defender, int bonus) {
		FightType fightType = attacker.getCombat().getFightType();
		bonus = attacker.getBonus(fightType.getBonus());
		return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
	}

	@Override
	public int modifyAggressiveBonus(Mob attacker, Mob defender, int bonus) {
		bonus = attacker.getBonus(Equipment.STRENGTH_BONUS);
		return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
	}

	@Override
	public int modifyDefensiveBonus(Mob attacker, Mob defender, int bonus) {
		FightType fightType = attacker.getCombat().getFightType();
		bonus = defender.getBonus(fightType.getCorrespondingBonus());
		return defender.getCombat().modifyDefensiveBonus(attacker, bonus);
	}

}
