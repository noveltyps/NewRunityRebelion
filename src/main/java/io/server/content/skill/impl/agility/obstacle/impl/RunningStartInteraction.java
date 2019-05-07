package io.server.content.skill.impl.agility.obstacle.impl;

import io.server.content.skill.impl.agility.obstacle.ObstacleInteraction;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;

public interface RunningStartInteraction extends ObstacleInteraction {
	@Override
	default void start(Player player) {
		player.mobAnimation.setWalk(getAnimation());
	}

	@Override
	default void onExecution(Player player, Position start, Position end) {
		player.getCombat().reset();
		player.movement.walk(end);
		player.updateFlags.add(UpdateFlag.APPEARANCE);
	}

	@Override
	default void onCancellation(Player player) {
	}
}