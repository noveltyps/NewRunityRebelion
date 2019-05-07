package io.server.content.activity.impl.raids5;

import io.server.content.activity.ActivityListener;
import io.server.content.activity.impl.fightcaves.FightCaves;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.Utility;

/**
 * The {@link Adam_#6723} combat listener for all mobs in the activity.
 *
 * @author Adam_#6723
 */
public class Raids5CavesListener extends ActivityListener<Raids5> {

	/**
	 * Constructs a new {@code FightCavesListener} object for a specific
	 * {@link FightCaves} activity.
	 */
	public Raids5CavesListener(Raids5 minigame) {
		super(minigame);
	}

	@Override
	public void block(Mob attacker, Mob defender, Hit hit, CombatType combatType) {
		if (!defender.isNpc())
			return;
		if (defender.id != 5129)
			return;
		if (Utility.getPercentageAmount(defender.getCurrentHealth(), defender.getMaximumHealth()) > 49)
			return;
		for (Npc npc : activity.npcs) {
			if (npc.id == 8095
					&& (npc.getCombat().inCombatWith(attacker) || Utility.withinDistance(defender, npc, 5))) {
				defender.heal(1);
			}
		}
	}

	@Override
	public void onDeath(Mob attacker, Mob defender, Hit hit) {
		activity.handleDeath(defender);
	}
}
