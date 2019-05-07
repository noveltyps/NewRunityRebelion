package plugin.click.button;

import io.server.game.Animation;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

/**
 * 
 * @author Adam_#6723
 *
 */

public class StopBottingButtonPlugin extends PluginContext {
	@Override
	protected boolean onClick(Player player, int button) {
		if (button == -16028) {
			player.abortBot = true;
			player.interfaceManager.close();
			player.walkTo(player.getPosition());
			player.action.clearNonWalkableActions();
			player.resetAnimation();
			player.animate(new Animation(0), true);

			return true;
		}
		return false;
	}
}