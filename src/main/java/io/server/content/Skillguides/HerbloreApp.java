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
public class HerbloreApp {

	/** Holds all the HerbloreApplication data. */
	public enum HerbloreApplication {
	Herblore0(3, new Item(249)), //
	Herblore1(3, new Item(1534)), //
	Herblore2(3, new Item(1526)), //
	Herblore3(5, new Item(251)), //
	Herblore4(11, new Item(253)), //
	Herblore5(20, new Item(257)), //
	Herblore6(25, new Item(255)), //
	Herblore7(30, new Item(2998)), Herblore8(40, new Item(259)), Herblore9(48, new Item(261)),
	Herblore10(54, new Item(263)), Herblore11(59, new Item(3000)), Herblore12(65, new Item(265)),
	Herblore13(67, new Item(2481)), Herblore14(70, new Item(267)), Herblore15(75, new Item(269)),
	Herblore16(99, new Item(9775)); // 99CAPE

		private final int level;
		private final Item reward;

		HerbloreApplication(int level, Item reward) {
			this.level = level;
			this.reward = reward;
		}

		public int getLevel() {
			return level;
		}

		public Item getReward() {
			return reward;
		}

		public static Optional<HerbloreApplication> forLevel(int level) {
			return Arrays.stream(values()).filter(a -> a.level == level).findAny();
		}

		public static Optional<HerbloreApplication> forOrdinal(int ordinal) {
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

		if (!HerbloreApplication.forLevel(skillmenu).isPresent())
			return;

		HerbloreApplication reward = HerbloreApplication.forLevel(skillmenu).get();
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
			int size = HerbloreApplication.values().length;
			Item[] items = new Item[size + 3];
			for (int index = 0, string = 37116; index < size; index++) {
				HerbloreApplication perk = HerbloreApplication.forOrdinal(index).get();
				Item item = perk.getReward();
				int amount = player.skillmenuLevel == 0 ? perk.getReward().getAmount()
						: perk.getReward().getAmount() * player.skillmenuLevel;
				items[index + 3] = new Item(item.getId(), amount);
				player.send(new SendString("@whi@Level: <col=3c50b2>" + perk.getLevel(), string++));
				player.send(new SendString("@bla@" + item.getName(), string++));
			}

			player.send(new SendString("Herblore gives a player the option to create potions.", 37111));
			player.send(new SendString("As a player raises their Herblore. They can create high tier potions", 37112));
			player.send(new SendString("Herblore is also a good money making method.", 37113));
			player.send(new SendString("In order to gain experience in the Herblore Skill,", 37114));
			player.send(new SendString("players must mix herbs and other items together.", 37115));
			player.send(new SendString("Check the forums for the Herblore guide.", 37107));
			player.send(new SendString("Brutal Herblore Guide", 37103));
			player.send(new SendScrollbar(37110, size * 50));
			player.send(new SendItemOnInterface(37199, items));
			player.interfaceManager.open(37100);
		}
	}
}
