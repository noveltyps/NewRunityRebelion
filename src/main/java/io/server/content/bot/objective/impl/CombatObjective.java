package io.server.content.bot.objective.impl;

import io.server.content.bot.BotUtility;
import io.server.content.bot.PlayerBot;
import io.server.content.bot.objective.BotObjectiveListener;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.Utility;

public class CombatObjective implements BotObjectiveListener {

	@Override
	public void init(PlayerBot bot) {
		Player opponent = (Player) bot.getCombat().getLastAggressor();
		bot.botClass.initCombat(opponent, bot);
		bot.getCombat().attack(opponent);
		bot.speak(Utility.randomElement(BotUtility.FIGHT_START_MESSAGES));
		bot.opponent = opponent;
	}

	@Override
	public void finish(PlayerBot bot) {
	}

}
