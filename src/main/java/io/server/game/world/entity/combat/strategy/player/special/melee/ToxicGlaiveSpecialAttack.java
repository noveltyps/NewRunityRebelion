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
 * Handles the Toxic Glaive weapon special attack.
 *
 * @author Adam_#6723
 */
public class ToxicGlaiveSpecialAttack extends PlayerMeleeStrategy {

	private static final Animation ANIMATION = new Animation(439, UpdatePriority.HIGH);

	private static final Graphic OTHER_GRAPHIC = new Graphic(184);
	private static final Graphic GRAPHIC = new Graphic(1213);

	private static final ToxicGlaiveSpecialAttack INSTANCE = new ToxicGlaiveSpecialAttack();

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		super.attack(attacker, defender, hit);
		attacker.graphic(GRAPHIC);
		attacker.speak("You just got knocked the F@!# out!");

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

	public static ToxicGlaiveSpecialAttack get() {
		return INSTANCE;
	}

}

/*
 * TOXIC_GLAIVE(new int[]{11063}, 50, DefaultMelee.get()) {
 * 
 * @Override public void enable(Player player) { if
 * (player.getSpecialPercentage().intValue() <
 * player.getCombatSpecial().getAmount()) { player.send(new
 * SendMessage("You do not have enough special energy left!")); return; }
 * player.animate(new Animation(439, UpdatePriority.HIGH)); player.graphic(new
 * Graphic(184, true, UpdatePriority.HIGH));
 * player.speak("You just got knocked the F@!# out!"); drain(player, 50); } },
 */