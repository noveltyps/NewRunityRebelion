package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

/**
 * @author Adam_#6723
 */

public class Wests implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		
		if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
            player.message("@red@You can no longer take custom's into the wilderness!");
            return;
        }
		
		
		Teleportation.teleport(player, Config.WESTS);
		player.send(new SendMessage("@or2@Goodluck, " + player.getName() + "!"));
		player.send(new SendMessage("@or2@You might need it.."));
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Teleports you to green dragons in western wilderness.";
	}
}
