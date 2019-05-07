package plugin.command.impl.player;

import io.server.content.activity.impl.inferno.Inferno;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class InfernoCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
        Inferno.create(player);
		}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	@Override
	public String description() {
		return "Teleports you to Inferno.";
	}
}
