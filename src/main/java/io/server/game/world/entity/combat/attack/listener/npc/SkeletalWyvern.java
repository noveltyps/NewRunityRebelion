package io.server.game.world.entity.combat.attack.listener.npc;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.combat.strategy.npc.impl.DragonfireStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.Utility;

/** @author Adam */
@NpcCombatListenerSignature(npcs = { 465 })
public class SkeletalWyvern extends SimplifiedListener<Npc> {
	private static DragonfireStrategy DRAGONFIRE;
	private static CombatStrategy<Npc>[] STRATEGIES;

	static {
		try {
			DRAGONFIRE = new DragonfireStrategy(getDefinition("Skeletal wyvern breathe"));
			STRATEGIES = createStrategyArray(new Melee(), DRAGONFIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(DRAGONFIRE);
		}
		return attacker.getStrategy().canAttack(attacker, defender);
	}

	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {

		int standardAttack = 1;
		int standardAttackRandom = Utility.random(standardAttack, 5);

		if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(DRAGONFIRE);
		} else {
			attacker.setStrategy(randomStrategy(STRATEGIES));
		}
		if (standardAttackRandom == 1) {
			attacker.animate(2989);
		}
	}

	private static final class Melee extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(2988, UpdatePriority.HIGH);
		//private static final Animation ANIMATION_1 = new Animation(2989, UpdatePriority.HIGH);

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 2;
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			attacker.animate(ANIMATION);
			return ANIMATION;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMeleeHit(attacker, defender) };
		}
	}
}
