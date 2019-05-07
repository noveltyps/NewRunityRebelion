package plugin.itemon.object;

import io.server.game.event.impl.ItemOnObjectEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class ConstructionToolKitPlugin extends PluginContext {

	@Override
	protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
		return event.getUsed().getId() == 1 && player.house.toolkit(event.getObject());
	}

}
