package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * 
 * @author Adam_#6723
 *
 */

public class QuickHelpCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
        World.sendMessage("@red@" + player.getName() +" @blu@is avaliable for any questions! feel free to PM Him!");
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Announces that you are accepting questions via PM.";
	}
}
