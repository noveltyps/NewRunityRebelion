package io.server.content.achievement;

import io.server.content.writer.InterfaceWriter;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendBanner;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Handles the achievements.
 *
 * @author Daniel
 */
public class AchievementHandler {

	/**
	 * Activates the achievement for the individual player. Increments the completed
	 * amount for the player. If the player has completed the achievement, they will
	 * receive their reward.
	 */
	public static void activate(Player player, AchievementKey achievement) {
		activate(player, achievement, 1);
	}

	/**
	 * Activates the achievement for the individual player. Increments the completed
	 * amount for the player. If the player has completed the achievement, they will
	 * receive their reward.
	 */
	public static void activate(Player player, AchievementKey achievement, int increase) {
		final int current = player.playerAchievements.computeIfAbsent(achievement, a -> 0);
		for (AchievementList list : AchievementList.values()) {
			if (list.getKey() == achievement) {
				if (current >= list.getAmount())
					return;
				player.playerAchievements.put(achievement, current + increase);
				if (player.playerAchievements.get(achievement) >= list.getAmount()) {
					player.bankVault.add(list.getDifficulty().getReward(), true);
					player.send(new SendBanner(
							"You've completed an achievement", "You've been rewarded with "
									+ Utility.formatDigits(list.getDifficulty().getReward()) + " gp",
							list.getDifficulty().getColor()));
				}
				InterfaceWriter.write(new AchievementInterface(player));
			}
		}
	}

	/**
	 * Completes all the achievements for player (used for administrative purposes).
	 */
	public static void completeAll(Player player) {
		if (!completedAll(player)) {
			for (AchievementList achievement : AchievementList.values()) {
				if (!player.playerAchievements.containsKey(achievement.getKey())) {
					player.playerAchievements.put(achievement.getKey(), achievement.getAmount());
					continue;
				}
				player.playerAchievements.replace(achievement.getKey(), achievement.getAmount());
			}
			player.send(new SendMessage("You have successfully Normal mastered all achievements."));
		}
	}

	/** Checks if the reward is completed. */
	public static boolean completed(Player player, AchievementList achievement) {
		if (!player.playerAchievements.containsKey(achievement.getKey()))
			player.playerAchievements.put(achievement.getKey(), 0);
		return player.playerAchievements.get(achievement.getKey()) >= achievement.getAmount();
	}

	/** Gets the total amount of achievements completed. */
	public static int getTotalCompleted(Player player) {
		int count = 0;
		for (AchievementList achievement : AchievementList.values()) {
			if (player.playerAchievements.containsKey(achievement.getKey()) && completed(player, achievement))
				count++;
		}
		return count;
	}

	/**
	 * Handles getting the amount of achievements completed based on it's
	 * difficulty.
	 */
	public static int getDifficultyCompletion(Player player, AchievementDifficulty difficulty) {
		int count = 0;
		for (AchievementList achievement : AchievementList.values()) {
			if (player.playerAchievements.containsKey(achievement.getKey()) && achievement.getDifficulty() == difficulty
					&& completed(player, achievement))
				count++;
		}
		return count;
	}

	/** Handles getting the amount of achievements based on the difficulty. */
	public static int getDifficultyAchievement(AchievementDifficulty difficulty) {
		int count = 0;
		for (AchievementList achievement : AchievementList.values()) {
			if (achievement.getDifficulty() == difficulty)
				count++;
		}
		return count;
	}

	/** Checks if a player has completed all the available achievements. */
	public static boolean completedAll(Player player) {
		return getTotalCompleted(player) == AchievementList.getTotal();
	}
}
