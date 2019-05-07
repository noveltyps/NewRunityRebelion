package io.server.content.skill.impl.agility.obstacle.impl;

import io.server.content.skill.impl.agility.obstacle.ObstacleInteraction;
import io.server.game.Animation;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;

public interface ClimbInteraction extends ObstacleInteraction {
	@Override
	default void start(Player player) {
	}

	@Override
	default void onExecution(Player player, Position start, Position end) {
		player.animate(new Animation(getAnimation()));
		World.schedule(new Task(2) {
			@Override
			public void execute() {
				player.move(end);
				player.animate(new Animation(65535));
				this.cancel();
			}
		});
	}

	@Override
	default void onCancellation(Player player) {
	}
}