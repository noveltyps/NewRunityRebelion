package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.content.discord.DiscordConstant;
import io.server.content.discord.DiscordManager;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class KickCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1].replaceAll("_", " "));
		World.kickPlayer(p -> p.getName().equalsIgnoreCase(name));
		player.send(new SendMessage("@or2@You have kicked " + name + "!"));
		player.send(new SendInputMessage("ENTER THE REASON FOR THE KICK", 100, string -> {
			try {
				System.err.println("" + string);
				new DiscordManager(DiscordConstant.STAFF_KICK_LOGS, player, name + " Has just been Kicked by", "Reason: " + string).log();
			} catch (Exception e) {
				System.err.println("Erorr.");
			}
		}));
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Kicks the specified player.";
	}
}
