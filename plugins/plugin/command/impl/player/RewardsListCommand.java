package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.content.rewardsList.RewardsListHandler;
import io.server.game.world.entity.mob.player.Player;

/**
 * 
 * @author Adam_#6723
 *
 */
public class RewardsListCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		new RewardsListHandler(player).Open();
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	
	@Override
	public String description() {
		return "Opens the rewards List Interface";
	}

}
