package plugin.click.item;

import io.server.content.consume.PotionData;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class EmptyPotionPlugin extends PluginContext {

	@Override
	protected boolean thirdClickItem(Player player, ItemClickEvent event) {
		if (PotionData.forId(event.getItem().getId()).isPresent()) {
			player.inventory.replace(event.getItem().getId(), 229, true);
			player.send(
					new SendMessage("You have poured out the remaining dose(s) of " + event.getItem().getName() + "."));
			return true;
		}
		return false;
	}

}
