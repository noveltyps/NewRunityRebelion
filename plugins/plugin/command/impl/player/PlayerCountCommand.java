package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

public class PlayerCountCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.send(new SendMessage("There are currently " + World.getPlayerCount() + " players playing RebelionXOS",
				MessageColor.RED));
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	
	@Override
	public String description() {
		return "Displays the current player count.";
	}
}
