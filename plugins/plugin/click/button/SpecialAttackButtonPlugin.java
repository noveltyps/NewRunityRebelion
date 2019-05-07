package plugin.click.button;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class SpecialAttackButtonPlugin extends PluginContext {

	/**
	 * @author adameternal123 handles clicking on the special attack button
	 */
	@Override
	protected boolean onClick(Player player, int button) {

		if (button == 1998) {

			if (player.getCombatSpecial() != null) {
				if (!player.isSpecialActivated()) {
					player.getCombatSpecial().enable(player);
				} else {
					player.getCombatSpecial().disable(player, true);
				}
			}

		}

		return false;
	}
}
