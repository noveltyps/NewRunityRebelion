package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.content.triviabot.TriviaBot;
import io.server.game.world.entity.mob.player.Player;

/**
 * @author Adam_#6723
 */

public class AnswerTriviaCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String message = parts[1].replaceAll("_", " ");
		TriviaBot.answer(player, message);

	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	
	@Override
	public String description() {
		return "Used for attempting to answer the Trivia question.";
	}

}
