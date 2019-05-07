package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.content.discord.DiscordConstant;
import io.server.content.discord.DiscordManager;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.punishments.PunishmentExecuter;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class MuteCommand implements Command {
	
	public static String info;

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1].replaceAll("_", " "));

		World.search(name.toString()).ifPresent(other -> {
			if (PunishmentExecuter.muted(other.getUsername())) {
				player.send(new SendMessage("Player " + other.getUsername() + " already has an active mute."));
				return;
			}
			PunishmentExecuter.mute(other.getUsername());
			player.send(new SendMessage("Player " + other.getUsername() + " was successfully muted."));
			player.message("You have muted! " +player.getName());
		//	World.kickPlayer(other.getUsername());
			player.send(new SendMessage("You have been muted by " + player.getUsername() + "."));
			player.send(new SendInputMessage("ENTER THE REASON FOR THE MUTE", 100, string -> {
				try {
					System.err.println("" + string);
					new DiscordManager(DiscordConstant.STAFF_MUTE_LOGS, player, other.getUsername() + " Has just been Muted by", "Reason: " + string).log();
				} catch (Exception e) {
					System.err.println("Erorr.");
				}
			}));
		});
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Mutes the specified player.";
	}
}
