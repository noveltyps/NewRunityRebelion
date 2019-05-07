package plugin.click.button;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class ExamineButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (button == -13390) {
			player.interfaceManager.close();
			return true;
		}

		return false;
	}

}
