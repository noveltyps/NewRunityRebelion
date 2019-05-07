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
public class AttackApp {

	/** Holds all the AttackApplication data. */
	public enum AttackApplication {
	ATTACK1(1, new Item(1307)), // BRONZE
	ATTACK2(1, new Item(1309)), // IRON
	ATTACK5(5, new Item(1311)), // STEEL
	ATTACK10(10, new Item(1313)), // BLACK
	ATTACK20(20, new Item(1315)), // MITH
	ATTACK30(30, new Item(1317)), // ADAM
	ATTACK40(40, new Item(1319)), // RUNE
	ATTACK50(50, new Item(4153)), // GMAUL
	ATTACK60(60, new Item(7158)), // DRAGON
	ATTACK70(70, new Item(4151)), // WHIP
	ATTACK99(99, new Item(9748)); // 99CAPE

		private final int level;
		private final Item reward;

		AttackApplication(int level, Item reward) {
			this.level = level;
			this.reward = reward;
		}

		public int getLevel() {
			return level;
		}

		public Item getReward() {
			return reward;
		}

		public static Optional<AttackApplication> forLevel(int level) {
			return Arrays.stream(values()).filter(a -> a.level == level).findAny();
		}

		public static Optional<AttackApplication> forOrdinal(int ordinal) {
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

		if (!AttackApplication.forLevel(skillmenu).isPresent())
			return;

		AttackApplication reward = AttackApplication.forLevel(skillmenu).get();
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
			int size = AttackApplication.values().length;
			Item[] items = new Item[size + 3];
			for (int index = 0, string = 37116; index < size; index++) {
				AttackApplication perk = AttackApplication.forOrdinal(index).get();
				Item item = perk.getReward();
				int amount = player.skillmenuLevel == 0 ? perk.getReward().getAmount()
						: perk.getReward().getAmount() * player.skillmenuLevel;
				items[index + 3] = new Item(item.getId(), amount);
				player.send(new SendString("@whi@Level: <col=3c50b2>" + perk.getLevel(), string++));
				player.send(new SendString("@bla@" + item.getName(), string++));
			}

			player.send(new SendString("Attack is a player's accuracy in melee combat.", 37111));
			player.send(new SendString("As a player raises their Attack. They can deal damage more", 37112));
			player.send(new SendString("consistently as well as wield weapons of stronger materials.", 37113));
			player.send(new SendString("In order to gain experience in the Attack stat,", 37114));
			player.send(new SendString(" players must choose the accurate attack style.", 37115));
			player.send(new SendString("Locations - ::train", 37107));
			player.send(new SendString("Brutal Attack Guide", 37103));
			player.send(new SendScrollbar(37110, size * 50));
			player.send(new SendItemOnInterface(37199, items));
			player.interfaceManager.open(37100);
		}
	}
}
