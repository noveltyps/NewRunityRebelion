package io.server.game.world.entity.combat.attack.listener.item.voidknight;

import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

/**
 * Handles the slayer helmet / black mask effects ofr slayer tasks.
 *
 * @author Michael | Chex
 */
@ItemCombatListenerSignature(requireAll = true, items = { 11665, 8839, 8840, 8842 })
public class VoidKnightMeleeListener extends SimplifiedListener<Player> {

	@Override
	public int modifyAttackLevel(Player attacker, Mob defender, int level) {
		if (attacker.getStrategy().getCombatType() != CombatType.MELEE)
			return level;
		return level * 11 / 10;
	}

	@Override
	public int modifyStrengthLevel(Player attacker, Mob defender, int level) {
		if (attacker.getStrategy().getCombatType() != CombatType.MELEE)
			return level;
		return level * 11 / 10;
	}

}
