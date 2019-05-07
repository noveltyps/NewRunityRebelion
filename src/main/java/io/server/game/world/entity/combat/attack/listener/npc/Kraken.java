package io.server.game.world.entity.combat.attack.listener.npc;

import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = { 494 })
public class Kraken extends SimplifiedListener<Npc> {

	private static MagicAttack MAGIC = new MagicAttack();

	@Override
	public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
		return roll + 25000;
	}

	@Override
	public int modifyAggressive(Npc attacker, Mob defender, int roll) {
		return roll + 15000;
	}

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		attacker.setStrategy(MAGIC);
	}

	private static class MagicAttack extends NpcMagicStrategy {
		private MagicAttack() {
			super(getDefinition("Kraken Blast"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit combatHit = nextMagicHit(attacker, defender, combatProjectile.getMaxHit());
			combatHit.setAccurate(true);
			return new CombatHit[] { combatHit };
		}
	}
}
