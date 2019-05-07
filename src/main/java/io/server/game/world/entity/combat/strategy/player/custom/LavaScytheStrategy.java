package io.server.game.world.entity.combat.strategy.player.custom;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.basic.MeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

public class LavaScytheStrategy extends MeleeStrategy<Player> {

	private static final LavaScytheStrategy INSTANCE = new LavaScytheStrategy();
	private static final Animation ANIMATION = new Animation(1203, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1172, true, UpdatePriority.HIGH);

	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);

		if (attacker.getCombat().getDefender() == defender) {
			attacker.animate(getAttackAnimation(attacker, defender));
		}
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		int animation = attacker.getCombat().getFightType().getAnimation();
		return new Animation(animation, UpdatePriority.HIGH);
		// return ANIMATION;
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		super.attack(attacker, defender, hit);
		attacker.graphic(GRAPHIC);
		addCombatExperience(attacker, hit);
	}

	@Override
	public int getAttackDistance(Player attacker, FightType fightType) {
		return fightType.getDistance();
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		CombatHit first = nextMeleeHit(attacker, defender);

		if (first.getDamage() < 1) {
			return secondOption(attacker, defender, first);
		}

		CombatHit second = first.copyAndModify(damage -> damage / 2);
		//CombatHit third = second.copyAndModify(damage -> damage / 2);
		return new CombatHit[] { first, second };
	}

	private CombatHit[] secondOption(Player attacker, Mob defender, CombatHit inaccurate) {
		CombatHit second = nextMeleeHit(attacker, defender);
		
		CombatHit third = second.copyAndModify(damage -> damage / 2);
		return new CombatHit[] { second, third };
	}


	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	public static LavaScytheStrategy get() {
		return INSTANCE;
	}

	public static int random(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	@Override
	public boolean canAttack(Player attacker, Mob defender) {
		// TODO Auto-generated method stub
		return true;
	}

	public static Animation getAnimation() {
		return ANIMATION;
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		// TODO Auto-generated method stub
		return 4;
	}

	public static Graphic getGraphic() {
		return GRAPHIC;
	}
}
