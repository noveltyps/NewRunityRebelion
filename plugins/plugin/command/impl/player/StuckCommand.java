package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;

public class StuckCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		World.sendStaffMessage("[STUCK]" + player.getName() + " Is Stuck!");
		player.message("Staff team have been alerted!");
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	
	@Override
	public String description() {
		return "Sends a message to all staff members that your character is stuck.";
	}
}
