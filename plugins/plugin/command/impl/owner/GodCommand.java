package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class GodCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.skills.setLevel(0, 1500);
		player.skills.setLevel(1, 1500);
		player.skills.setLevel(2, 1500);
		player.skills.setLevel(3, 1500);
		player.skills.setLevel(4, 1500);
		player.send(new SendMessage("You are now in God mode."));
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Sets your combat stats to 1500.";
	}
}
