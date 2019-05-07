package plugin.command.impl.player;

import io.server.content.DropDisplay;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;

public class DropsCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		DropDisplay.open(player);
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Opens the drop table interface.";
	}
}
