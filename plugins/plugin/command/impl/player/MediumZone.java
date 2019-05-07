package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */
public class MediumZone implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, Config.MEDIUM_ZONE);
		player.send(new SendMessage("You have teleported to the Medium zone!"));
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	
	@Override
	public String description() {
		return "Teleports you to the Medium zone.";
	}

}
