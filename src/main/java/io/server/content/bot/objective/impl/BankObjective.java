package io.server.content.bot.objective.impl;

import io.server.content.bot.PlayerBot;
import io.server.content.bot.objective.BotObjective;
import io.server.content.bot.objective.BotObjectiveListener;
import io.server.game.world.position.Position;
import io.server.util.RandomUtils;

public class BankObjective implements BotObjectiveListener {

	/** The positions of all the bank locations for the bot to access. */
	private static final Position[] BANK_LOCATIONS = { new Position(3096, 3493), new Position(3098, 3493),
			new Position(3095, 3491), new Position(3095, 3489) };

	@Override
	public void init(PlayerBot bot) {
		Position position = RandomUtils.random(BANK_LOCATIONS);
		bot.walkTo(position, () -> finish(bot));
	}

	@Override
	public void finish(PlayerBot bot) {
		bot.schedule(RandomUtils.random(6, 12), () -> BotObjective.RESTOCK.init(bot));
	}

}
