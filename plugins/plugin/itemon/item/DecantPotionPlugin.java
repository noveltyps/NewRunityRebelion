package plugin.itemon.item;

import io.server.Config;
import io.server.content.clanchannel.content.ClanTaskKey;
import io.server.content.consume.PotionData;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.game.event.impl.ItemOnItemEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.net.packet.out.SendMessage;

public class DecantPotionPlugin extends PluginContext {

	private final static int[] COMBAT_POTION_ITEMS = { 2436, 2440, 2442, 269 };

	private void makeCombatPotion(Player player) {
		if (player.skills.getMaxLevel(Skill.HERBLORE) < 90) {
			player.message("You need a Herblore level of 90 to do this!");
			return;
		}

		if (!player.inventory.containsAll(COMBAT_POTION_ITEMS)) {
			player.message("You need a clean torstol and all 3 super combat potions.");
			return;
		}

		for (int potion : COMBAT_POTION_ITEMS) {
			player.inventory.remove(potion, 1);
		}

		player.inventory.add(12695, 1);
		player.skills.addExperience(Skill.HERBLORE,
				(150 * Config.HERBLORE_MODIFICATION) * new ExperienceModifier(player).getModifier());
		player.forClan(channel -> channel.activateTask(ClanTaskKey.SUPER_COMBAT_POTION, player.getName()));
	}

	@Override
	protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
		final Item used = event.getUsed();
		//final int usedSlot = event.getUsedSlot();
		final Item with = event.getWith();
		final int withSlot = event.getWithSlot();

		for (int potion : COMBAT_POTION_ITEMS) {
			if (used.getId() == potion || with.getId() == potion) {
				makeCombatPotion(player);
				return true;
			}
		}

		final PotionData first = PotionData.forId(used.getId()).orElse(null);

		if (first == null) {
			return false;
		}

		if (with.getId() == 229) {
			handlePotionOnVial(player, used, with, first, withSlot);
			return true;
		}

		final PotionData second = PotionData.forId(with.getId()).orElse(null);

		if (second == null) {
			return false;
		}

		if (first != second) {
			player.send(new SendMessage("You can't mix two different types of potions."));
			return true;
		}

		if (first.getIds()[0] == used.getId() || second.getIds()[0] == with.getId()) {
			player.send(new SendMessage("You can't combine these potions as one of them is already full."));
			return true;
		}

		int doses = getDoses(used.getId()) + getDoses(with.getId());
		int remainder = doses > 4 ? doses % 4 : 0;
		doses -= remainder;

		player.inventory.replace(with.getId(), first.getIdForDose(doses), withSlot, false);

		if (remainder > 0) {
			player.inventory.replace(used, new Item(first.getIdForDose(remainder)), false);
		} else {
			player.inventory.replace(used, new Item(229), false);
		}

		player.inventory.refresh();
		player.send(new SendMessage("You carefully combine the potions."));
		return true;
	}

	private void handlePotionOnVial(Player player, Item used, Item with, PotionData first, int withSlot) {
		int doses = getDoses(used.getId());
		int remainder = doses % 4 / 2;
		doses -= remainder;

		player.inventory.replace(with.getId(), first.getIdForDose(doses), withSlot, false);

		if (remainder > 0) {
			player.inventory.replace(used, new Item(first.getIdForDose(remainder)), false);
		} else {
			player.inventory.replace(used, new Item(229), false);
		}

		player.inventory.refresh();
		player.send(new SendMessage("You carefully combine the potions."));
	}

	private static int getDoses(int id) {
		ItemDefinition definition = ItemDefinition.get(id);
		int index = definition.getName().lastIndexOf(')');
		return Integer.valueOf(definition.getName().substring(index - 1, index));
	}

}
