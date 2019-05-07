package io.server.game.world.entity.combat.strategy.npc.boss.dagannoths;

import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

/** @author Michael | Chex */
public class DagannothPrime extends MultiStrategy {

	public DagannothPrime() {
		currentStrategy = new WaterWave();
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	private static class WaterWave extends NpcMagicStrategy {
		private WaterWave() {
			super(getDefinition("Water Wave"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, 50) };
		}
	}

}
