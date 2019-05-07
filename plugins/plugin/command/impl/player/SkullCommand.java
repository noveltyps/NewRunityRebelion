package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;

public class SkullCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.skulling.skull();
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	
	@Override
	public String description() {
		return "Gives your player a wilderness skull.";
	}
}
