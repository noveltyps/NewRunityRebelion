package io.server.game.world.pathfinding.path;

import io.server.game.world.Interactable;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.position.Position;

/**
 * An algorithm used to find a path between two {@link Position}s.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class PathFinder {

	/**
	 * Finds a valid path from the origin {@link Position} to the target one.
	 *
	 * @param origin The origin Position.
	 * @param target The target Position.
	 * @return The path containing the Positions to go through.
	 */
	public abstract Path find(Position origin, int originWidth, int originLength, Position target, int targetWidth,
			int targetLength);

	public final Path find(Interactable origin, Interactable target) {
		return find(origin.getPosition(), origin.width(), origin.length(), target.getPosition(), target.width(),
				target.length());
	}

	public final Path find(Interactable origin, Position target) {
		return find(origin.getPosition(), origin.width(), origin.length(), target, 0, 0);
	}

	public final Path find(Position origin, Position target) {
		return find(origin, 0, 0, target, 0, 0);
	}

	/**
	 * Returns whether or not a {@link Position} walking one step in any of the
	 * specified {@link Direction}s would lead to is traversable.
	 *
	 * @param current    The current Position.
	 * @param size       The entity's size.
	 * @param directions The Directions that should be checked.
	 * @return {@code true} if any of the Directions lead to a traversable tile,
	 *         otherwise {@code false}.
	 */
	protected boolean traversable(Position current, int size, Direction... directions) {
		for (Direction direction : directions) {
			if (!TraversalMap.isTraversable(current, direction, size)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns whether or not a {@link Position} is traversable to the direction of
	 * another {@link Position}.
	 *
	 * @param current The current Position.
	 * @param going   The position to which we are going.
	 * @return {@code true} if any of the Directions lead to a traversable tile,
	 *         otherwise {@code false}.
	 */
	boolean traversable(Position current, Position going, int size) {
		Direction first = Direction.getDirection(current, going);
		return TraversalMap.isTraversable(current, first, size);
	}

	/**
	 * Returns whether or not a {@link Position} shooting projectile to another
	 * {@link Position} would lead to is traversable.
	 *
	 * @param current The current Position.
	 * @param going   The position to which we are going.
	 * @return {@code true} if any of the Directions lead to a projectile
	 *         traversable tile, otherwise {@code false}.
	 */
	boolean projectileCheck(Position current, Position going) {
		Direction first = Direction.getDirection(going, current);
		Direction second = Direction.getDirection(current, going);
		return (TraversalMap.isTraversable(current, second, true) && TraversalMap.isTraversable(going, first, true));
	}

}