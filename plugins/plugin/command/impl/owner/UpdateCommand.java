package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * 
 * @author Adam_#6723
 *
 */

public class UpdateCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		int time = Integer.parseInt(parts[1]);
		World.update(time * 1000 / 60, false);

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Updates the server in the specified amount of minutes.";
	}
}
