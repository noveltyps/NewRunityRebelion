package io.server.game.world.entity.combat.strategy.npc.boss.scorpia;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import io.server.game.Animation;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.movement.waypoint.Waypoint;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.position.Position;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/** @author Daniel */
public class Scorpia extends MultiStrategy {
	private boolean hasGuardians;

	public Scorpia() {
		currentStrategy = new Melee();
	}

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		currentStrategy.block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);
	}

	@Override
	public void hit(Npc attacker, Mob defender, Hit hit) {
		super.hit(attacker, defender, hit);
		if (hasGuardians || attacker.getCurrentHealth() >= 100) {
			return;
		}
		hasGuardians = true;
		for (int i = 0; i < 2; i++) {
			Position spawn = RandomUtils.random(Utility.getInnerBoundaries(attacker));
			Npc guardian = new Guardian(spawn, attacker);
			guardian.register();
			guardian.definition.setRespawnTime(-1);
		}
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}
/*
	private class GuardianWaypoint extends Waypoint {
		final Npc scorpia;

		GuardianWaypoint(Guardian guardian, Npc scorpia) {
			super(guardian, scorpia);
			this.scorpia = scorpia;
		}

		@Override
		protected void onDestination() {
			CombatProjectile.getDefinition("Scorpia guardian").getProjectile()
					.ifPresent(projectile -> projectile.send(mob, scorpia));
			mob.animate(new Animation(6261));
			scorpia.heal(2);
			((Guardian) mob).lastHeal.reset();
		}
	}*/

	private class Guardian extends Npc {
		private final StopWatch lastHeal = new StopWatch();

		private Guardian(Position spawn, Npc scorpia) {
			super(6617, spawn);
			lastHeal.start();
			setWaypoint(new Waypoint(this, scorpia) {
				@Override
				protected void onDestination() {
					CombatProjectile.getDefinition("Scorpia guardian").getProjectile()
							.ifPresent(projectile -> projectile.send(mob, scorpia));
					lastHeal.reset();
					lastHeal.start();
					mob.animate(new Animation(6261));
					scorpia.heal(2);
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

	private static final class Melee extends NpcMeleeStrategy {
		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMeleeHit(attacker, defender, 16) };
		}
	}
}
