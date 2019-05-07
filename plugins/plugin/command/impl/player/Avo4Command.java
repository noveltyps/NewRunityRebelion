package plugin.command.impl.player;

import io.server.content.activity.impl.allvsone4.AllVsOneV4;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class Avo4Command implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		AllVsOneV4.create(player);
		player.locking.unlock();
		}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	@Override
	public String description() {
		return "Teleports you to AVO V4";
	}
}
