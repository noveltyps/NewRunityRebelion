package io.server.game.world.entity.combat.strategy.npc.boss.kril;

import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

public class Tstanon extends MultiStrategy {

	public Tstanon() {
		currentStrategy = new Melee();
	}

	private class Melee extends NpcMeleeStrategy {
		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMeleeHit(attacker, defender, 15) };
		}
	}

}
