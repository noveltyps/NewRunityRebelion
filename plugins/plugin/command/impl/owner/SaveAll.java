package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

public class SaveAll implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		World.save();
		World.sendMessage("[SAVE] You're player files have been safely saved, this is to prevent rollbacks.");
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Saves all players.";
	}
}
