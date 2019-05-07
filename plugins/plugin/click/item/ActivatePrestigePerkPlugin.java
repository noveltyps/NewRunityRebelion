package plugin.click.item;

import java.util.HashSet;

import io.server.content.prestige.PrestigePerk;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

public class ActivatePrestigePerkPlugin extends PluginContext {

	@Override
	protected boolean firstClickItem(Player player, ItemClickEvent event) {
		if (!PrestigePerk.forItem(event.getItem().getId()).isPresent()) {
			return false;
		}

		final PrestigePerk perk = PrestigePerk.forItem(event.getItem().getId()).get();

		if (player.prestige.activePerks == null) {
			player.prestige.activePerks = new HashSet<>();
		}

		if (player.prestige.activePerks.contains(perk)) {
			player.send(new SendMessage("The Perk: " + perk.name + " perk is already active on your account!",
					MessageColor.DARK_BLUE));
			return true;
		}

		player.inventory.remove(event.getItem());
		player.prestige.activePerks.add(perk);
		player.send(
				new SendMessage("You have successfully activated the " + perk.name + " perk.", MessageColor.DARK_BLUE));
		return true;
	}

}
