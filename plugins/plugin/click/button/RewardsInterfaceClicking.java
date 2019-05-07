package plugin.click.button;

import io.server.content.rewardsList.RewardsListHandler;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class RewardsInterfaceClicking  extends PluginContext {
	
	protected boolean onClick(Player player, int button) {
		new RewardsListHandler(player).button(button);
		return false;
	}
}
