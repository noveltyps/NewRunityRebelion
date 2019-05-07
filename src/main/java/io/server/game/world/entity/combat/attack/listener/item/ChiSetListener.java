package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;

/**
 * Handles the Chi/kril armour effect, which if equipped reduces drain rate by
 * 12% && steals 25% of the mob's life for the player themselves.
 *
 * @author Adam_#6723
 */
@ItemCombatListenerSignature(requireAll = true, items = { 13703, 13704, 13705 })
public class ChiSetListener extends SimplifiedListener<Mob> {


	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		if (Math.random() > 0.50) {
			attacker.heal(hit.getDamage() / 4);
			attacker.graphic(new Graphic(398, UpdatePriority.HIGH));
		}
	}
}
