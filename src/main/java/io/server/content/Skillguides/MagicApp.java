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
public class MagicApp {

	/** Holds all the MagicApplication data. */
	public enum MagicApplication {
	Magic0(20, new Item(2579)), //
	Magic1(20, new Item(13385)), //
	Magic2(40, new Item(20517)), //
	Magic3(40, new Item(4091)), //
	Magic4(40, new Item(7399)), //
	Magic5(40, new Item(8839)), //
	Magic6(42, new Item(6916)), Magic7(50, new Item(2413)), Magic8(60, new Item(9079)), Magic9(65, new Item(10338)),
	Magic10(65, new Item(12002)), Magic11(70, new Item(4712)), Magic12(70, new Item(13235)),
	Magic13(75, new Item(21021)), Magic14(75, new Item(9763)); // 99CAPE

		private final int level;
		private final Item reward;

		MagicApplication(int level, Item reward) {
			this.level = level;
			this.reward = reward;
		}

		public int getLevel() {
			return level;
		}

		public Item getReward() {
			return reward;
		}

		public static Optional<MagicApplication> forLevel(int level) {
			return Arrays.stream(values()).filter(a -> a.level == level).findAny();
		}

		public static Optional<MagicApplication> forOrdinal(int ordinal) {
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

		if (!MagicApplication.forLevel(skillmenu).isPresent())
			return;

		MagicApplication reward = MagicApplication.forLevel(skillmenu).get();
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
			int size = MagicApplication.values().length;
			Item[] items = new Item[size + 3];
			for (int index = 0, string = 37116; index < size; index++) {
				MagicApplication perk = MagicApplication.forOrdinal(index).get();
				Item item = perk.getReward();
				int amount = player.skillmenuLevel == 0 ? perk.getReward().getAmount()
						: perk.getReward().getAmount() * player.skillmenuLevel;
				items[index + 3] = new Item(item.getId(), amount);
				player.send(new SendString("@whi@Level: <col=3c50b2>" + perk.getLevel(), string++));
				player.send(new SendString("@bla@" + item.getName(), string++));
			}

			player.send(new SendString("Magic is a player's power in melee combat.", 37111));
			player.send(new SendString("As a player raises their Magic. They can protect themselfs", 37112));
			player.send(new SendString("against an opponent. High Magic is favoured in PKing & PVM'ing", 37113));
			player.send(new SendString("In order to gain experience in the Magic Skill,", 37114));
			player.send(new SendString("players must choose a Magic spell.", 37115));
			player.send(new SendString("Locations - ::train", 37107));
			player.send(new SendString("Brutal Magic Guide", 37103));
			player.send(new SendScrollbar(37110, size * 50));
			player.send(new SendItemOnInterface(37199, items));
			player.interfaceManager.open(37100);
		}
	}
}
