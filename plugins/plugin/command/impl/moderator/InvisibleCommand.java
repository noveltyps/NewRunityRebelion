package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class InvisibleCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		 player.setVisible(!player.isVisible());
         player.send(new SendMessage(String.format("You are now %s.", player.isVisible() ? "visible" : "hidden")));
         }

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Toggles character visibility.";
	}
}
