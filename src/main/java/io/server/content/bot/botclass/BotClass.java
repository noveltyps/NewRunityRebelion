package io.server.content.bot.botclass;

import io.server.content.bot.PlayerBot;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;

public interface BotClass {

	Item[] inventory();

	Item[] equipment();

	int[] skills();

	void initCombat(Player target, PlayerBot bot);

	void handleCombat(Player target, PlayerBot bot);

	void endFight(PlayerBot bot);

	void pot(Player target, PlayerBot bot);

	void eat(Player target, PlayerBot bot);

}
