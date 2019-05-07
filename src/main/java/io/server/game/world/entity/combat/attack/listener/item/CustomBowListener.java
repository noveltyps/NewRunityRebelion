package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * @author Adam_#6723
 */
@ItemCombatListenerSignature(requireAll = true, items = {13749, 13831, 13713, 22162})
public class CustomBowListener extends SimplifiedListener<Mob> {

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		
		if(Utility.random(1, 3) < 1) {
		if (hit.getDamage() <= 0) {
			hit.setDamage(RandomUtils.inclusive(5, 40));
	     	}
		}
	}
}
