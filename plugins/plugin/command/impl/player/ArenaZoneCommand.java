package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class ArenaZoneCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, Config.ARENA_ZONE);
     //  new InfernoBoss().spawnbosswave(player);
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	
	@Override
	public String description() {
		return "Teleports you to the Arena.";
	}

}
