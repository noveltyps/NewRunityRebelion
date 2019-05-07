package io.server.game.world.entity.combat.strategy.player.special.melee;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

/** @author Daniel | Obey */
public class DragonLongsword extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(1058, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(248);
	private static final DragonLongsword INSTANCE = new DragonLongsword();

	private DragonLongsword() {
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 4;
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int damage) {
		return (int) (damage * 1.15);
	}

	public static DragonLongsword get() {
		return INSTANCE;
	}

}