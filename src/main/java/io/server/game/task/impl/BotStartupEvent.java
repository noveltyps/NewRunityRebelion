package io.server.game.task.impl;

import io.server.Config;
import io.server.content.bot.PlayerBot;
import io.server.content.bot.objective.BotObjective;
import io.server.game.task.TickableTask;

/**
 * This loads all the bots into the game world after starting the server.
 *
 * @author Daniel
 */
public class BotStartupEvent extends TickableTask {

	public BotStartupEvent() {
		super(false, 1);
	}

	@Override
	protected void tick() {
		if (tick >= Config.MAX_BOTS) {
			cancel();
			return;
		}

		PlayerBot bot = new PlayerBot();
		bot.register();
		BotObjective.WALK_TO_BANK.init(bot);
	}
}
