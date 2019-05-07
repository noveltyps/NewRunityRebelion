package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;

import io.server.game.Animation;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.Utility;

/**
 * Handles Giant Roc's strategy
 *
 * @author Adam
 */
public class GiantRoc extends MultiStrategy {
	private static Magic MAGIC = new Magic();
	private static TeleGrab TELE_GRAB = new TeleGrab();

	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC,
			TELE_GRAB);
	private static final CombatStrategy<Npc>[] MAGIC_STRATEGIES = createStrategyArray(MAGIC, MAGIC, MAGIC, TELE_GRAB);
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(MAGIC, MAGIC, MAGIC, MAGIC,
			TELE_GRAB);

	private static final String[] SHOUTS = { "The Cold Winds are Rising!", "Darkness and death marches upon gilenor!" };

	/** Constructs a new <code>Giant Roc</code>. */
	public GiantRoc() {
		currentStrategy = MAGIC;
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(MAGIC_STRATEGIES);
			currentStrategy = randomStrategy(NON_MELEE);
		}
		return currentStrategy.canAttack(attacker, defender);
	}


	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {
		currentStrategy.finishOutgoing(attacker, defender);
		if (NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		} else {
			currentStrategy = randomStrategy(MAGIC_STRATEGIES);
		}
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	/** Skotizo's magic strategy. */
	private static class Magic extends NpcMagicStrategy {
		public Magic() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			Projectile projectile = new Projectile(1198, 50, 80, 85, 25);
			attacker.animate(new Animation(5023, UpdatePriority.VERY_HIGH));
			CombatUtil.areaAction(attacker, 64, 18, mob -> {
				if (Utility.random(0, 3) == 2)
					attacker.speak(Utility.randomElement(SHOUTS));
				projectile.send(attacker, defender);
				mob.damage(nextMagicHit(attacker, defender, 35));
			});
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}

		@Override
		public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
			return roll + 50_000;
		}
	}

	private static class TeleGrab extends NpcMagicStrategy {
		TeleGrab() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			Projectile projectile = new Projectile(1198, 50, 80, 85, 25);
			attacker.animate(new Animation(5023, UpdatePriority.VERY_HIGH));
			CombatUtil.areaAction(attacker, 64, 18, mob -> {
				if (Utility.random(0, 3) == 2)
					attacker.speak(Utility.randomElement(SHOUTS));
				projectile.send(attacker, defender);
				mob.damage(nextMagicHit(attacker, defender, 35));
			});
		}
		

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}
	}
}
