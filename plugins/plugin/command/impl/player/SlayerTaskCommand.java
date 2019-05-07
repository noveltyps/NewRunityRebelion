package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;

public class SlayerTaskCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.message("You have " + player.slayer.getAmount() + " of " + player.slayer.getTask() + "!");
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	
	@Override
	public String description() {
		return "Tells you your current slayer task.";
	}
}
