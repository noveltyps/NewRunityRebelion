package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.World;
import io.server.game.world.entity.combat.CombatType;
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
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;

/**
 * @author Adam_#6723
 */

public class TzTokJad extends MultiStrategy {
	
	private static RangedAttack RANGED = new RangedAttack();
	private static MagicAttack MAGIC = new MagicAttack();
	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(RANGED, MAGIC,
			NpcMeleeStrategy.get());
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(RANGED, MAGIC);

	public TzTokJad() {
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
		
		private final Animation ANIMATION = new Animation(2652, UpdatePriority.HIGH);

		RangedAttack() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			attacker.animate(ANIMATION);
			World.sendGraphic(new Graphic(451, UpdatePriority.HIGH), defender.getPosition());
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 97, 5, 5) };
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}

	private static class MagicAttack extends NpcMagicStrategy {
		private final Animation ANIMATION = new Animation(2656, UpdatePriority.HIGH);

		public MagicAttack() {
			super(getDefinition("Jad Magic"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 97, 5, 5);
			hit.setAccurate(true);
			return new CombatHit[] { hit };
		}

		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}
}
