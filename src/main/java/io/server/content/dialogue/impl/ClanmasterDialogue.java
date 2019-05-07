package io.server.content.dialogue.impl;

import java.util.Optional;

import io.server.content.clanchannel.ClanMember;
import io.server.content.clanchannel.channel.ClanChannel;
import io.server.content.clanchannel.content.ClanLevel;
import io.server.content.clanchannel.content.ClanTask;
import io.server.content.clanchannel.content.ClanViewer;
import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.store.Store;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendScrollbar;
import io.server.net.packet.out.SendString;
import io.server.util.Difficulty;
import io.server.util.Utility;

/**
 * Handles the clan master dialogue.
 *
 * @author Daniel
 * @edited by Adam_#6723
 */
public class ClanmasterDialogue extends Dialogue {

	@Override
	public void sendDialogues(DialogueFactory factory) {
		Player player = factory.getPlayer();
		factory.sendNpcChat(1143, "Ello mate!", "What can I do you for today?");
		factory.sendOption("World Clan List", () -> player.clanViewer.open(ClanViewer.ClanTab.OVERVIEW),
				"Clan Management", () -> clanManagement(factory), "Clan Levels", () -> clanLevel(factory.getPlayer()),
				"Nevermind", factory::clear);
		factory.execute();
	}

	/** The clan management dialogue. */
	private void clanManagement(DialogueFactory factory) {
		Player player = factory.getPlayer();
		ClanChannel channel = player.clanChannel;

		if (channel == null) {
			factory.sendNpcChat(1143, "You must be in a clan in order to do this!");
			return;
		}

		Optional<ClanMember> member = channel.getMember(player.getName());

		if (!member.isPresent() || !channel.canManage(member.get())) {
			factory.sendNpcChat(1143, "You do not have the proper authorization to manage",
					"the clan " + channel.getName() + "! If you want to manage your own clan",
					"please join your own clan and then speak to me again.");
			return;
		}

		factory.sendOption("View store", () -> Store.STORES.get("The Clanmaster's Store").open(player), "Manage tasks",
				() -> clanTask(channel, factory), "Nevermind", factory::clear);
	}

	/** The clan task dialogue. */
	private void clanTask(ClanChannel channel, DialogueFactory factory) {
		ClanTask task = channel.getDetails().clanTask;
		factory.sendOption("Task Information", () -> {
			if (task == null) {
				factory.sendStatement("Your clan currently does not have a task assigned!");
				return;
			}

			factory.sendStatement("Your current task is to:", task.getName(channel));
		}, "Obtain Clan Task", () -> {
			if (task != null) {
				factory.sendNpcChat(1143, "Your clan currently has a task assigned!",
						"Cancel or complete it to obtain another one.");
				return;
			}
			factory.sendOption("Easy Task - <col=255>2 CP</col> Reward", () -> channel.receiveTask(Difficulty.EASY),
					"Medium Task - <col=255>3 CP</col> Reward", () -> channel.receiveTask(Difficulty.MEDIUM),
					"Hard Task - <col=255>5 CP</col> Reward", () -> channel.receiveTask(Difficulty.HARD));
		}, "Cancel Clan Task (<col=FF2929>5 CP</col>)", () -> {
			if (task == null) {
				factory.sendStatement("Your clan currently does not have a task assigned!");
				return;
			}
			if (channel.getDetails().points < 5) {
				factory.sendStatement("You do not have 5 clan points.");
				return;
			}
			channel.getDetails().points -= 5;
			if (channel.getDetails().points < 0) {
				channel.getDetails().points = 0;
			}
			channel.getDetails().taskAmount = -1;
			channel.getDetails().clanTask = null;
			channel.message("Our clan task was cancelled for 5 CP. We have <col=255>"
					+ Utility.formatDigits(channel.getDetails().points) + "</col> remaining.");
		}, "Nevermind", factory::clear);
	}

	/** Displays all the clan level information. */
	private void clanLevel(Player player) {
		int size = ClanLevel.values().length;
		for (int index = 0, string = 37111; index < size; index++) {
			ClanLevel level = ClanLevel.values()[index];
			String color = "<col=" + level.getColor() + ">";
			String experience = "Experience: <col=7A6856>" + Utility.formatDigits(level.getExperience()) + "</col>";
			String points = "Points: <col=7A6856>" + Utility.formatDigits(level.getPoints()) + "</col>";
			player.send(new SendString(color + Utility.formatEnum(level.name()), string));
			string++;
			player.send(new SendString(experience + " <col=000000>|</col> " + points, string));
			string++;
			player.send(new SendString("", string));
			string++;
		}
		player.send(new SendString("", 37107));
		player.send(new SendString("Clan Levels Information", 37103));
		player.send(new SendScrollbar(37110, 750));
		player.send(new SendItemOnInterface(37199));
		player.interfaceManager.open(37100);
	}
}
