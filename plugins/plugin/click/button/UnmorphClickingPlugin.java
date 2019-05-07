package plugin.click.button;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

/**
 * 
 * @author Adam_#6723
 *
 */

public class UnmorphClickingPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		switch (button) {
		case 6020:
			player.playerAssistant.transform(-1, true);
			return true;
		}
		return false;
	}
}
