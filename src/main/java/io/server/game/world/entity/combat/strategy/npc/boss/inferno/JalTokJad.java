package io.server.game.world.entity.combat.strategy.npc.boss.inferno;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.task.impl.CeillingCollapseTask;
import io.server.game.world.World;
import io.server.game.world.entity.combat.CombatType;
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
import io.server.game.world.entity.mob.movement.waypoint.Waypoint;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.position.Position;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * @author Adam_#6723
 */
public class JalTokJad extends MultiStrategy {
	private static RangedAttack RANGED = new RangedAttack();
	private static MagicAttack MAGIC = new MagicAttack();
	private static MeleeAttack MELEE = new MeleeAttack();
	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(RANGED, MAGIC, MELEE);
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(RANGED, MAGIC);

	public JalTokJad() {
		currentStrategy = randomStrategy(NON_MELEE);
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(NON_MELEE);
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		currentStrategy.block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);
	}
 private boolean hasGuardians;
 //private static int OBJECT = 1;
 public static int POS1 = 2274;
 public static int POS2 = 5337;

	@Override
	public void hit(Npc attacker, Mob defender, Hit hit) {
		super.hit(attacker, defender, hit);

		if (!defender.isPlayer())
			return;
		Player player = defender.getPlayer();

		if (currentStrategy.getCombatType().equals(CombatType.MELEE)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
			hit.setDamage(0);
		} else if (currentStrategy.getCombatType().equals(CombatType.RANGED)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_RANGE)) {
			hit.setDamage(0);
		} else if (currentStrategy.getCombatType().equals(CombatType.MAGIC)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
			hit.setDamage(0);
		}
		
			if (hasGuardians || attacker.getCurrentHealth() >= 175) {
				return;
			}
			hasGuardians = true;
			
			for (int i = 0; i <= 2; i++) {
				System.out.println("VALUE OF I: " + i);
				Position spawn = RandomUtils.random(Utility.getInnerBoundaries(attacker));
				Npc guardian = new Guardian(spawn, attacker);
				guardian.register();
				guardian.definition.setRespawnTime(-1);
				guardian.walkExactlyTo(new Position(POS1, POS2, guardian.getHeight()), () -> {
					
					World.sendGraphic(new Graphic(1460, true), guardian.getPosition());
					defender.damage(new Hit(10 * guardian.getCurrentHealth() / guardian.getMaximumHealth()));
					World.schedule(new CeillingCollapseTask(defender.getPlayer()));
				});
			}
		}
	

	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {
		currentStrategy.finishOutgoing(attacker, defender);
		if (NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		} else {
			currentStrategy = randomStrategy(NON_MELEE);
		}
	}

	@Override
	public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
		return roll + 150_000;
	}
	
	

	private static class RangedAttack extends NpcRangedStrategy {

		private final Animation ANIMATION = new Animation(7593, UpdatePriority.HIGH);

		RangedAttack() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			attacker.animate(ANIMATION);
			World.sendGraphic(new Graphic(451, UpdatePriority.HIGH), defender.getPosition());
		}

		
		@Override
		public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
			return 11;
		}
		
		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 97, 2, 2) };
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}

	private static class MagicAttack extends NpcMagicStrategy {
		private final Animation ANIMATION = new Animation(7592, UpdatePriority.HIGH);

		public MagicAttack() {
			super(getDefinition("jal jad Magic"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 97, 5, 2);
			hit.setAccurate(true);
			return new CombatHit[] { hit };
		}
		
		@Override
		public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
			return 11;
		}

		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}
	
	private class Guardian extends Npc {
		private final StopWatch lastHeal = new StopWatch();

		private Guardian(Position spawn, Npc healer) {
			super(7705, spawn);
			lastHeal.start();
			setWaypoint(new Waypoint(this, healer) {
				@Override
				protected void onDestination() {
					CombatProjectile.getDefinition("Scorpia guardian").getProjectile()
							.ifPresent(projectile -> projectile.send(mob, healer));
					lastHeal.reset();
					lastHeal.start();
					mob.animate(new Animation(2637));
					healer.heal(4);
				}
			});
		}

		@Override
		public void sequence() {
			super.sequence();
			long millis = TimeUnit.MILLISECONDS.convert(lastHeal.getNanoTime(), TimeUnit.NANOSECONDS);
			if (millis > 15_000) {
				unregister();
			}
		}
	}
	
	private static class MeleeAttack extends NpcMeleeStrategy {
		private final Animation ANIMATION = new Animation(7590, UpdatePriority.HIGH);


		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMeleeHit(attacker, defender, 97, 5, 2);
			hit.setAccurate(true);
			return new CombatHit[] { hit };
		}

		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}
	
}
