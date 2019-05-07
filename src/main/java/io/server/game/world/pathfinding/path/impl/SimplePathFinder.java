package io.server.game.world.pathfinding.path.impl;

import static io.server.game.world.entity.mob.Direction.NONE;
import static io.server.game.world.entity.mob.Direction.getDirection;

import java.util.ArrayDeque;
import java.util.Deque;

import io.server.game.world.Interactable;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.pathfinding.path.Path;
import io.server.game.world.pathfinding.path.PathFinder;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.util.Utility;

/**
 * Represents a simple path finder which determines a straight path to the first
 * blocked tile or it's destination. Mostly used by {@link Mob} following and
 * movement.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class SimplePathFinder extends PathFinder {

	/**
	 * A default method to find a path for the specified position.
	 *
	 * @return A {@link Deque} of {@link Position steps} to the specified
	 *         destination.
	 */
	@Override
	public Path find(Position start, int originWidth, int originLength, Position end, int targetWidth,
			int targetLength) {
		int approximation = (int) (start.getLongestDelta(end) * 1.5);
		Deque<Position> positions = new ArrayDeque<>(approximation);
		return new Path(addWalks(start, originWidth, originLength, end, targetWidth, targetLength, positions));
	}

	/**
	 * Performs the path finding calculations to find the path using the A*
	 * algorithm.
	 *
	 * @param origin    The path finder's start position.
	 * @param target    The path finder's destination.
	 * @param positions The current searched deque of moves.
	 * @return The path to pursue to reach the destination.
	 */
	private Deque<Position> addWalks(Position origin, int originWidth, int originLength, Position target,
			int targetWidth, int targetLength, Deque<Position> positions) {
		Position current = origin;
		Position targ = target;
		Interactable targInt = Interactable.create(target, targetWidth, targetLength);

		if (targetWidth > 0 || targetLength > 0)
			target = Utility.findBestInside(Interactable.create(origin, originWidth, originLength), targInt);

		while (!Region.reachable(targ, targetWidth, targetLength, current, originWidth, originLength)) {
			Direction direction = getDirection(current, target);

			if (!traversable(current, originWidth, targInt, direction)) {
				if (direction == Direction.NORTH_WEST) {
					if (traversable(current, originWidth, targInt, Direction.WEST)) {
						direction = Direction.WEST;
					} else if (traversable(current, originWidth, targInt, Direction.NORTH)) {
						direction = Direction.NORTH;
					} else
						break;
				} else if (direction == Direction.NORTH_EAST) {
					if (traversable(current, originWidth, targInt, Direction.NORTH)) {
						direction = Direction.NORTH;
					} else if (traversable(current, originWidth, targInt, Direction.EAST)) {
						direction = Direction.EAST;
					} else
						break;
				} else if (direction == Direction.SOUTH_WEST) {
					if (traversable(current, originWidth, targInt, Direction.SOUTH)) {
						direction = Direction.SOUTH;
					} else if (traversable(current, originWidth, targInt, Direction.WEST)) {
						direction = Direction.WEST;
					} else
						break;
				} else if (direction == Direction.SOUTH_EAST) {
					if (traversable(current, originWidth, targInt, Direction.SOUTH)) {
						direction = Direction.SOUTH;
					} else if (traversable(current, originWidth, targInt, Direction.EAST)) {
						direction = Direction.EAST;
					} else
						break;
				} else
					break;
			}

			if (direction == NONE)
				break;

			current = current.transform(direction.getFaceLocation());
			positions.addLast(current);
		}
		return positions;
	}

	private boolean traversable(Position current, int size, Interactable target, Direction direction) {
		Position next = current.transform(direction.getFaceLocation());
		return traversable(current, size, direction)
				&& !Utility.inside(next, size, size, target.getPosition(), target.width(), target.length());
	}

}
