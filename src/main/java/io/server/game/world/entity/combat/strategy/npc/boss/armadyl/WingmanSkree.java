package io.server.game.world.entity.combat.strategy.npc.boss.armadyl;

import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

public class WingmanSkree extends MultiStrategy {

	public WingmanSkree() {
		currentStrategy = new Magic();
	}

	/*	@Override
	public boolean canOtherAttack(Mob attacker, Npc defender) {
		if (attacker.isPlayer() && attacker.getStrategy().getCombatType().equals(CombatType.MELEE)) {
			attacker.getPlayer().message("You can't attack Armadyl with melee!");
			return false;
		}
		return super.canOtherAttack(attacker, defender);
	}*/

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	private static class Magic extends NpcMagicStrategy {
		public Magic() {
			super(CombatProjectile.getDefinition("Wingman Skree"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, 16) };
		}
	}

}
