package plugin.command.impl.owner;

import io.server.Config;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * @author Adam_#6723 test
 */

public class SpawnCustomCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.bank.clear();
		player.bank.addAll(Config.CUSTOM_ITEMS);
		System.arraycopy(Config.TAB_AMOUNT, 0, player.bank.tabAmounts, 0, Config.TAB_AMOUNT.length);
		player.bank.shift();
		player.message("Spawned! Customs.");
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}
	@Override
	public String description() {
		return "Adds custom items to your bank.";
	}
}
