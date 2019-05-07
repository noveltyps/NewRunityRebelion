package plugin.itemcontainer;

import io.server.content.upgrading.UpgradeDisplay;
import io.server.game.event.impl.ItemContainerContextMenuEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;

public class UpgradeContainerPlugin extends PluginContext {

	@Override
	protected boolean firstClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		if (event.getInterfaceId() == 48275) {
			Item item = new Item(event.getRemoveId());
			new UpgradeDisplay(player, item).select();
			return true;
		}
		return false;
	}


}
