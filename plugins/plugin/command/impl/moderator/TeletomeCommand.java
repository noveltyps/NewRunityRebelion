package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class TeletomeCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1].replaceAll("_", " "));

		if (World.search(name).isPresent()) {
			final Player target = World.search(name).get();
			if (target.isBot) {
				player.send(new SendMessage("@or2@You can't teleport bot to you!"));
				return;
			}
			if (target.getCombat().inCombat() && !PlayerRight.isDeveloper(player)) {
				player.message("@or2@That player is currently in combat!");
				return;
			}

			target.move(player.getPosition());
			target.instance = player.instance;
		} else {
			player.send(new SendMessage("@or2@The player '" + name + "' @or2@either doesn't exist, or is offline."));
			player.send(new SendMessage("@or2@For players with spaces in their names use player_name"));
		}
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Teleports the specified player to you.";
	}
}
