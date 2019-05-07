package io.server.game.world.entity.combat.formula;

import io.server.game.world.entity.combat.FormulaModifier;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.containers.equipment.Equipment;

public final class MagicFormula implements FormulaModifier<Mob> {

	@Override
	public int modifyAccuracy(Mob attacker, Mob defender, int roll) {
		int level = attacker.skills.getLevel(Skill.MAGIC);
		return 17 + attacker.getCombat().modifyMagicLevel(defender, level);
	}

	@Override
	public int modifyAggressive(Mob attacker, Mob defender, int roll) {
		int level = attacker.skills.getLevel(Skill.MAGIC);
		return 12 + attacker.getCombat().modifyMagicLevel(defender, level);
	}

	@Override
	public int modifyDefensive(Mob attacker, Mob defender, int roll) {
		/** Needs debug and redo.. **/
		FightType fightType = defender.getCombat().getFightType();

		int magic = defender.skills.getLevel(Skill.MAGIC);
		magic = defender.getCombat().modifyMagicLevel(attacker, magic);

		int defence = defender.skills.getLevel(Skill.DEFENCE);
		defence = defender.getCombat().modifyDefenceLevel(attacker, defence);

		int effectiveLevel = 11 + fightType.getStyle().getDefensiveIncrease();
		effectiveLevel += 0.25 * magic + 0.20 * defence;

		return effectiveLevel;
	}

	@Override
	public int modifyDamage(Mob attacker, Mob defender, int damage) {
		int bonus = attacker.getBonus(Equipment.MAGIC_STRENGTH);
		return damage + damage * bonus / 100;
	}

	@Override
	public int modifyOffensiveBonus(Mob attacker, Mob defender, int bonus) {
		bonus = attacker.getBonus(Equipment.MAGIC_OFFENSE);
		return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
	}

	@Override
	public int modifyAggressiveBonus(Mob attacker, Mob defender, int bonus) {
		return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
	}

	@Override
	public int modifyDefensiveBonus(Mob attacker, Mob defender, int bonus) {
		bonus = attacker.getBonus(Equipment.MAGIC_DEFENSE);
		return attacker.getCombat().modifyDefensiveBonus(defender, bonus);
	}

}
