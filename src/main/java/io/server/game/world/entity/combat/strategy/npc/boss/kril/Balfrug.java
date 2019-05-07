package io.server.game.world.entity.combat.strategy.npc.boss.kril;

import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

public class Balfrug extends MultiStrategy {

	public Balfrug() {
		currentStrategy = new Magic();
	}

	private class Magic extends NpcMagicStrategy {
		public Magic() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, 16) };
		}
	}

}
