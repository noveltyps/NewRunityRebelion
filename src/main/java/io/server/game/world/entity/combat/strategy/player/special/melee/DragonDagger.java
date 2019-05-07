package io.server.game.world.entity.combat.strategy.player.special.melee;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/** @author Michael | Chex *///Updated 
public class DragonDagger extends PlayerMeleeStrategy {
	private static final DragonDagger INSTANCE = new DragonDagger();

	private static final Animation ANIMATION = new Animation(1062, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(252, true, UpdatePriority.HIGH);

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		super.attack(attacker, defender, hit);
		
		if(Utility.random(1, 5) == 1) {
		if (hit.getDamage() == 0) {
			hit.setDamage(RandomUtils.inclusive(0, 20));
		    }
		}
		attacker.graphic(GRAPHIC);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[] { nextMeleeHit(attacker, defender), nextMeleeHit(attacker, defender) 
				};
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return roll * 4 / 3;
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int damage) {
		return damage * 23 / 20;
	}

	public static DragonDagger get() {
		return INSTANCE;
	}

}
