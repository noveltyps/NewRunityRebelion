package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * @author Adam_#6723
 */
@NpcCombatListenerSignature(npcs = { 1674 })
@ItemCombatListenerSignature(requireAll = true, items = { 4732, 4734, 4736, 4738 })
public class KarilsListener extends SimplifiedListener<Mob> {

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		
		if(Utility.random(1, 5) == 1) {
		if (hit.getDamage() == 0) {
			hit.setDamage(RandomUtils.inclusive(0, 20));
	     	}
		}
	}
}
