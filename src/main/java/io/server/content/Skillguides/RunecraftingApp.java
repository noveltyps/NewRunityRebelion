package io.server.content.Skillguides;

import java.util.Arrays;
import java.util.Optional;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendScrollbar;
import io.server.net.packet.out.SendString;

/**
 * Created by Arlo.
 */
public class RunecraftingApp {

	/** Holds all the RunecraftApp data. */
	public enum RunecraftApp {
	Runecrafting0(1, new Item(556)), //
	Runecrafting1(2, new Item(555)), //
	Runecrafting2(5, new Item(557)), //
	Runecrafting3(9, new Item(554)), //
	Runecrafting4(14, new Item(559)), //
	Runecrafting5(20, new Item(564)), //
	Runecrafting6(27, new Item(562)), Runecrafting7(35, new Item(11699)), Runecrafting8(40, new Item(11693)),
	Runecrafting9(44, new Item(11695)), Runecrafting10(54, new Item(11692)), Runecrafting11(65, new Item(11697)),
	Runecrafting12(77, new Item(11698)), Runecrafting13(90, new Item(11698)), // This needs to be changed to wrath runes
	Runecrafting14(99, new Item(9766)); // 99CAPE

		private final int level;
		private final Item reward;

		RunecraftApp(int level, Item reward) {
			this.level = level;
			this.reward = reward;
		}

		public int getLevel() {
			return level;
		}

		public Item getReward() {
			return reward;
		}

		public static Optional<RunecraftApp> forLevel(int level) {
			return Arrays.stream(values()).filter(a -> a.level == level).findAny();
		}

		public static Optional<RunecraftApp> forOrdinal(int ordinal) {
			return Arrays.stream(values()).filter(a -> a.ordinal() == ordinal).findAny();
		}
	}

	/** Handles the skillmenu program Item. */
	public static void append(Player player) {
		append(player, 1);
	}

	/** Handles the skillmenu program Itemdisplay. */
	public static void append(Player player, int increment) {
		player.skillmenu += increment;
		//String suffix = increment > 1 ? "s" : "";

		int skillmenu = player.skillmenu;
		boolean levelUp = skillmenu >= 100;

		if (levelUp) {
			player.skillmenuLevel++;
			player.skillmenu = 0;
		}

		if (!RunecraftApp.forLevel(skillmenu).isPresent())
			return;

		RunecraftApp reward = RunecraftApp.forLevel(skillmenu).get();
		int item = reward.getReward().getId();
		int amount = reward.getReward().getAmount() * player.skillmenuLevel;
		//String name = ItemDefinition.get(item).getName();
		player.inventory.addOrDrop(new Item(item, amount));

	}

	/** Opens the skillmenu itemcontainer. */
	public static void open(Player player) {
		if (player.getCombat().inCombat()) {
			player.dialogueFactory.sendStatement("You can not open a Skill Guide while in combat!").execute();
			return;
		} else {
			int size = RunecraftApp.values().length;
			Item[] items = new Item[size + 3];
			for (int index = 0, string = 37116; index < size; index++) {
				RunecraftApp perk = RunecraftApp.forOrdinal(index).get();
				Item item = perk.getReward();
				int amount = player.skillmenuLevel == 0 ? perk.getReward().getAmount()
						: perk.getReward().getAmount() * player.skillmenuLevel;
				items[index + 3] = new Item(item.getId(), amount);
				player.send(new SendString("@whi@Level: <col=3c50b2>" + perk.getLevel(), string++));
				player.send(new SendString("@bla@" + item.getName(), string++));
			}

			player.send(new SendString("Runecrafting is an easy way to get runes.", 37111));
			player.send(
					new SendString("As a player raises their Runecrafting. They can create higher tier runes.", 37112));
			player.send(new SendString("Runecrafting is also a nice money making method.", 37113));
			player.send(new SendString("In order to gain experience in the Runecrafting Skill,", 37114));
			player.send(new SendString("players must infuse their Rune essence on an altar.", 37115));
			player.send(new SendString("Locations - ::train", 37107));
			player.send(new SendString("Brutal Runecrafting Guide", 37103));
			player.send(new SendScrollbar(37110, size * 50));
			player.send(new SendItemOnInterface(37199, items));
			player.interfaceManager.open(37100);
		}
	}
}
