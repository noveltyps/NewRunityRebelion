package io.server.game.world.entity.combat.strategy.player.special.range;

import io.server.game.Animation;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

/**
 * Handles the magic shortbow weapon special attack.
 *
 * @author Daniel
 * @author Michaael | Chex
 */
public class ArmadylCrossbow extends PlayerRangedStrategy {

	private static final ArmadylCrossbow INSTANCE = new ArmadylCrossbow();
	private static final Animation ANIMATION = new Animation(4230, UpdatePriority.HIGH);
	private static Projectile PROJECTILE;

	static {
		try {
			setProjectiles(CombatProjectile.getDefinition("Armadyl special"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArmadylCrossbow() {
	}

	@Override
	protected void sendStuff(Player attacker, Mob defender) {
		attacker.animate(ANIMATION);
		PROJECTILE.send(attacker, defender);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[] { nextRangedHit(attacker, defender) };
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return 2 * roll;
	}

	private static void setProjectiles(CombatProjectile projectile) {
		if (!projectile.getProjectile().isPresent())
			throw new NullPointerException("No Magic Shortbow projectile found.");
		PROJECTILE = projectile.getProjectile().get();
	}

	public static ArmadylCrossbow get() {
		return INSTANCE;
	}

}