package io.server.game.world.entity.combat.attack.listener.npc;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.Utility;

/** @author Daniel */
@NpcCombatListenerSignature(npcs = { 3129 })
public class KrilTsutsaroth extends SimplifiedListener<Npc> {
	private static CombatStrategy<Npc>[] STRATEGIES;
	private static final String[] SHOUTS = { "Attack them, you dogs!", "Attack!", "YARRRRRRRR!",
			"Rend them limb from limb!", "Forward!", "No retreat!", };

	static {
		try {
			STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), new MagicAttack());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		attacker.setStrategy(randomStrategy(STRATEGIES));
		if (Utility.random(3) == 0) {
			attacker.speak(Utility.randomElement(SHOUTS));
		}
	}

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		attacker.getStrategy().block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);
	}

	private static class MagicAttack extends NpcMagicStrategy {
		public MagicAttack() {
			super(getDefinition("Kril Tsutsaroth"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 30);
			hit.setAccurate(true);
			return new CombatHit[] { hit };
		}
	}
}
