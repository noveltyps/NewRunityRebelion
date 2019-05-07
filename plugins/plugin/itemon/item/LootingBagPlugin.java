package plugin.itemon.item;

import io.server.game.event.impl.ItemOnItemEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;

public class LootingBagPlugin extends PluginContext {

	@Override
	protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
		final Item used = event.getUsed();
		final Item with = event.getWith();
		if (!(used.getId() == 11941 || with.getId() == 11941)) {
			return false;
		}

		if (used.getId() == 11941) {
			if (with.getAmount() == 1) {
				player.lootingBag.deposit(with, with.getAmount());
				return true;
			}
			player.lootingBag.depositMenu(with);
			return true;
		}

		if (with.getId() == 11941) {
			if (used.getAmount() == 1) {
				player.lootingBag.deposit(used, used.getAmount());
				return true;
			}
			player.lootingBag.depositMenu(used);
			return true;
		}
		return false;
	}

}
