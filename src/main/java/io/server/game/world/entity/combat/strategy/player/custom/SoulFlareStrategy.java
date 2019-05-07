package io.server.game.world.entity.combat.strategy.player.custom;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

/**
 * 
 * @author Adam_#6723 Handles the animation of Soulflare and gfx with it
 *         aswell as effects.
 *
 */

public class SoulFlareStrategy extends PlayerMeleeStrategy {

	private static final SoulFlareStrategy INSTANCE = new SoulFlareStrategy();

	public String name() {
		return "Soulflare";
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	/** Atack delay. **/

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 2;
	}

	/** Instane's the class to be called upon,and applied to an item. **/
	public static SoulFlareStrategy get() {
		return INSTANCE;
	}

	private static final Animation ANIMATION = new Animation(439, UpdatePriority.HIGH);

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		defender.graphic(new Graphic(986, UpdatePriority.HIGH));
		attacker.graphic(new Graphic(1249, UpdatePriority.HIGH));
		super.attack(attacker, defender, hit);
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[] { nextMeleeHit(attacker, defender) };
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return roll * 5 / 3;
	}

}
