package io.server.game.world.entity.combat.formula;

import io.server.game.world.entity.combat.FormulaModifier;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.containers.equipment.Equipment;

public final class RangedFormula implements FormulaModifier<Mob> {

	@Override
	public int modifyAccuracy(Mob attacker, Mob defender, int roll) {
		FightType fightType = attacker.getCombat().getFightType();
		int level = attacker.skills.getLevel(Skill.RANGED);
		int effectiveAccuracy = attacker.getCombat().modifyRangedLevel(defender, level);
		return 14 + effectiveAccuracy + fightType.getStyle().getAccuracyIncrease();
	}

	@Override
	public int modifyAggressive(Mob attacker, Mob defender, int roll) {
		int level = attacker.skills.getLevel(Skill.RANGED);
		return 13 + attacker.getCombat().modifyRangedLevel(defender, level);
	}

	@Override
	public int modifyDefensive(Mob attacker, Mob defender, int roll) {
		FightType fightType = defender.getCombat().getFightType();
		int level = defender.skills.getLevel(Skill.DEFENCE);
		int effectiveDefence = defender.getCombat().modifyDefenceLevel(attacker, level);
		return 13 + effectiveDefence + fightType.getStyle().getDefensiveIncrease();
	}

	@Override
	public int modifyOffensiveBonus(Mob attacker, Mob defender, int bonus) {
		bonus = attacker.getBonus(Equipment.RANGED_OFFENSE);
		return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
	}

	@Override
	public int modifyAggressiveBonus(Mob attacker, Mob defender, int bonus) {
		bonus = attacker.getBonus(Equipment.RANGED_STRENGTH);
		return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
	}

	@Override
	public int modifyDefensiveBonus(Mob attacker, Mob defender, int bonus) {
		bonus = defender.getBonus(Equipment.RANGED_DEFENSE);
		return defender.getCombat().modifyDefensiveBonus(attacker, bonus);
	}

}
