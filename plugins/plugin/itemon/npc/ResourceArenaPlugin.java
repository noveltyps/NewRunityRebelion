package plugin.itemon.npc;

import io.server.game.event.impl.ItemOnNpcEvent;
import io.server.game.event.impl.NpcClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.util.Utility;

public class ResourceArenaPlugin extends PluginContext {

	@Override
	protected boolean firstClickNpc(Player player, NpcClickEvent event) {
		if (event.getNpc().id != 6599) {
			return false;
		}

		player.dialogueFactory.sendNpcChat(6599, "Hello, #name!", " Welcome to the wilderness resource arena!",
				"Any items that you obtain here I can note it for", "you for a small fee! Just use the item on me.")
				.execute();
		return true;
	}

	@Override
	protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
		if (event.getNpc().id != 6599) {
			return false;
		}

		Item item = event.getUsed();

		/*
		 * if (!SkillRepository.isSkillingItem(item.getId())) {
		 * player.dialogueFactory.sendNpcChat(6599,
		 * "You can only note skilling items obtained from", "this area.").execute();
		 * return true; }
		 */

		if (item.isNoted()) {
			// player.dialogueFactory.sendNpcChat(6599, "I do not un-note items, I only note
			// them!").execute();
			item.unnoted();
			return true;
		}

		int itemAmount = player.inventory.computeAmountForId(item.getId());
		int cost = (int) Math.round(itemAmount / 4.0D) * 250;

		player.dialogueFactory.sendNpcChat(6599, "It is going to cost you " + Utility.formatDigits(cost) + " coins",
				"for me to note " + itemAmount + " " + item.getName() + ".").sendOption("Proceed", () -> {
					if (!player.inventory.contains(995, cost)) {
						player.dialogueFactory.sendNpcChat(6599, "You do not have enough coins to do this!");
						return;
					}

					player.inventory.remove(995, cost);
					player.inventory.remove(item.getId(), itemAmount);
					player.inventory.add(item.getNotedId(), itemAmount);
					player.dialogueFactory.sendNpcChat(6599,
							"You have successfully exchanged " + itemAmount + " " + item.getName() + "!");
				}, "Nevermind", () -> player.dialogueFactory.clear());

		player.dialogueFactory.execute();
		return true;
	}
}
