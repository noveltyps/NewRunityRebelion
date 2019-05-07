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

public class BroadcastCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {

		try {
			final String message = command.substring(9, command.length());

			World.sendBroadcast(1, message, false);
		} catch (ArrayIndexOutOfBoundsException e) {
			player.message("Invalid use of the command do ::broadcast 'input message'.");
		}
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isPriviledged(player);
	}

	@Override
	public String description() {
		return "Broadcasts a message to everyone's screen.";
	}
}
