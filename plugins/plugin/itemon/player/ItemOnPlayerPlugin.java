package plugin.itemon.player;

import io.server.game.event.impl.ItemOnPlayerEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class ItemOnPlayerPlugin extends PluginContext {

	@Override
	protected boolean itemOnPlayer(Player player, ItemOnPlayerEvent event) {

		Player other = event.getOther();

		switch (event.getUsed().getId()) {
		/* Slayer Gem (Enchanted Gem) */
		case 4155:
			player.slayer.startDuoDialogue(player, other);
			return true;
		}

		return false;
	}

}
