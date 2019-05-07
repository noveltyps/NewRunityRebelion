package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class StaffZoneCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, new Position(2602, 3874, 0));
		player.send(new SendMessage("@or2@Welcome to the staffzone, " + player.getName() + "."));
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Teleports you to the staff zone.";
	}
}
