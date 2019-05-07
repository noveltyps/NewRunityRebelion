package io.server.content.bot.objective;

import io.server.content.bot.PlayerBot;

public interface BotObjectiveListener {

	void init(PlayerBot bot);

	void finish(PlayerBot bot);

}
