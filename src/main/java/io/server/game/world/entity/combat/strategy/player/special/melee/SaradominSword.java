package io.server.game.world.entity.combat.strategy.player.special.melee;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

/**
 * Handles the saradomin sword weapon special attack.
 *
 * @author Daniel
 */
public class SaradominSword extends PlayerMeleeStrategy {

	private static final Animation ANIMATION = new Animation(1132, UpdatePriority.HIGH);

	private static final Graphic OTHER_GRAPHIC = new Graphic(1196);
	private static final Graphic GRAPHIC = new Graphic(1213);

	private static final SaradominSword INSTANCE = new SaradominSword();

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		super.attack(attacker, defender, hit);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void finishOutgoing(Player attacker, Mob defender) {
		defender.graphic(OTHER_GRAPHIC);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		CombatHit melee = nextMeleeHit(attacker, defender);
		return new CombatHit[] { melee, nextMagicHit(attacker, defender, 16, 1, 0) };
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return roll * 4 / 3;
	}

	public static SaradominSword get() {
		return INSTANCE;
	}

}
