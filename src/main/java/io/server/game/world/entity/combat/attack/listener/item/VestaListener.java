package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.position.Area;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * Handles the Vesta armour effect, which if equipped reduces drain rate by 25%
 * && steals 50% of the mob's life for the player themselves.
 *
 * @author Adam_#6723
 */
@ItemCombatListenerSignature(requireAll = false, items = { 19687, 8798, 8799, 8800, 8801 })
public class VestaListener extends SimplifiedListener<Mob> {

	public int healingGraphic = 1296;

	public void method(Mob attacker) {
		if(Area.inWilderness(attacker)) {
			return;
		}
		}

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		if(Area.inWilderness(attacker)) {
			return;
		}
		if (Math.random() > 0.25) {
			attacker.heal(hit.getDamage() / 2);
			attacker.graphic(new Graphic(healingGraphic, UpdatePriority.HIGH));
	     	}
		if(hit.getDamage() == 0 && Utility.random(1, 5) == 1) {
			hit.setDamage(RandomUtils.inclusive(0, 40));
		   }
	}

	@Override
	public void block(Mob attacker, Mob defender, Hit hit, CombatType combatType) {
		if(Area.inWilderness(attacker)) {
			return;
		}
		method(attacker);
	}
}
