package io.server.game.world.entity.combat.strategy.player.special.range;

import io.server.game.Animation;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * Handles the magic shortbow weapon special attack.
 *
 * @author Daniel
 * @author Michaael | Chex
 */
public class DragonThrownaxe extends PlayerRangedStrategy {
	private static final DragonThrownaxe INSTANCE = new DragonThrownaxe();
	private static final Animation ANIMATION = new Animation(4230, UpdatePriority.HIGH);
	private static Projectile PROJECTILE;

	static {
		try {
			setProjectiles(CombatProjectile.getDefinition("Dragon thrownaxe"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DragonThrownaxe() {
	}
	
	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		super.attack(attacker, defender, hit);
		
    if(Utility.random(1, 3) == 1){
		if (hit.getDamage() == 0) {
			hit.setDamage(RandomUtils.inclusive(0, 20));
		     	}
		}
    attacker.animate(ANIMATION);
	PROJECTILE.send(attacker, defender);
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
		return roll * 6 / 5;
	}

	private static void setProjectiles(CombatProjectile projectile) {
		if (!projectile.getProjectile().isPresent())
			throw new NullPointerException("No Dragon Thrownaxe projectile found.");
		PROJECTILE = projectile.getProjectile().get();
	}

	public static DragonThrownaxe get() {
		return INSTANCE;
	}

}