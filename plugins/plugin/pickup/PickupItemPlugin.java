package plugin.pickup;

import io.server.game.event.impl.PickupItemEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;

/**
 * Adam_#6723
 * 
 */
public class PickupItemPlugin extends PluginContext {

	@Override
	protected boolean onPickupItem(Player player, PickupItemEvent event) {
		if (player.right != PlayerRight.IRONMAN) {
			Item[] ironman_armour = new Item[] { new Item(12810), new Item(12811), new Item(12812) };
			for (Item item : ironman_armour) {
				if (event.getItem().matchesId(item.getId())) {
					return false;
				}
			}
		} else if (player.right != PlayerRight.ULTIMATE_IRONMAN) {
			Item[] ulitmate_ironman_armour = new Item[] { new Item(12813), new Item(12814), new Item(12815) };
			for (Item item : ulitmate_ironman_armour) {
				if (event.getItem().matchesId(item.getId())) {
					return false;
				}
			}
		} else if (player.right != PlayerRight.HARDCORE_IRONMAN) {
			Item[] hardcore_ironman_armour = new Item[] { new Item(20792), new Item(20794), new Item(20796) };
			for (Item item : hardcore_ironman_armour) {
				if (event.getItem().matchesId(item.getId())) {
					return false;
				}
			}
		}
		return super.onPickupItem(player, event);
	}
}