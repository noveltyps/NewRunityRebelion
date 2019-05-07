package io.server.game.world.entity.combat.strategy.player.special.melee;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.net.packet.out.SendMessage;

/** @author Daniel | Obey */
public class DragonScimitar extends PlayerMeleeStrategy {
	
	private static final Animation ANIMATION = new Animation(1872, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(347);
	private static final DragonScimitar INSTANCE = new DragonScimitar();

	private DragonScimitar() {
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit h) {
		if (!defender.isPlayer())
			return;

		defender.prayer.deactivate(Prayer.PROTECT_FROM_MAGIC, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_RANGE);
		defender.getPlayer().send(new SendMessage("Your overhead prayers have been disabled!"));
		attacker.getPlayer().send(new SendMessage("You have disabled " + defender.getName() + "'s overhead prayers!"));
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 4;
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	public static DragonScimitar get() {
		return INSTANCE;
	}

}