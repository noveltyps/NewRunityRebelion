package io.server.game.world.entity.combat.attack.listener.item;

import io.server.Config;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Area;
import io.server.net.packet.out.SendMessage;

/**
 * Handles the ring of life listener. OSRS Wiki:
 * http://oldschoolrunescape.wikia.com/wiki/Ring_of_life
 *
 * @author Daniel
 */
@ItemCombatListenerSignature(requireAll = false, items = { 2570 })
public class RingOfLifeListener extends SimplifiedListener<Player> {

	@Override
	public void block(Mob attacker, Player defender, Hit hit, CombatType combatType) {
		if (Area.inDuelArena(defender))
			return;
		if (defender.getCurrentHealth() - hit.getDamage() <= 0)
			return;
		if (defender.getCurrentHealth() - hit.getDamage() <= defender.getMaximumHealth() * 0.10) {
			if (Teleportation.teleport(defender, Config.DEFAULT_POSITION)) {
				defender.send(new SendMessage("The Ring of life has saved you; but was destroyed in the process."));
			}
			defender.getCombat().removeListener(this);
			defender.equipment.remove(2570);
		}
	}
}
