package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class RevenantCaveCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		
		if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
            player.message("@red@You can no longer take custom's into the wilderness!");
            return;
        }
		
		player.dialogueFactory.sendOption("@red@Teleport me [Wilderness]", () -> {

				Teleportation.teleport(player, Config.REV_CAVES);
				player.message("You have teleported to Revenant Caves");
				

		}, "Cancel", () -> {

			player.dialogueFactory.clear();
		}).execute();
	}
	

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	
	@Override
	public String description() {
		return "Teleports you to the Revenant cave.";
	}
}
