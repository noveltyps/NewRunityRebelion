package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.Utility;

/**
 * Handles the Elysian spirit shield listener. OSRS Wiki:
 * http://oldschoolrunescape.wikia.com/wiki/Elysian_spirit_shield
 *
 * @author Adam_#6
 */
@ItemCombatListenerSignature(requireAll = true, items = { 22326, 22327, 22328 })
public class JusticiarSetListener extends SimplifiedListener<Player> {

	@Override
	public void block(Mob attacker, Player defender, Hit hit, CombatType combatType) {
		if (Utility.random(1, 10) <= 7) {
			hit.modifyDamage(damage -> damage * 3 / 4);
			defender.graphic(new Graphic(321, UpdatePriority.HIGH));
		}
	}
}
