package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

/**
 * @author Adam_#6723
 */

public class InterfaceCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		int id = Integer.parseInt(parts[1]);
		player.interfaceManager.open(id);
		player.send(new SendMessage("Opening interface: " + id, MessageColor.LIGHT_PURPLE));

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Opens the specified interface ID.";
	}
}
