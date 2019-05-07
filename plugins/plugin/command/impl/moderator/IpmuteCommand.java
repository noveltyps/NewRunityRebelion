package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.punishments.PunishmentExecuter;
import io.server.net.packet.out.SendMessage;

public class IpmuteCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1].replaceAll("_", " "));

		World.search(name.toString()).ifPresent(other -> {
			if (PunishmentExecuter.IPMuted(other.lastHost)) {
				player.send(new SendMessage("Player " + other.getUsername() + "'s IP is already IPMuted"));
				return;
			}
			final String mutedIP = other.lastHost;
			PunishmentExecuter.addMutedIP(mutedIP);
			player.send(new SendMessage("Player " + other.getUsername() + " was successfully IPMuted"));
			other.send(new SendMessage("You have been IPMuted by " + player.getUsername() + "."));
		});
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player) || PlayerRight.isPriviledged(player);
	}

	@Override
	public String description() {
		return "Mutes the specified player's IP address.";
	}
}
