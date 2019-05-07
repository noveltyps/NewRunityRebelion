package io.server.game.world.entity.combat.strategy.player.special.melee;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

public class GraniteMaul extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(1667, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(340);
	private static final GraniteMaul INSTANCE = new GraniteMaul();

	private GraniteMaul() {
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);

		if (attacker.attributes.is("granite-maul-spec")) {
			CombatHit damage = nextMeleeHit(attacker, defender);
			defender.damage(damage);
			defender.getCombat().getDamageCache().add(attacker, damage);
			attacker.getCombatSpecial().drain(attacker);
			attacker.attributes.remove("granite-maul-spec");
		}

		attacker.graphic(GRAPHIC);
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
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 3;
	}

	public static GraniteMaul get() {
		return INSTANCE;
	}

}