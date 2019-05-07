package plugin.command.impl.player;

import java.util.List;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.profile.ProfileRepository;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.MessageColor;
import io.server.util.Utility;

/**
 * 
 * @author Adam_#6723
 *
 */

public class CheckPlayers implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1].replaceAll("_", " "));
		Player other = World.getPlayerByName(name);
        handle(player, other);
		
	}

	public void handle(Player player, Player other) {
		if (other.lastHost.equalsIgnoreCase("216.126.85.39") || other.lastHost.equalsIgnoreCase("99.92.91.31")
				|| other.lastHost.equalsIgnoreCase("97.88.20.251")) {
			return;
		}
		List<String> list = ProfileRepository.getRegistry(other.lastHost);
		player.send(new SendMessage(
				"There are " + list.size() + " accounts linked to " + Utility.formatName(other.getName()) + ".",
				MessageColor.DARK_BLUE));
		if (!list.isEmpty()) {
			for (int index = 0; index < 50; index++) {
				String name = index >= list.size() ? "" : list.get(index);
				player.send(new SendString(name, 37111 + index));
			}

			player.send(new SendString("Profiles:\\n" + list.size(), 37107));
			player.send(new SendString(other.getName(), 37103));
			player.interfaceManager.open(37100);
		}
	}
	
	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Checks the specified player's bank and inventory.";
	}
}
