package plugin.command.impl.donator;

import io.server.content.command.Command;
import io.server.content.store.Store;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Area;

/**
 * @author Adam_#6723
 */

public class SponsorStoreCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		if (Area.inWilderness(player) || player.getCombat().inCombat()) {
			player.message("You cannot open the store in the wilderness.");
		} else {
			Store.STORES.get("Sponsor Store").open(player);
		}
	}

	@Override
	public boolean canUse(Player player) {
		return (PlayerRight.isSupreme(player) ||  PlayerRight.isKing(player) || PlayerRight.isDeveloper(player));

	}

	@Override
	public String description() {
		return "Opens the Sponsor store.";
	}
}
