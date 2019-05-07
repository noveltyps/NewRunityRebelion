package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

/**
 * Handles the Pheonix necklace listener. OSRS Wiki:
 * http://oldschoolrunescape.wikia.com/wiki/Phoenix_necklace
 *
 * @author Daniel
 */
@ItemCombatListenerSignature(requireAll = false, items = { 11090 })
public class PhoenixNecklaceListener extends SimplifiedListener<Player> {

	@Override
	public void block(Mob attacker, Player defender, Hit hit, CombatType combatType) {
		if (defender.getCurrentHealth() - hit.getDamage() <= 0)
			return;
		if (defender.getCurrentHealth() - hit.getDamage() <= defender.getMaximumHealth() * 0.20) {
			defender.send(new SendMessage("The Phoenix necklace of life saves you but was destroyed in the process"));
			defender.heal((int) (defender.getMaximumHealth() * 0.30));
			defender.getCombat().removeListener(this);
			defender.equipment.remove(11090);
		}
	}
}
