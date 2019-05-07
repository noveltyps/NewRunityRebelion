package io.server.content.activity.impl.barrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.server.content.ActivityLog;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
import io.server.game.task.impl.CeillingCollapseTask;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

public class BarrowsUtility {

	public static Item[] JUNK = { new Item(995, 500), new Item(985), new Item(987), new Item(1149) };

	public static int RUNES_AND_AMMUNITION[] = { 4740, 558, 560, 565 };

	public static int BARROWS[] = { 4708, 4710, 4712, 4714, 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4718,
			4720, 4722, 4724, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751,
			4753, 4755, 4757, 4759,  13701, 13702, 17165, 17164, 17163, 6199};

	public static final int[][] BROKEN_BARROWS = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 },
			{ 4716, 4884 }, { 4720, 4896 }, { 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 },
			{ 4730, 4926 }, { 4745, 4956 }, { 4747, 4926 }, { 4749, 4968 }, { 4751, 4994 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public static boolean teleportPlayer(Player player) {
		for (BrotherData brother : BrotherData.values()) {
			if (player.getPosition().inLocation(brother.getSouthWest(), brother.getNorthEast(), true)) {
				player.move(brother.getCryptPosition());
				player.send(
						new SendMessage("You dig a hole and fall into " + brother.name().toLowerCase() + "s crypt!"));
				return true;
			}
		}
		return false;
	}

	static BrotherData getHiddenBrother(Player player) {
		List<BrotherData> brother = new ArrayList<>();
		for (int i = 0; i < player.barrowKills.length; i++) {
			if (!player.barrowKills[i]) {
				brother.add(BrotherData.values()[i]);
			}
		}
		return Utility.randomElement(brother);
	}

	static void generateRewards(Player player) {
		Set<Item> rewards = new HashSet<>();
		Item[] rewardDisplay = new Item[5];
		Item junk = Utility.randomElement(JUNK);
		Item runes = new Item(Utility.randomElement(BarrowsUtility.RUNES_AND_AMMUNITION), 25 + Utility.random(5));
		Item runes2 = new Item(Utility.randomElement(BarrowsUtility.RUNES_AND_AMMUNITION), 25 + Utility.random(5));

		if (junk.getId() == 995) {
			junk.setAmount(Utility.random(5000, 25000));
		}

		rewards.add(junk);

		if (runes.getId() == runes2.getId()) {
			runes.setAmount(runes.getAmount() + runes2.getAmount());
			rewards.add(runes);
		} else {
			rewards.add(runes);
			rewards.add(runes2);
		}

		if (Utility.random(100) >= 90) {
			rewards.add(new Item(Utility.randomElement(BarrowsUtility.BARROWS)));
		}

		int value = 0;
		int count = 0;
		for (Item item : rewards) {
			value += item.getValue() * item.getAmount();
			rewardDisplay[count] = item;
			count++;
		}

		player.send(new SendString("Total worth: " + Utility.formatDigits(value), 33306));
		player.send(new SendItemOnInterface(33305, rewardDisplay));
		rewards.forEach(player.inventory::addOrDrop);
		player.interfaceManager.open(33300);
		player.activityLogger.add(ActivityLog.BARROWS);
		World.schedule(new CeillingCollapseTask(player));
		AchievementHandler.activate(player, AchievementKey.BARROWS);
	}
}
