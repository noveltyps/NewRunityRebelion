package plugin.click.item;

import io.server.game.event.impl.DropItemEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class DissolveTentaclePlugin extends PluginContext {

	@Override
	protected boolean onDropItem(Player player, DropItemEvent event) {
		if (!event.getItem().matchesId(12006)) {
			return false;
		}

		if (player.inventory.getFreeSlots() <= 0) {
			player.message("You need some more inventory space to do that.");
		} else {
			player.inventory.replace(12006, 4151, true);
			player.inventory.add(12004, 1);
			player.message("You dissolve your Abyssal tentacle.");
		}
		return true;
	}

}
