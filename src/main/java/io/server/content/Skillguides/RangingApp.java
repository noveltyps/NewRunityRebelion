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
public class RangingApp {

	/** Holds all the RangeApplication data. */

	// CHANGE THE ENUM NAME SO "RangeApplication to skillname + application"
	public enum RangeApplication {
	// RANGE0(HEREGOESTHELEVEL REQUIRED, ITEMID)
	Range0(1, new Item(841)), //
	Range1(5, new Item(843)), //
	Range2(20, new Item(849)), //
	Range3(30, new Item(853)), //
	Range4(40, new Item(857)), //
	Range5(50, new Item(861)), //
	Range6(60, new Item(11235)), Range7(65, new Item(12424)), Range8(70, new Item(4214)), Range9(75, new Item(20997)),
	Range11(99, new Item(9757)); // 99CAPE

		private final int level;
		private final Item reward; // item you want to display

		RangeApplication(int level, Item reward) {
			this.level = level;
			this.reward = reward;
		}

		public int getLevel() {
			return level;
		}

		public Item getReward() {
			return reward;
		}

		public static Optional<RangeApplication> forLevel(int level) {
			return Arrays.stream(values()).filter(a -> a.level == level).findAny();
		}

		public static Optional<RangeApplication> forOrdinal(int ordinal) {
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

		if (!RangeApplication.forLevel(skillmenu).isPresent())
			return;

		RangeApplication reward = RangeApplication.forLevel(skillmenu).get();
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
			int size = RangeApplication.values().length;
			Item[] items = new Item[size + 3];
			for (int index = 0, string = 37116; index < size; index++) {
				RangeApplication perk = RangeApplication.forOrdinal(index).get();
				Item item = perk.getReward();
				int amount = player.skillmenuLevel == 0 ? perk.getReward().getAmount()
						: perk.getReward().getAmount() * player.skillmenuLevel;
				items[index + 3] = new Item(item.getId(), amount);
				player.send(new SendString("@whi@Level: <col=3c50b2>" + perk.getLevel(), string++));
				player.send(new SendString("@bla@" + item.getName(), string++));
			}

			player.send(new SendString("Range is a player's power in melee combat.", 37111));
			player.send(new SendString("As a player raises their Range. They can protect themselfs", 37112));
			player.send(new SendString("against an opponent. High Range is favoured in PVM'ing", 37113));
			player.send(new SendString("In order to gain experience in the Range Skill,", 37114));
			player.send(new SendString("players must choose a Range Combat item.", 37115));
			player.send(new SendString("Locations - ::train", 37107));
			player.send(new SendString("Brutal Range Guide", 37103));
			player.send(new SendScrollbar(37110, size * 50));
			player.send(new SendItemOnInterface(37199, items));
			player.interfaceManager.open(37100);
		}
	}
}
