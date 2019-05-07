package io.server.game.world.entity.combat.strategy.player.special.melee;

import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;

/**
 * Handles the staff of the dead weapon special attack.
 *
 * @author Daniel
 */
public class DefaultMelee extends PlayerMeleeStrategy {

	private static final DefaultMelee INSTANCE = new DefaultMelee();

	public static DefaultMelee get() {
		return INSTANCE;
	}

}