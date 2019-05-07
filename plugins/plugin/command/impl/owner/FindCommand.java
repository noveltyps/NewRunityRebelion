package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.items.containers.ItemContainer;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendScrollbar;
import io.server.net.packet.out.SendString;

/**
 * @author Adam_#6723
 */

public class FindCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		if(!PlayerRight.isDeveloper(player)) {
			return;
		}
		
		final String name = String.format(parts[1]);

		ItemContainer container = new ItemContainer(400, ItemContainer.StackPolicy.ALWAYS);
		int randomint = 0;
		for (final ItemDefinition itemNAME : ItemDefinition.DEFINITIONS) {
			if (itemNAME == null || itemNAME.getName() == null || itemNAME.isNoted())
				continue;
			if (itemNAME.getName().toLowerCase().trim().contains(name)) {
				container.add(new Item(itemNAME.getId()));
				randomint++;
				if (randomint >= 400)
					break;
			}
		}
		player.send(new SendString("Search: <col=FF5500>" + name, 37506));
		player.send(new SendString(
				String.format("Found <col=FF5500>%s</col> item%s", randomint, randomint != 1 ? "s" : ""), 37507));
		player.send(new SendScrollbar(37520, randomint / 8 * 52 + ((randomint % 8) == 0 ? 0 : 52)));
		player.send(new SendItemOnInterface(37521, container.getItems()));
		player.interfaceManager.open(37500);
		player.send(new SendMessage(
				String.format("Found %s item%s containing the key '%s'.", randomint, randomint != 1 ? "s" : "", name)));

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player) || PlayerRight.isGambleManager(player);
	}

	@Override
	public String description() {
		return "Finds the specified item in the spawn administration interface.";
	}
}
