package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;

/**
 * Handles the Soul armour effect, which if equipped reduces drain rate by 25%
 * && steals 50% of the mob's life for the player themselves.
 *
 * @author Adam_#6723
 */
@ItemCombatListenerSignature(requireAll = true, items = { 13693, 13696, 13695, 17158, 13692 })
public class SoulListener extends SimplifiedListener<Mob> {

	public int healingGraphic = 1296;

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		if (Math.random() > 0.50) {
			attacker.heal(hit.getDamage() / 2);
			attacker.graphic(new Graphic(healingGraphic, UpdatePriority.HIGH));
		}
	}
}
