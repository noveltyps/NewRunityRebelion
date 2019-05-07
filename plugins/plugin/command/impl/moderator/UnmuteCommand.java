package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.punishments.PunishmentExecuter;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class UnmuteCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1].replaceAll("_", " "));
		
		World.search(name.toString()).ifPresent(other -> {
				if (!PunishmentExecuter.muted(other.getUsername())) {
					player.send(new SendMessage("Player " + other.getUsername() + " is not muted!"));
					return;
				}
				PunishmentExecuter.unmute(other.getUsername());
				player.send(new SendMessage("Player " + other.getUsername() + " was successfully unmuted."));
				other.send(new SendMessage("@red@ You have been unmuted by " + player.getUsername()));
		});
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Unmutes the specified player.";
	}
}
