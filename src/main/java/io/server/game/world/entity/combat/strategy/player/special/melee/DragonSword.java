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

/** @author Adam_#6723 <-- Discord */

public class DragonSword extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(7515, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1369);
	private static final DragonSword INSTANCE = new DragonSword();

	private DragonSword() {
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

		defender.prayer.deactivate(Prayer.PROTECT_FROM_MELEE);
		defender.getPlayer().send(new SendMessage("Your overhead prayers have been disabled!"));
		attacker.getPlayer().send(new SendMessage("You have disabled " + defender.getName() + "'s overhead prayers!"));
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 4;
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int damage) {
		return damage * 3 / 2;
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	public static DragonSword get() {
		return INSTANCE;
	}

}
