package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.content.freeforall.impl.FreeForAllLobbyTask;
import io.server.game.world.entity.mob.player.Player;

public class FreeForAllLobbyCommand implements Command {

	
	@Override
	public void execute(Player player, String command, String[] parts) {
		new FreeForAllLobbyTask(player).execute();
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	
	@Override
	public String description() {
		return "Teleports you to the free-for-all lobby.";
	}
}
