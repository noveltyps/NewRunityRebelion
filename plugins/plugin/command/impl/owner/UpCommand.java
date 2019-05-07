package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * 
 * @author Adam_#6723
 *
 */

public class UpCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.move(player.getPosition().transform(0, 0, 1));
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Moves your character up by one floor.";
	}
}
