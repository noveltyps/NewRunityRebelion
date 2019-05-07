package plugin.command.impl.player;

import io.server.content.activity.impl.soloraids5.Raids5;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class SoloRaids5Command implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
        Raids5.create(player);
		}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	@Override
	public String description() {
		return "Teleports you to Solo Raids 4.";
	}
}
