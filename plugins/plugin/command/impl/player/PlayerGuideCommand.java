package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.content.playerguide.PlayerGuideHandler;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class PlayerGuideCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {

		PlayerGuideHandler guide = new PlayerGuideHandler();
		guide.open(player);

	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Opens the player guide.";
	}
}
