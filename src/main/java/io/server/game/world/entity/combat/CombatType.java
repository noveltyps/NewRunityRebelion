package io.server.game.world.entity.combat;

import io.server.game.world.entity.combat.formula.MagicFormula;
import io.server.game.world.entity.combat.formula.MeleeFormula;
import io.server.game.world.entity.combat.formula.RangedFormula;
import io.server.game.world.entity.mob.Mob;

public enum CombatType {
	
	MELEE(new MeleeFormula()), 
	
	RANGED(new RangedFormula()), 
	
	MAGIC(new MagicFormula());

	public FormulaModifier<Mob> formula;

	CombatType(FormulaModifier<Mob> formula) {
		this.formula = formula;
	}
	
	public FormulaModifier<Mob> getFormula() {//im retarded..
		return formula;
	}
}
