package io.server.game.world.entity.combat.attack.listener.npc;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = { 1672 })
public class Ahrim extends SimplifiedListener<Npc> {

	private static FireBlast FIRE_BLAST = new FireBlast();
	private static Confuse CONFUSE = new Confuse();
	private static Weaken WEAKEN = new Weaken();
	private static Curse CURSE = new Curse();
	private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(FIRE_BLAST, FIRE_BLAST, FIRE_BLAST,
			CONFUSE, WEAKEN, CURSE);

	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {
		attacker.setStrategy(randomStrategy(STRATEGIES));
	}

	private static class FireBlast extends NpcMagicStrategy {
		private FireBlast() {
			super(getDefinition("Fire Blast"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 25);
			hit.setAccurate(true);
			return new CombatHit[] { hit };
		}
	}

	private static class Confuse extends NpcMagicStrategy {
		private Confuse() {
			super(getDefinition("Confuse"));
		}
	}

	private static class Weaken extends NpcMagicStrategy {
		private Weaken() {
			super(getDefinition("Weaken"));
		}
	}

	private static class Curse extends NpcMagicStrategy {
		private Curse() {
			super(getDefinition("Curse"));
		}
	}
}
