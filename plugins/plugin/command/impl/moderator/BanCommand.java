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

public class BanCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String playerToBan = String.format(parts[1].replaceAll("_", " "));

		World.search(playerToBan.toString()).ifPresent(toBan -> {
			if (PunishmentExecuter.banned(toBan.getUsername())) {
				player.send(new SendMessage("Player " + toBan.getUsername() + " already has an active ban."));
				return;
			}
			PunishmentExecuter.ban(toBan.getUsername());
			player.send(new SendMessage("Player " + toBan.getUsername() + " was successfully banned"));
			World.kickPlayer(toBan.getUsername());
			
			player.send(new SendInputMessage("ENTER THE REASON FOR THE BAN", 100, string -> {
				try {
					System.err.println("" + string);
					new DiscordManager(DiscordConstant.STAFF_BAN_LOGS, player, playerToBan + " Has just been Banned by", "Reason: " + string).log();
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
		return "Bans the specified player.";
	}
}
