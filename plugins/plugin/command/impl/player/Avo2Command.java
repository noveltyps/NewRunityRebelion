package plugin.command.impl.player;

import io.server.content.activity.impl.allvsone2.AllVsOne2;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class Avo2Command implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		AllVsOne2.create(player);
		player.locking.unlock();
		}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	@Override
	public String description() {
		return "Teleports you to AVO V2";
	}
}
