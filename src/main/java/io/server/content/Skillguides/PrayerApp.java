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
public class PrayerApp {

	// remember to edit the strings
	// remember to edit the levels

	/** Holds all the PrayerApplication data. */
	public enum PrayerApplication {
	// RANGE0(HEREGOESTHELEVEL REQUIRED, ITEMID)
	Prayer0(1, new Item(841)), //
	Prayer1(30, new Item(1183)), //
	Prayer2(40, new Item(1185)), //
	Prayer3(55, new Item(10828)), //
	Prayer4(60, new Item(1187)), //
	Prayer5(65, new Item(10348)), //
	Prayer6(65, new Item(11832)), Prayer7(70, new Item(4720)), Prayer8(70, new Item(12831)),
	Prayer9(75, new Item(11283)), Prayer10(75, new Item(21015)), Prayer11(99, new Item(9754)); // 99CAPE

		private final int level;
		private final Item reward; // item you want to display

		PrayerApplication(int level, Item reward) {
			this.level = level;
			this.reward = reward;
		}

		public int getLevel() {
			return level;
		}

		public Item getReward() {
			return reward;
		}

		public static Optional<PrayerApplication> forLevel(int level) {
			return Arrays.stream(values()).filter(a -> a.level == level).findAny();
		}

		public static Optional<PrayerApplication> forOrdinal(int ordinal) {
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

		if (!PrayerApplication.forLevel(skillmenu).isPresent())
			return;

		PrayerApplication reward = PrayerApplication.forLevel(skillmenu).get();
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
			int size = PrayerApplication.values().length;
			Item[] items = new Item[size + 3];
			for (int index = 0, string = 37116; index < size; index++) {
				PrayerApplication perk = PrayerApplication.forOrdinal(index).get();
				Item item = perk.getReward();
				int amount = player.skillmenuLevel == 0 ? perk.getReward().getAmount()
						: perk.getReward().getAmount() * player.skillmenuLevel;
				items[index + 3] = new Item(item.getId(), amount);
				player.send(new SendString("@whi@Level: <col=3c50b2>" + perk.getLevel(), string++));
				player.send(new SendString("@bla@" + item.getName(), string++));
			}

			player.send(new SendString("Prayer is a player's power in all combat.", 37111));
			player.send(new SendString("As a player raises their Prayer. They can protect themself", 37112));
			player.send(new SendString("against an opponent. High Prayer is favoured in combat.", 37113));
			player.send(new SendString("In order to gain experience in the Prayer Skill,", 37114));
			player.send(new SendString("players must use bones on the alter located at home.", 37115));
			player.send(new SendString("Locations - ::home", 37107));
			player.send(new SendString("RebelionX Prayer Guide", 37103));
			player.send(new SendScrollbar(37110, size * 50));
			player.send(new SendItemOnInterface(37199, items));
			player.interfaceManager.open(37100);
		}
	}
}
