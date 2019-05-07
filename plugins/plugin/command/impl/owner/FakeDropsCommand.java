package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item; 

/**
 * 
 * @author Adam_#6723
 *
 */

public class FakeDropsCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		try {
			String args[] = command.split(" ");
			if (args.length > 2 || args.length < 2) {
				player.sendMessage("Use as ::fakedrops itemID npcName");
				return;
			}
			World.sendDropMessage(player, new Item(Integer.parseInt(args[0])), args[1]);
		} catch (Exception e) {
			player.sendMessage("Use as ::fakedrops itemID npcName");
		}
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Sends a fake drop message.";
	}
}
