package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class GiveAllCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		/*
		 * int itemId = Integer.parseInt(command[1]); int amount =
		 * Integer.parseInt(command[2]); World.sendItem(itemId, amount);
		 */
		int itemId = Integer.parseInt(parts[1]);
		int amount = Integer.parseInt(parts[2]);
		for (Player players : World.getPlayers()) {
			if (players != null) {
				players.inventory.add(new Item(itemId, amount));
				Item starterbox = new Item(itemId);
				players.send(new SendMessage("You have all received a " + starterbox.getName() + " From "+player.getName()+"!"));
				players.send(new SendMessage(
						"@lre@This is a token of appreciation from Adam himself, for being loyal players!"));
			}
		}
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Gives the specified item to all online players.";
	}
}
