package io.server.game.world.entity.combat.attack.listener.npc;

import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.Utility;

/** @author Daniel */
@NpcCombatListenerSignature(npcs = { 3103 })
public class Alkahrid extends SimplifiedListener<Npc> {

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		if (!attacker.isPlayer())
			return;

		int currentHealth = defender.getCurrentHealth();
		int maximumHealth = defender.getMaximumHealth();

		if (currentHealth == maximumHealth) {
			Player player = attacker.getPlayer();

			for (Npc monster : player.viewport.getNpcsInViewport()) {
				if (monster.id != 3103)
					continue;
				if (monster.equals(defender))
					continue;
				if (monster.getCombat().inCombat() && monster.getCombat().getDefender() != null)
					continue;
				if (!Utility.within(attacker.getPosition(), monster.getPosition(), 10))
					continue;
				monster.speak("Brother, I will help thee with this infidel!");
				monster.getCombat().attack(attacker);
			}
		}
	}
}
