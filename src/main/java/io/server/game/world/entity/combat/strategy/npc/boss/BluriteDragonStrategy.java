package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.combat.strategy.npc.impl.DragonfireStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

/** @author Meradj | Adam */
public class BluriteDragonStrategy extends MultiStrategy {
	private static final StabMelee STAB = new StabMelee();
	private static final CrushMelee CRUSH = new CrushMelee();
	private static final Dragonfire DRAGONFIRE = new Dragonfire();
	private static final Poison POISON = new Poison();
	private static final Freeze FREEZE = new Freeze();
	private static final Shock SHOCK = new Shock();

	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(CRUSH, STAB, DRAGONFIRE, POISON,
			FREEZE, SHOCK);
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(DRAGONFIRE, CRUSH, STAB, POISON, FREEZE,
			SHOCK);

	public BluriteDragonStrategy() {
		currentStrategy = randomStrategy(NON_MELEE);
	}

	@Override
	public boolean withinDistance(Npc attacker, Mob defender) {
		if (!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(NON_MELEE);
		}
		return currentStrategy.withinDistance(attacker, defender);
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.canAttack(attacker, defender)) {
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
	public void finishOutgoing(Npc attacker, Mob defender) {
		currentStrategy.finishOutgoing(attacker, defender);
		if (STAB.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		} else {
			currentStrategy = randomStrategy(NON_MELEE);
		}
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	private static final class CrushMelee extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(80, UpdatePriority.HIGH);

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 9;
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMeleeHit(attacker, defender) };
		}
	}

	private static final class StabMelee extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(91, UpdatePriority.HIGH);

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMeleeHit(attacker, defender) };
		}
	}

	private static final class Dragonfire extends DragonfireStrategy {

		Dragonfire() {
			super(getDefinition("KBD fire"));
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 2;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 65, true) };
		}
	}

	private static final class Freeze extends DragonfireStrategy {

		Freeze() {
			super(getDefinition("KBD freeze"));
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 3;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 65, true) };
		}
	}

	private static final class Shock extends DragonfireStrategy {

		Shock() {
			super(getDefinition("KBD shock"));
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 2;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 65, true) };
		}
	}

	private static final class Poison extends DragonfireStrategy {

		Poison() {
			super(getDefinition("KBD poison"));
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 2;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 65, true) };
		}
	}

}
