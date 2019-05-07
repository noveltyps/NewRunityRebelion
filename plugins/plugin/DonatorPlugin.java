package plugin;

import io.server.content.dialogue.DialogueFactory;
import io.server.content.donators.DonatorBond;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.position.Area;

/**
 * Handles the donator plugin.
 *
 * @author Daniel.
 * @author Adam_#6723
 */
public class DonatorPlugin extends PluginContext {

	@Override
	protected boolean firstClickItem(Player player, ItemClickEvent event) {
		DonatorBond bond = DonatorBond.forId(event.getItem().getId());

		if (bond == null)
			return false;

		DialogueFactory factory = player.dialogueFactory;

		if (Area.inWilderness(player)) {
			factory.sendStatement("You can not be in the wilderness to redeem a bond!").execute();
			return true;
		}

		if (player.getCombat().inCombat()) {
			factory.sendStatement("You can not be in combat to redeem a bond!").execute();
			return true;
		}

		factory.sendStatement("Are you sure you want to redeem this <col=255>" + event.getItem().getName() + "</col>?",
				"There is no going back!");
		factory.sendOption("Yes, redeem <col=255>" + event.getItem().getName() + "</col>!",
				() -> redeem(player, event.getItem(), bond), "Nevrmind", factory::clear);
		factory.execute();
		return true;
	}

	private void redeem(Player player, Item item, DonatorBond bond) {
		if (!player.inventory.contains(item.getId(), 1))
			return;

		player.inventory.remove(item.getId(), 1);
		player.donation.redeem(bond);
	}
}
