package io.server.game.world.entity.combat.attack.listener.npc;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = { 3125 })
public class KetZek extends SimplifiedListener<Npc> {

	private static MagicAttack MAGIC;
	private static CombatStrategy<Npc>[] STRATEGIES;

	static {
		try {
			MAGIC = new MagicAttack();
			STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(MAGIC);
		}
		return attacker.getStrategy().canAttack(attacker, defender);
	}

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(MAGIC);
		} else {
			attacker.setStrategy(randomStrategy(STRATEGIES));
		}
	}

	private static class MagicAttack extends NpcMagicStrategy {
		private MagicAttack() {
			super(getDefinition("Fire Blast"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(2647, UpdatePriority.HIGH);
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit combatHit = nextMagicHit(attacker, defender, combatProjectile.getMaxHit());
			combatHit.setAccurate(true);
			return new CombatHit[] { combatHit };
		}
	}
}
