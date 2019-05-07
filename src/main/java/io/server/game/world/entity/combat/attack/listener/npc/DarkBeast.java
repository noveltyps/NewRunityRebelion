package io.server.game.world.entity.combat.attack.listener.npc;

import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.RandomUtils;

@NpcCombatListenerSignature(npcs = { 4005 })
public class DarkBeast extends SimplifiedListener<Npc> {
	private static MagicAttack MAGIC = new MagicAttack();

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		if (RandomUtils.success(.85)) {
			attacker.setStrategy(NpcMeleeStrategy.get());
		} else {
			attacker.setStrategy(MAGIC);
		}
	}

	private static class MagicAttack extends NpcMagicStrategy {
		private MagicAttack() {
			super(getDefinition("Fire Bolt"));
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			attacker.animate(new Animation(2730, UpdatePriority.VERY_HIGH));
			combatProjectile.sendProjectile(attacker, defender);

		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 8, 2, 1);
			hit.setAccurate(true);
			return new CombatHit[] { hit };
		}
	}
}
