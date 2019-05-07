package plugin.click.button;

import io.server.content.playerguide.PlayerGuideDisplay;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

/**
 * Handles the interface opening
 * 
 * @author Nerik#8690
 *
 */

public class PlayerGuideInterfacePlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {

		PlayerGuideDisplay.display(player, button);

		/** Closes the interface! **/
		if (button == -23034) {
			player.interfaceManager.close();
		}

		return false;
	}

}
