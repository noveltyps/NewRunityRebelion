package io.server.content;

import io.server.content.achievement.AchievementHandler;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendPlayerIndex;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

/**
 * Handles viewing other player's profiles.
 *
 * @author Daniel
 */
public class ProfileViewer {

	/** Gets the main strings to display. */
	public static String[] string(Player player) {
		return new String[] { "Created: ", player.created, "Total play time:", Utility.getTime(player.playTime),
				"Session time:", Utility.getTime(player.sessionPlayTime), "Networth:",
				"" + Utility.formatPrice(player.playerAssistant.networth()) + " RT", "Clan:",
				player.clan == null ? "None" : player.clan, "Total level:",
				"" + Utility.formatDigits(player.skills.getTotalLevel()), "Kills/Deaths/KDR",
				"" + player.killCount + "/" + player.deathCount + "/" + player.playerAssistant.getKdr() + "",
				"Current KC/Highest KC", "0/0", "Achievements Completed:",
				"" + AchievementHandler.getTotalCompleted(player) + "", "Quests Completed:",
				"" + player.quest.getCompleted() };
	}

	/** Opens the profile itemcontainer. */
	public static void open(Player player, Player other) {
		if (!other.getPosition().isWithinDistance(player.getPosition(), 26)) {
			player.message("You must get closer to that player if you want to view their profile!");
			return;
		}
		if (player.getCombat().inCombat()) {
			player.send(new SendMessage("You can't view profiles whilst in combat!"));
			return;
		}
		for (int index = 0, string = 51832; index < Skill.SKILL_COUNT; index++, string += 2) {
			Skill skill = other.skills.get(index);
			String color = Integer.toHexString(other.prestige.getPrestigeColor(index));
			player.send(new SendString(
					skill.getLevel() + "<col=" + color + "></col>/<col=" + color + ">" + skill.getMaxLevel(), string));
		}
		for (int index = 0; index < string(other).length; index++) {
			player.send(new SendString(string(other)[index], 51901 + index));
		}
		other.profileView++;
		player.send(new SendPlayerIndex(other.getIndex()));
		player.send(new SendString("Profile views: " + other.profileView, 51810));
		player.send(new SendString("</col>Name: <col=FFB83F>" + other.getName(), 51807));
		player.send(new SendString(
				"</col>Rank: <col=FFB83F>" + PlayerRight.getCrown(other) + " " + other.right.getName(), 51808));
		player.send(new SendString("</col>Level: <col=FFB83F>" + other.skills.getCombatLevel(), 51809));
		player.send(new SendMessage("You are now viewing " + other.getName() + "'s profile."));
		player.interfaceManager.open(51800);
	}
}