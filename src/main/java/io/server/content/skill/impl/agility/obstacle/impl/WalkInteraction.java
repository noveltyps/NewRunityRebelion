package io.server.content.skill.impl.agility.obstacle.impl;

import io.server.content.skill.impl.agility.obstacle.ObstacleInteraction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;

public interface WalkInteraction extends ObstacleInteraction {
	@Override
	default void start(Player player) {
		player.mobAnimation.setWalk(getAnimation());
	}

	@Override
	default void onExecution(Player player, Position start, Position end) {
		player.movement.walk(end);
	}

	@Override
	default void onCancellation(Player player) {
	}
}