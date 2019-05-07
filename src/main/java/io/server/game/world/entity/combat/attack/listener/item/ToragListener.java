package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Handles the Torag's armor effects to the assigned npc and item ids.
 *
 * @author Adam_#6723
 */
@NpcCombatListenerSignature(npcs = { 1676 })
@ItemCombatListenerSignature(requireAll = true, items = { 4745, 4747, 4749, 4751 })
public class ToragListener extends SimplifiedListener<Mob> {

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		if(Utility.random(1, 10) == 1) {
		if (defender.isPlayer() && hit.getDamage() > 1) {
			boolean success = Utility.random(100) <= 25;

			if (!success)
				return;

			Player player = defender.getPlayer();
			int energy = player.runEnergy;
			int drain = energy < 50 ? 10 : 20;

			energy -= drain;

			if (energy < 0)
				energy = 0;

			player.runEnergy = energy;
			player.send(new SendMessage(drain + "% run energy has been drained by " + attacker.getName() + "."));
			player.graphic(new Graphic(399, UpdatePriority.VERY_HIGH));

			if (attacker.isPlayer()) {
				attacker.getPlayer().send(
						new SendMessage("You have drained " + drain + "% of " + defender.getName() + "'s run energy."));
			}
		}

		super.hit(attacker, defender, hit);
	   }
	}
}
