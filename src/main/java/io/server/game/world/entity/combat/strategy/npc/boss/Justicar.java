package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.world.World;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.position.Position;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * Handles JUSTICAR Combat Strategy Justicar
 *
 * @author Adam_#6723
 */
public class Justicar extends MultiStrategy {
	private static Magic MAGIC = new Magic();
	private static Melee MELEE = new Melee();
	private static TeleGrab TELE_GRAB = new TeleGrab();

	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC,
			TELE_GRAB);
	private static final CombatStrategy<Npc>[] MAGIC_STRATEGIES = createStrategyArray(MAGIC, MAGIC, MAGIC, TELE_GRAB);
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(MAGIC, MELEE, MELEE, MAGIC, MAGIC,
			TELE_GRAB);

	/** Constructs a new <code>Justiciar</code>. */
	public Justicar() {
		currentStrategy = MAGIC;
		currentStrategy = MELEE;
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
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		currentStrategy.block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);

		if (!defender.getCombat().isAttacking()) {
			defender.animate(new Animation(7853, UpdatePriority.VERY_HIGH));
			defender.graphic(1196);
			defender.graphic(481);
			defender.speak("Night King, Lend me your powers for i am your faithful servant!");
			CombatUtil.areaAction(attacker, 64, 18, mob -> {
				if (RandomUtils.success(.65))
					return;

				World.schedule(2, () -> {
					Position destination = Utility.randomElement(defender.boundaries);
					World.sendGraphic(new Graphic(481), destination);

				});
			});
		}
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

	private static class Melee extends NpcRangedStrategy {

		public Melee() {
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

			attacker.animate(new Animation(7962, UpdatePriority.VERY_HIGH));
			Projectile projectile = new Projectile(162, 50, 80, 85, 25);
			//CombatHit hit = nextMeleeHit(attacker, defender, 21);
			defender.graphic(163);
			CombatUtil.areaAction(attacker, 64, 18, mob -> {
				projectile.send(attacker, defender);
				mob.damage(nextMagicHit(attacker, defender, 35));
			});

			/*
			 * if (Utility.random(0, 8) == 1) { attacker.animate(new Animation(7965,
			 * UpdatePriority.VERY_HIGH)); attacker.graphic(new Graphic(1296,
			 * UpdatePriority.VERY_HIGH)); attacker.heal(50);
			 * attacker.speak("Time To HEAL!"); System.out.println("It executes!");
			 * 
			 * }
			 */

		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextRangedHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}

		@Override
		public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
			return roll + 50_000;
		}

	}

	/** Jisticiar magic strategy. */
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
			Projectile projectile = new Projectile(393, 50, 80, 85, 25);
			attacker.animate(new Animation(7853, UpdatePriority.VERY_HIGH));
			CombatUtil.areaAction(attacker, 64, 18, mob -> {
				projectile.send(attacker, defender);
				defender.graphic(157);
				mob.damage(nextMagicHit(attacker, defender, 38));
			});
			
			if (Utility.random(0, 10) == 1) {
				attacker.animate(new Animation(7965, UpdatePriority.VERY_HIGH));
				attacker.graphic(new Graphic(1296, UpdatePriority.VERY_HIGH));
				attacker.heal(130);
				attacker.speak("Time To HEAL!");

			}

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

				attacker.animate(new Animation(7964, UpdatePriority.VERY_HIGH));
				Projectile projectile = new Projectile(1479, 50, 80, 85, 25);
				projectile.send(attacker, defender);
				defender.damage(new Hit(Utility.random(20, 50)));
				attacker.speak("I AM A DECENDENT OF HELL!");
			

	}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}
	}
}
