package io.server.game.world.pathfinding.path;

import io.server.game.world.Interactable;
import io.server.game.world.position.Position;

/**
 * Represents a {@code PathFinder} which is meant to be used to check
 * projectiles passage in a straight line.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class SimplePathChecker extends PathFinder {
	private static final SimplePathChecker SIMPLE_PATH_CHECKER = new SimplePathChecker();

	@Override
	public Path find(Position start, int originWidth, int originLength, Position end, int targetWidth,
			int targetLength) {
		return new Path(null);// Empty path.
	}

	public static boolean checkLine(Interactable source, Interactable target) {
		return SIMPLE_PATH_CHECKER.check(source.getPosition(), target.getPosition(), source.width(), false);
	}

	public static boolean checkLine(Position start, Position end, int size) {
		return SIMPLE_PATH_CHECKER.check(start, end, size, false);
	}

	public static boolean checkLine(Interactable source, Position target) {
		return SIMPLE_PATH_CHECKER.check(source.getPosition(), target, source.width(), false);
	}

	public static boolean checkProjectile(Interactable source, Interactable target) {
		return SIMPLE_PATH_CHECKER.check(source.getPosition(), target.getPosition(), 1, true);
	}

	public static boolean checkProjectile(Interactable source, Position target) {
		return SIMPLE_PATH_CHECKER.check(source.getPosition(), target, 1, true);
	}

	public static boolean checkProjectile(Position start, Position end) {
		return SIMPLE_PATH_CHECKER.check(start, end, 1, true);
	}

	/**
	 * Determines if the projectile can reach it's destination.
	 *
	 * @param start      The projectile's starting Position.
	 * @param end        The projectile's ending Position.
	 * @param projectile The condition if the check is meant for projectiles.
	 * @return {@code true} if the projectile can reach it's destination, {@code
	 * false} otherwise.
	 */
	private boolean check(Position start, Position end, int size, boolean projectile) {
		int deltaX = end.getX() - start.getX();
		int deltaY = end.getY() - start.getY();
		double error = 0;
		final double deltaError = Math.abs((deltaY) / (deltaX == 0 ? ((double) deltaY) : ((double) deltaX)));
		int x = start.getX();
		int y = start.getY();
		int z = start.getHeight();
		int pX = x;
		int pY = y;
		boolean incrX = start.getX() < end.getX();
		boolean incrY = start.getY() < end.getY();
		while (true) {
			if (x != end.getX()) {
				x += (incrX ? 1 : -1);
			}
			if (y != end.getY()) {
				error += deltaError;

				if (error >= 0.5) {
					y += (incrY ? 1 : -1);
					error -= 1;
				}
			}
			if (projectile ? !projectileCheck(new Position(x, y, z), new Position(pX, pY, z))
					: !traversable(new Position(x, y, z), new Position(pX, pY, z), size)) {
				return false;
			}
			if (incrX && incrY && x >= end.getX() && y >= end.getY()) {
				break;
			} else if (!incrX && !incrY && x <= end.getX() && y <= end.getY()) {
				break;
			} else if (!incrX && incrY && x <= end.getX() && y >= end.getY()) {
				break;
			} else if (incrX && !incrY && x >= end.getX() && y <= end.getY()) {
				break;
			}
			pX = x;
			pY = y;
		}
		return true;
	}

}