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

public class RemindVote implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
        World.sendMessage("<img=2> @blu@10 Players have just voted! do ::vote to get rewards!");
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Reminds all players to vote.";
	}
}
