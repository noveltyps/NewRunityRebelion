package io.server.content.skill.impl.agility.obstacle.impl;

import io.server.content.skill.impl.agility.obstacle.ObstacleInteraction;
import io.server.game.Animation;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;

public interface SwingAcrossCableInteraction extends ObstacleInteraction {
	@Override
	default void start(Player player) {
	}

	@Override
	default void onExecution(Player player, Position start, Position end) {
		World.schedule(new Task(1) {

			@Override
			public void execute() {
				player.animate(new Animation(getAnimation()));
			}
		});
	}

	@Override
	default void onCancellation(Player player) {
	}
}