package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.getHitDelay;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.task.TickableTask;
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
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.data.LockType;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.entity.skill.Skill;
import io.server.util.RandomUtils;

/** @author Daniel */
public class Venenatis extends MultiStrategy {
	private static final PrayerDrain PRAYER_DRAIN = new PrayerDrain();
	private static final Magic MAGIC = new Magic();
	private static final Web WEB = new Web();

	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(),
			NpcMeleeStrategy.get(), MAGIC, MAGIC, PRAYER_DRAIN, WEB);
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(MAGIC, MAGIC, PRAYER_DRAIN, WEB);

	public Venenatis() {
		currentStrategy = MAGIC;
	}

	@Override
	public boolean withinDistance(Npc attacker, Mob defender) {
		if (currentStrategy == NpcMeleeStrategy.get() && !currentStrategy.withinDistance(attacker, defender)
				&& !defender.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
			currentStrategy = randomStrategy(NON_MELEE);
		}
		return currentStrategy.withinDistance(attacker, defender);
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.canAttack(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
//        if (hit.isAccurate() && !Area.inMulti(defender)) { TODO: Gotta make the area multi first
//            hit.modifyDamage(damage -> RandomUtils.inclusive(1, 6));
//        }

		currentStrategy.block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);
	}

	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {
		CombatStrategy<Npc> strategy = currentStrategy;
		currentStrategy.finishOutgoing(attacker, defender);

		if (strategy != currentStrategy)
			return;

		if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(NON_MELEE);
		} else {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		}
	}

	private static final class Magic extends NpcMagicStrategy {
		private static final Animation ANIMATION = new Animation(5322, UpdatePriority.HIGH);

		Magic() {
			super(CombatProjectile.getDefinition("Earth Wave"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender) };
		}
	}

	private static final class Web extends NpcMagicStrategy {
		private static final Animation ANIMATION = new Animation(5322, UpdatePriority.HIGH);
		private static final Graphic GRAPHIC = new Graphic(80, true, UpdatePriority.HIGH);

		Web() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public boolean canAttack(Npc attacker, Mob defender) {
			return !defender.locking.locked() && super.canAttack(attacker, defender);
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
			super.hit(attacker, defender, hit);
			if (!hit.isAccurate())
				return;
			if (defender.isPlayer())
				defender.getPlayer().message("Venenatis hurls her web at you, sticking you to the ground.");
			defender.graphic(GRAPHIC);
			defender.locking.lock(5, LockType.WALKING);
		}

		@Override
		public void finishOutgoing(Npc attacker, Mob defender) {
			attacker.getCombat().setCooldown(0);
			((Venenatis) attacker.getStrategy()).currentStrategy = RandomUtils.randomExclude(FULL_STRATEGIES, WEB);
		}

		@Override
		public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
			return 0;
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, 50) };
		}
	}

	private static final class PrayerDrain extends NpcMagicStrategy {
		PrayerDrain() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
			if (!hit.isAccurate())
				return;
			World.schedule(new TickableTask(true, 1) {
				@Override
				protected void tick() {
					defender.graphic(new Graphic(172, true, UpdatePriority.HIGH));
					Skill prayer = defender.skills.get(Skill.PRAYER);
					prayer.modifyLevel(level -> level * 2 / 3);
					defender.skills.refresh(Skill.PRAYER);

					if (defender.isPlayer())
						defender.getPlayer().message("Your prayer has been drained!");

					if (tick == ((CombatHit) hit).getHitsplatDelay() - 1)
						cancel();
				}
			});
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			int hitDelay = getHitDelay(attacker, defender, CombatType.MAGIC);
			return new CombatHit[] { nextMagicHit(attacker, defender, 50, hitDelay, RandomUtils.inclusive(1, 3)) };
		}
	}

}
