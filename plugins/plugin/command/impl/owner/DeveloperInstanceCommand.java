package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

public class DeveloperInstanceCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.message("instance is " + player.instance);
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Tells you your instance number.";
	}
}
