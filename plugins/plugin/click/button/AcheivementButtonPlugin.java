package plugin.click.button;

import static io.server.content.achievement.AchievementButton.ACHIEVEMENT_BUTTONS;
import static io.server.content.achievement.AchievementButton.ACHIEVEMENT_TITLES;

import io.server.content.achievement.AchievementDifficulty;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementList;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;
import io.server.util.Utility;

public class AcheivementButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (ACHIEVEMENT_BUTTONS.containsKey(button)) {
			AchievementList achievement = ACHIEVEMENT_BUTTONS.get(button);
			boolean completed = AchievementHandler.completed(player, achievement);
			int completion = player.playerAchievements.computeIfAbsent(achievement.getKey(), a -> 0);
			int progress = (int) (completion * 100 / (double) achievement.getAmount());
			String remaining = " (" + Utility.formatDigits((achievement.getAmount() - completion)) + " remaining).";
			player.send(new SendMessage("You have completed " + (progress > 100 ? 100 : progress)
					+ "% of this achievement" + (completed ? "." : remaining), MessageColor.DARK_BLUE));
			return true;
		}

		if (ACHIEVEMENT_TITLES.containsKey(button)) {
			AchievementDifficulty difficulty = ACHIEVEMENT_TITLES.get(button);
			int completed = AchievementHandler.getDifficultyCompletion(player, difficulty);
			int total = AchievementHandler.getDifficultyAchievement(difficulty);
			player.send(new SendMessage("You have completed a total of " + completed + "/" + total + " "
					+ difficulty.name().toLowerCase() + " achievements.", MessageColor.DARK_BLUE));
			return true;
		}
		return false;
	}
}
