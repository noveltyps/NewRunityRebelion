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
public class MagicShortbow extends PlayerRangedStrategy {

	private static final MagicShortbow INSTANCE = new MagicShortbow();
	private static final Animation ANIMATION = new Animation(1074, UpdatePriority.HIGH);
	private static Projectile PROJECTILE_1;
	private static Projectile PROJECTILE_2;

	static {
		try {
			setProjectiles(CombatProjectile.getDefinition("Magic Shortbow"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MagicShortbow() {
	}

	@Override
	protected void sendStuff(Player attacker, Mob defender) {
		attacker.animate(ANIMATION);
		PROJECTILE_1.send(attacker, defender);
		PROJECTILE_2.send(attacker, defender);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[] { nextRangedHit(attacker, defender), nextRangedHit(attacker, defender) };
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return roll - roll / 4;
	}

	private static void setProjectiles(CombatProjectile projectile) {
		if (!projectile.getProjectile().isPresent())
			throw new NullPointerException("No Magic Shortbow projectile found.");
		PROJECTILE_1 = projectile.getProjectile().get();
		PROJECTILE_2 = PROJECTILE_1.copy();
		PROJECTILE_2.setDelay(23 + PROJECTILE_1.getDelay());
		PROJECTILE_2.setDuration(23 + PROJECTILE_1.getDuration());
	}

	public static MagicShortbow get() {
		return INSTANCE;
	}

}