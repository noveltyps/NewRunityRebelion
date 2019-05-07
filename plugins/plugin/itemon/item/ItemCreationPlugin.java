package plugin.itemon.item;

import java.util.Arrays;

import io.server.content.ItemCreation;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.skill.impl.slayer.SlayerUnlockable;
import io.server.game.event.impl.ItemOnItemEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class ItemCreationPlugin extends PluginContext {

	@Override
	protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
		Item used = event.getUsed();
		Item with = event.getWith();

		if ((used.getId() == 233 || with.getId() == 233) && (with.getId() == 5075 || with.getId() == 5075)) {
			player.inventory.remove(5075, 1);
			player.inventory.add(6693, 1);
			player.skills.addExperience(Skill.CRAFTING, 150);
			player.message("You have crushed up the birds nest.");
			return true;
		}

		/** Dragon skirt (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 12416 || with.getId() == 12416)) {
			player.inventory.remove(12416, 1);
			player.inventory.add(4585, 1);
			player.inventory.add(12536, 1);
			player.message("@red@You have distmantled a Dragon Skirt (OR)");
			return true;
		}
		
		/** AFK Logs message**/
		if ((used.getId() == 2862 || with.getId() == 2862) && (with.getId() == 590 || with.getId() == 590)) {
			player.message("@red@Use the log on the bonfire at ::skill to burn this log!");
			return true;
		}
		/** AFK Logs message**/
		if ((used.getId() == 590 || with.getId() == 590) && (with.getId() == 2862 || with.getId() == 2862)) {
			player.message("@red@Use the log on the bonfire at ::skill to burn this log!");
			return true;
		}

		/** Dragon Defender (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 19722 || with.getId() == 19722)) {
			player.inventory.remove(19722, 1);
			player.inventory.add(12954, 1);
			player.inventory.add(20143, 1);
			player.message("@red@You have distmantled a Dragon Defender (OR)");
			return true;
		}

		/** torture amulet (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 20366 || with.getId() == 20366)) {
			player.inventory.remove(20366, 1);
			player.inventory.add(19553, 1);
			player.inventory.add(20062, 1);
			player.message("@red@You have distmantled a Amulet of torture (OR)");
			return true;
		}

		/** occult amulet (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 19720 || with.getId() == 19720)) {
			player.inventory.remove(19720, 1);
			player.inventory.add(12002, 1);
			player.inventory.add(20065, 1);
			player.message("@red@You have distmantled a Amulet of occult (OR)");
			return true;
		}

		/** Armadyl God (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 20368 || with.getId() == 20368)) {
			player.inventory.remove(20368, 1);
			player.inventory.add(11802, 1);
			player.inventory.add(20068, 1);
			player.message("@red@You have distmantled a Armadyl Godsword (OR)");
			return true;
		}

		/** Bandos God (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 20370 || with.getId() == 20370)) {
			player.inventory.remove(20370, 1);
			player.inventory.add(11804, 1);
			player.inventory.add(20071, 1);
			player.message("@red@You have distmantled a Bandos Godsword (OR)");
			return true;
		}
		/** Saradomin God (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 20372 || with.getId() == 20372)) {
			player.inventory.remove(20372, 1);
			player.inventory.add(11806, 1);
			player.inventory.add(20074, 1);
			player.message("@red@You have distmantled a dragon Saradomin Godsword (OR)");
			return true;
		}

		/** Zamorak God (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 20374 || with.getId() == 20374)) {
			player.inventory.remove(20374, 1);
			player.inventory.add(11808, 1);
			player.inventory.add(20077, 1);
			player.message("@red@You have distmantled a dragon Zamorak Godsword (OR)");
			return true;
		}

		/** Dragon Scimitar (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 20000 || with.getId() == 20000)) {
			player.inventory.remove(20000, 1);
			player.inventory.add(4587, 1);
			player.inventory.add(20002, 1);
			player.message("@red@You have distmantled a dragon Scimitar (or)");
			return true;
		}

		/** Dragon full helm (OR) Kit Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 12417 || with.getId() == 12417)) {
			player.inventory.remove(12417, 1);
			player.inventory.add(11335, 1);
			player.inventory.add(12538, 1);
			player.message("@red@You have distmantled a dragon full helm (or)");
			return true;
		}

		/** Dragon platelegs (OR) Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 12415 || with.getId() == 12415)) {
			player.inventory.remove(12415, 1);
			player.inventory.add(4087, 1);
			player.inventory.add(12536, 1);
			player.message("@red@You have distmantled a dragon platelegs (or)");
			return true;
		}

		/** Dragon chainbody (OR) Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 12414 || with.getId() == 12414)) {
			player.inventory.remove(12414, 1);
			player.inventory.add(3140, 1);
			player.inventory.add(12534, 1);
			player.message("@red@You have distmantled a dragon chainbody (or)");
			return true;
		}

		/** Dragon SQ Shield (OR) Dismantle **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 12418 || with.getId() == 12418)) {
			player.inventory.remove(12418, 1);
			player.inventory.add(1187, 1);
			player.inventory.add(12532, 1);
			player.message("@red@You have distmantled a dragon SQ Shield!");
			return true;
		}

		/** Fury (OR) **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 12436 || with.getId() == 12436)) {
			player.inventory.remove(12436, 1);
			player.inventory.add(6585, 1);
			player.inventory.add(12526, 1);
			player.message("@red@You have distmantled a fury!");
			return true;
		}

		/** Armadyl God sword! **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 11802 || with.getId() == 11802)) {
			player.inventory.remove(11802, 1);
			player.inventory.add(11818, 1);
			player.inventory.add(11820, 1);
			player.inventory.add(11822, 1);
			player.inventory.add(11810, 1);
			player.message("@red@You have broken the godsword into tiny pieces.");
			return true;
		}

		/** Bandos God sword! **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 11804 || with.getId() == 11804)) {
			player.inventory.remove(11804, 1);
			player.inventory.add(11818, 1);
			player.inventory.add(11820, 1);
			player.inventory.add(11822, 1);
			player.inventory.add(11812, 1);
			player.message("@red@You have broken the godsword into tiny pieces.");
			return true;
		}
		/** Saradomin God sword! **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 11806 || with.getId() == 11806)) {
			player.inventory.remove(11806, 1);
			player.inventory.add(11818, 1);
			player.inventory.add(11820, 1);
			player.inventory.add(11822, 1);
			player.inventory.add(11814, 1);
			player.message("@red@You have broken the godsword into tiny pieces.");
			return true;
		}

		/** zamorak God sword! **/
		if ((used.getId() == 1755 || with.getId() == 1755) && (with.getId() == 11808 || with.getId() == 11808)) {
			player.inventory.remove(11808, 1);
			player.inventory.add(11818, 1);
			player.inventory.add(11820, 1);
			player.inventory.add(11822, 1);
			player.inventory.add(11816, 1);
			player.message("@red@You have broken the godsword into tiny pieces.");
			return true;
		}

		/** Blessed Spirit Shield Creation **/
		/*if ((used.getId() == 12833 || with.getId() == 12833) && (with.getId() == 12829 || with.getId() == 12829)) {
			player.inventory.remove(12833, 1);
			player.inventory.remove(12829, 1);
			player.inventory.add(12831, 1);
			player.message("@red@You have created a blessed spirit shield!");
			return true;
		}*/

		/** Elysian Spirit Shield **/
		if ((used.getId() == 12831 || with.getId() == 12831) && (with.getId() == 12819 || with.getId() == 12819)) {
			player.inventory.remove(12831, 1);
			player.inventory.remove(12819, 1);
			player.inventory.add(12817, 1);
			player.message("@red@You have created a Elysian Spirit Shield!");
			return true;
		}
		
		
		/** Staff of light creation **/
		if ((used.getId() == 13256 || with.getId() == 13256) && (with.getId() == 11791 || with.getId() == 11791)) {
			player.inventory.remove(13256, 1);
			player.inventory.remove(11791, 1);
			player.inventory.add(22296, 1);
			player.message("@red@You have created a Staff of light!");
			return true;
		}
		
		/** Spectral Spirit Shield **/
		if ((used.getId() == 12831 || with.getId() == 12831) && (with.getId() == 12823 || with.getId() == 12823)) {
			player.inventory.remove(12831, 1);
			player.inventory.remove(12823, 1);
			player.inventory.add(12821, 1);
			player.message("@red@You have created a Spectral Spirit Shield!");
			return true;
		}
		/** Arcane Spirit Shield **/
		if ((used.getId() == 12831 || with.getId() == 12831) && (with.getId() == 12827 || with.getId() == 12827)) {
			player.inventory.remove(12831, 1);
			player.inventory.remove(12827, 1);
			player.inventory.add(12825, 1);
			player.message("@red@You have created a Arcane Spirit Shield!");
			return true;
		}

		/** End of spirit shield handler/creation **/

		if ((used.getId() == 233 || with.getId() == 233) && (with.getId() == 237 || with.getId() == 237)) {
			player.inventory.remove(237, 1);
			player.inventory.add(235, 1);
			player.skills.addExperience(Skill.CRAFTING, 150);
			player.message("You have crushed up the unicorn horn.");
			return true;
		}
		// adam
		/*if ((used.getId() == 12831 || with.getId() == 12831) && (with.getId() == 10028 || with.getId() == 10028)) {
			player.inventory.remove(12831, 1);
			player.inventory.remove(12827, 1);
			player.inventory.add(12825, 1);
			player.inventory.add(5020, 2147000);
			player.message("@red@You have merged both items!!");
			return true;
		}*/

		if (!ItemCreation.CreationData.forItems(used, with).isPresent()) {
			return false;
		}

		final ItemCreation.CreationData creation = ItemCreation.CreationData.forItems(event.getUsed(), event.getWith())
				.get();
		final String name = creation.product[0].getName();

		if (!player.inventory.containsAll(creation.required)) {
			player.send(new SendMessage(
					"You do not have the required items to make " + Utility.getAOrAn(name) + " " + name + "."));
			return true;
		}

		if (creation.level != null) {
			for (Skill skill : creation.level) {
				if (player.skills.getMaxLevel(skill.getSkill()) < skill.getLevel()) {
					String skillName = Skill.getName(skill.getSkill());
					player.message("You need " + Utility.getAOrAn(skillName) + " " + skillName + " level of "
							+ skill.getLevel() + " to do this!");
					return true;
				}
			}
		}

		if (creation == ItemCreation.CreationData.SLAYER_HELM
				&& !player.slayer.getUnlocked().contains(SlayerUnlockable.SLAYER_HELM)) {
			player.message("You need to unlock this ability first!");
			return true;
		}

		final DialogueFactory factory = player.dialogueFactory;

		factory.sendItem(name, "Are you sure you want to do this?", creation.product[0]).sendOption("Yes", () -> {
			Arrays.stream(creation.required).forEach(item -> {
				if (item.getId() == 1755 || item.getId() == 2347)
					return;
				player.inventory.remove(item);
			});

			factory.clear();
			player.inventory.addAll(creation.product);
			player.send(new SendMessage("You successfully create " + Utility.getAOrAn(name) + " " + name + "."));
			if (creation.level != null) {
				for (Skill skill : creation.level) {
					player.skills.addExperience(skill.getSkill(), skill.getExperience());
				}
			}
		}, "No", factory::clear).execute();
		return true;
	}

}
