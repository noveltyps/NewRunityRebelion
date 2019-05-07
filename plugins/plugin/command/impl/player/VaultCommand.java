package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class VaultCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.bankVault.value();
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Displays your bank vault's value.";
	}
}
