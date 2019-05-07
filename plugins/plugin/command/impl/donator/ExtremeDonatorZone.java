package plugin.command.impl.donator;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */
public class ExtremeDonatorZone implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, Config.EXTREME_ZONE);
		player.send(new SendMessage("You have teleported to the Extreme Donator Zone!"));
	}

	@Override
	public boolean canUse(Player player) {
		return (PlayerRight.isExtreme(player) || PlayerRight.isElite(player) || PlayerRight.isKing(player)
				|| PlayerRight.isSupreme(player) || PlayerRight.isPriviledged(player));
	}

	@Override
	public String description() {
		return "Teleports you to the Extreme donator zone.";
	}
}
