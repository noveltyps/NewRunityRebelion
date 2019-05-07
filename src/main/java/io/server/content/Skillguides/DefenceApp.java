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
public class DefenceApp {

	/** Holds all the DefenceApplication data. */
	public enum DefenceApplication {
	Defence0(1, new Item(1175)), //
	Defence1(30, new Item(1183)), //
	Defence2(40, new Item(1185)), //
	Defence5(55, new Item(10828)), //
	Defence10(60, new Item(1187)), //
	Defence20(65, new Item(10348)), //
	Defence30(65, new Item(11832)), Defence40(70, new Item(4720)), Defence70(70, new Item(12831)),
	Defence71(75, new Item(11283)), Defence75(75, new Item(21015)), Defence99(99, new Item(9754)); // 99CAPE

		private final int level;
		private final Item reward;

		DefenceApplication(int level, Item reward) {
			this.level = level;
			this.reward = reward;
		}

		public int getLevel() {
			return level;
		}

		public Item getReward() {
			return reward;
		}

		public static Optional<DefenceApplication> forLevel(int level) {
			return Arrays.stream(values()).filter(a -> a.level == level).findAny();
		}

		public static Optional<DefenceApplication> forOrdinal(int ordinal) {
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

		if (!DefenceApplication.forLevel(skillmenu).isPresent())
			return;

		DefenceApplication reward = DefenceApplication.forLevel(skillmenu).get();
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
			int size = DefenceApplication.values().length;
			Item[] items = new Item[size + 3];
			for (int index = 0, string = 37116; index < size; index++) {
				DefenceApplication perk = DefenceApplication.forOrdinal(index).get();
				Item item = perk.getReward();
				int amount = player.skillmenuLevel == 0 ? perk.getReward().getAmount()
						: perk.getReward().getAmount() * player.skillmenuLevel;
				items[index + 3] = new Item(item.getId(), amount);
				player.send(new SendString("@whi@Level: <col=3c50b2>" + perk.getLevel(), string++));
				player.send(new SendString("@bla@" + item.getName(), string++));
			}

			player.send(new SendString("Defence is a player's power in melee combat.", 37111));
			player.send(new SendString("As a player raises their Defence. They can protect themselfs", 37112));
			player.send(new SendString("against an opponent. High Defence is favoured in PKing", 37113));
			player.send(new SendString("In order to gain experience in the Defence Skill,", 37114));
			player.send(new SendString("players must choose a Defensive Combat style.", 37115));
			player.send(new SendString("Locations - ::train", 37107));
			player.send(new SendString("RebelionX Defence Guide", 37103));
			player.send(new SendScrollbar(37110, size * 50));
			player.send(new SendItemOnInterface(37199, items));
			player.interfaceManager.open(37100);
		}
	}
}
