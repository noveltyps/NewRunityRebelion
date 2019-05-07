package plugin.click.item;

import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.teleport.TeleportTablet;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class TeletabPlugin extends PluginContext {

	@Override
	protected boolean firstClickItem(Player player, ItemClickEvent event) {
		if (!TeleportTablet.forId(event.getItem().getId()).isPresent()) {
			return false;
		}
		
		if(player.wilderness >= 35) {
			player.send(new SendMessage("You can't teleport above 35 wilderness with a tablet."));
			return false;
		}

		final TeleportTablet tablet = TeleportTablet.forId(event.getItem().getId()).get();

		if (player.house.isInside()) {
			player.send(new SendMessage("Please leave the house before teleporting."));
			return true;
		}

		player.inventory.remove(new Item(event.getItem().getId(), 1));
		player.send(
				new SendMessage("You have broken the " + Utility.formatEnum(tablet.name()) + " and were teleported."));
		Teleportation.teleport(player, tablet.getPosition(), Teleportation.TeleportationData.TABLET, () -> {
		});
		return true;
	}

}
