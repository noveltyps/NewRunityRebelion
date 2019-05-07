package io.server.content.skill.impl.agility.obstacle.impl;

import io.server.content.skill.impl.agility.obstacle.ObstacleInteraction;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;

public interface ClimbOverInteraction extends ObstacleInteraction {
	@Override
	default void start(Player player) {
	}

	@Override
	default void onExecution(Player player, Position start, Position end) {
		int modX = end.getX() - player.getPosition().getX();
		int modY = end.getY() - player.getPosition().getY();
		Position destination = Position.create(modX, modY);
		Direction direction = Direction.getFollowDirection(player.getPosition(), end);
		player.forceMove(2, getAnimation(), 23, 60, destination, direction);
	}

	@Override
	default void onCancellation(Player player) {
	}
}