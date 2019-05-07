package plugin.click.button;

import io.server.content.store.Store;
import io.server.content.store.impl.PersonalStore;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class PersonalStoreButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		switch (button) {
		case -27321:
			PersonalStore.openPanel(player);
			return true;
		case -27318:
			player.personalStore.claimCoins(player);
			return true;
		case -27315:
			player.interfaceManager.open(38300);
			return true;
		case -27312:
			PersonalStore.myShop(player);
			return true;

		case -27226:
			PersonalStore.openMenu(player);
			return true;
		}
		boolean featured = button >= -12600 && button <= -12528;
		PersonalStore shop = featured ? PersonalStore.FEATURED_SHOPS.get(button) : player.viewing_shops.get(button / 3 + 4168);
		if (shop == null)
			return false;
		if (!Store.PERSONAL_STORES.containsKey(shop.name))
			return false;
		shop.open(player);
		return true;
	}
}
