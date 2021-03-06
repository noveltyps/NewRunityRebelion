package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendURL;

//TODOCOMMAND
public class Commands implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.send(new SendURL(""));
		player.message("Opening command list!");
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Displays all available commands.";
	}
}
