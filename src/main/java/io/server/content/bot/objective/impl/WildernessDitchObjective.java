package io.server.content.bot.objective.impl;

import io.server.content.bot.PlayerBot;
import io.server.content.bot.objective.BotObjective;
import io.server.content.bot.objective.BotObjectiveListener;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.position.Position;
import io.server.util.RandomUtils;

public class WildernessDitchObjective implements BotObjectiveListener {

	@Override
	public void init(PlayerBot bot) {
		int x = RandomUtils.inclusive(3083, 3108);
		Position position = Position.create(x, 3520);
		bot.walkTo(position, () -> finish(bot));
	}

	@Override
	public void finish(PlayerBot bot) {
		bot.face(Direction.NORTH);
		bot.forceMove(3, 6132, 33, 60, new Position(0, 4), Direction.NORTH);
		bot.schedule(4, () -> BotObjective.WALK_IN_WILDERNESS.init(bot));
	}

}
