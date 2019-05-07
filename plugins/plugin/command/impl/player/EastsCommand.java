package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class EastsCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		
		
		if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
            player.message("@red@You can no longer take custom's into the wilderness!");
            return;
        }
		
		Teleportation.teleport(player, Config.EASTS);
		player.send(new SendMessage("Welcome to Easts"));
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Teleports you to green dragons in eastern wilderness.";
	}
}
