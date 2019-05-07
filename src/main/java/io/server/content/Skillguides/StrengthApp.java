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
public class StrengthApp {

	/** Holds all the StrengthApplication data. */
	public enum StrengthApplication {
	Strength0(1, new Item(1413)), // Halberd
	Strength1(40, new Item(10887)), // BARRELCHEST
	Strength2(50, new Item(12848)), // GMAUL
	Strength5(60, new Item(6528)), // Tzhaar
	Strength10(70, new Item(4718)), // DHAROK AXE
	Strength20(70, new Item(13263)), // ABBYSAL BLUDGE
	Strength30(75, new Item(21003)), // ELDER MAUL
	// Strength40(75, new Item(21003)), //ELDER MAUL
	Strength99(99, new Item(9751)); // 99CAPE

		private final int level;
		private final Item reward;

		StrengthApplication(int level, Item reward) {
			this.level = level;
			this.reward = reward;
		}

		public int getLevel() {
			return level;
		}

		public Item getReward() {
			return reward;
		}

		public static Optional<StrengthApplication> forLevel(int level) {
			return Arrays.stream(values()).filter(a -> a.level == level).findAny();
		}

		public static Optional<StrengthApplication> forOrdinal(int ordinal) {
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

		if (!StrengthApplication.forLevel(skillmenu).isPresent())
			return;

		StrengthApplication reward = StrengthApplication.forLevel(skillmenu).get();
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
			int size = StrengthApplication.values().length;
			Item[] items = new Item[size + 3];
			for (int index = 0, string = 37116; index < size; index++) {
				StrengthApplication perk = StrengthApplication.forOrdinal(index).get();
				Item item = perk.getReward();
				int amount = player.skillmenuLevel == 0 ? perk.getReward().getAmount()
						: perk.getReward().getAmount() * player.skillmenuLevel;
				items[index + 3] = new Item(item.getId(), amount);
				player.send(new SendString("@whi@Level: <col=3c50b2>" + perk.getLevel(), string++));
				player.send(new SendString("@bla@" + item.getName(), string++));
			}

			player.send(new SendString("Strength is a player's power in melee combat.", 37111));
			player.send(new SendString("As a player raises their Strength. They can deal damage more", 37112));
			player.send(new SendString("against an opponent. High strength is favoured in PKing", 37113));
			player.send(new SendString("In order to gain experience in the Strength stat,", 37114));
			player.send(new SendString(" players must choose the Aggresive Combat style.", 37115));
			player.send(new SendString("Locations - ::train", 37107));
			player.send(new SendString("Brutal Strength Guide", 37103));
			player.send(new SendScrollbar(37110, size * 50));
			player.send(new SendItemOnInterface(37199, items));
			player.interfaceManager.open(37100);
		}
	}
}
