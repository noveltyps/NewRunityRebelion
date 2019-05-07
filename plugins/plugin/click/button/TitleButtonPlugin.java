package plugin.click.button;

import io.server.content.tittle.TitleManager;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class TitleButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (button >= -26485 && button <= -26420) {
			TitleManager.click(player, button);
			return true;
		}
		switch (button) {
		case -26525:
			TitleManager.redeem(player);
			break;
		case -26528:
			TitleManager.reset(player);
			break;
		}
		return false;
	}
}
