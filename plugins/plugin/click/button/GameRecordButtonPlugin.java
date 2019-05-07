package plugin.click.button;

import io.server.content.activity.ActivityType;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class GameRecordButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (button == 32307) {
			player.gameRecord.global = true;
			player.gameRecord.display(ActivityType.getFirst());
			return true;
		}
		if (button == 32309) {
			player.gameRecord.global = false;
			player.gameRecord.display(ActivityType.getFirst());
			return true;
		}
		int base_button = 32352;
		int index = (button - base_button) / 2;
		ActivityType activity = ActivityType.getOrdinal(index);
		if (activity == null)
			return false;
		player.gameRecord.display(activity);
		return true;
	}
}
