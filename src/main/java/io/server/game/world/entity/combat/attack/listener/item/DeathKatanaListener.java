package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * Handles the Lime whip effect, which if equipped reduces drain rate by 25%
 * && steals 50% of the mob's life for the player themselves.
 *
 * @author Adam_#6723
 */
@ItemCombatListenerSignature(requireAll = false, items = {11642, 22161})
public class DeathKatanaListener extends SimplifiedListener<Mob> {

	public int healingGraphic = 1296;

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		if (Math.random() > 0.50) {
			attacker.heal(hit.getDamage() / 2);
			attacker.graphic(new Graphic(healingGraphic, UpdatePriority.HIGH));
		}
		if(Utility.random(1, 5) == 1) {
		if (hit.getDamage() <= 10) {
			hit.setDamage(RandomUtils.inclusive(10, 55));
			attacker.graphic(new Graphic(483, UpdatePriority.HIGH));
	     	}
		}
	}
}
