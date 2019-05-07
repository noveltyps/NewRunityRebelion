package io.server.content.skill.impl.agility.obstacle.impl;

import io.server.content.skill.impl.agility.obstacle.ObstacleInteraction;
import io.server.game.Animation;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;

public interface ArdougneRoofJumpInteraction extends ObstacleInteraction {
	@Override
	default void start(Player player) {
	}

	@Override
	default void onExecution(Player player, Position start, Position end) {
		World.schedule(new Task(1) {
			int ticks = 0;

			@Override
			public void execute() {
				switch (ticks++) {
				case 1:
					player.face(new Position(2667, 3311));
					player.animate(new Animation(2586));
					break;
				case 2:
					player.move(new Position(2667, 3311, 1));
					player.animate(new Animation(2588));
					break;
				case 3:
					player.face(new Position(2665, 3315));
					break;
				case 4:
					player.animate(new Animation(2588));
					player.move(new Position(2665, 3315, 1));
					break;
				case 6:
					player.move(end);
					player.animate(new Animation(2588));
					cancel();
					break;
				}
			}
		});
	}

	@Override
	default void onCancellation(Player player) {
	}
}