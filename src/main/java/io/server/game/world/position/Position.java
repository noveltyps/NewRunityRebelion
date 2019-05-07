package io.server.game.world.position;

import io.server.game.world.Interactable;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.region.Region;
import io.server.util.Utility;

/**
 * Represents a single tile on the game world.
 *
 * @author Graham Edgecombe
 */
public class Position {

	/** The maximum amount of height-planes. */
	public static final int HEIGHT_LEVELS = 4;

	/** The x coordinate. */
	private final int x;

	/** The y coordinate. */
	private final int y;

	/** The height coordinate. */
	private final int height;

	/**
	 * Creates a location with a default height of 0.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Position(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * Creates a location.
	 *
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param height The height coordinate.
	 */
	public Position(int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}

	/**
	 * Gets the absolute x coordinate.
	 *
	 * @return The absolute x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the absolute y coordinate.
	 *
	 * @return The absolute y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the height coordinate, or height.
	 *
	 * @return The height coordinate.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the local x coordinate relative to this region.
	 *
	 * @return The local x coordinate relative to this region.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local y coordinate relative to this region.
	 *
	 * @return The local y coordinate relative to this region.
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the local x coordinate relative to a specific region.
	 *
	 * @param origin The region the coordinate will be relative to.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Position origin) {
		return x - 8 * origin.getChunkX();
	}

	/**
	 * Gets the local y coordinate relative to a specific region.
	 *
	 * @param origin The region the coordinate will be relative to.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Position origin) {
		return y - 8 * origin.getChunkY();
	}

	/**
	 * Gets the chunk x coordinate.
	 *
	 * @return The region x coordinate.
	 */
	public int getChunkX() {
		return (x >> 3) - 6;
	}

	/**
	 * Gets the chunk y coordinate.
	 *
	 * @return The chunk y coordinate.
	 */
	public int getChunkY() {
		return (y >> 3) - 6;
	}

	public int getRegionX() {
		return getChunkX() / 8;
	}

	public int getRegionY() {
		return getChunkY() / 8;
	}

	public Region getRegion() {
		return World.getRegions().getRegion(this);
	}

	public Position north() {
		return transform(0, 1);
	}

	public Position east() {
		return transform(1, 0);
	}

	public Position south() {
		return transform(0, -1);
	}

	public Position west() {
		return transform(-1, 0);
	}

	public Position northEast() {
		return transform(1, 1);
	}

	public Position northWest() {
		return transform(-1, 1);
	}

	public Position southEast() {
		return transform(1, -1);
	}

	public Position southWest() {
		return transform(-1, -1);
	}

	/**
	 * Gets the manhattan distance between two interactable object.
	 *
	 * @param origin The originating location.
	 * @param target The target location.
	 * @return The distance between the origin and target.
	 */
	public static int getManhattanDistance(Interactable origin, Interactable target) {
		return Utility.getDistance(origin, target);
	}

	/**
	 * Checks if this location is within range of another.
	 *
	 * @param other  The other location.
	 * @param radius The radius from the origin point.
	 * @return {@code True} if the location is within the radius.
	 */
	public boolean isWithinDistance(Position other, int radius) {
		if (height != other.height) {
			return false;
		}

		final int deltaX = Math.abs(other.x - x);
		final int deltaY = Math.abs(other.y - y);
		return deltaX <= radius && deltaY <= radius;
	}

	/**
	 * Gets the distance between this location and another location.
	 *
	 * @param other The other location.
	 * @return The distance between the two locations.
	 */
	public double getDistance(Position other) {
		if (height != other.height)
			return 0;
		int dx = other.x - x;
		int dy = other.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Gets the Euclidean (straight-line) distance between two {@link Position} s.
	 *
	 * @return The distance in tiles between the two locations.
	 */
	public static double getDistance(Position first, Position second) {
		final int dx = second.getX() - first.getX();
		final int dy = second.getY() - first.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Gets the distance between this location and another location without
	 * diagonals.
	 *
	 * @param other The other location.
	 * @return The distance between the two locations.
	 */
	public int getManhattanDistance(Position other) {
		if (other == null || height != other.height)
			return Integer.MAX_VALUE;
		int dx = Math.abs(other.x - x);
		int dy = Math.abs(other.y - y);
		return dx + dy;
	}

	/**
	 * Gets the Euclidean (straight-line) distance between two {@link Position} s.
	 *
	 * @return The distance in tiles between the two locations.
	 */
	public static int getManhattanDistance(Position first, Position second) {
		final int dx = Math.abs(second.getX() - first.getX());
		final int dy = Math.abs(second.getY() - first.getY());
		return dx + dy;
	}

	/**
	 * Gets the longest horizontal or vertical delta between the two positions.
	 *
	 * @param other The other position.
	 * @return The longest horizontal or vertical delta.
	 */
	public int getLongestDelta(Position other) {
		if (height != other.height)
			return 0;
		int deltaX = Math.abs(getX() - other.getX());
		int deltaY = Math.abs(getY() - other.getY());
		return Math.max(deltaX, deltaY);
	}

	/**
	 * Creates a location.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The height coordinate.
	 * @return The location.
	 */
	public static Position create(int x, int y, int z) {
		return new Position(x, y, z);
	}

	/**
	 * Creates a location.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The location.
	 */
	public static Position create(int x, int y) {
		return new Position(x, y);
	}

	/**
	 * Creates a new location based on this location.
	 *
	 * @param diffX X difference.
	 * @param diffY Y difference.
	 * @param diffZ Z difference.
	 * @return The new location.
	 */
	public Position transform(int diffX, int diffY, int diffZ) {
		return new Position(x + diffX, y + diffY, height + diffZ);
	}

	/**
	 * Creates a new location based on this location.
	 *
	 * @param diffX X difference.
	 * @param diffY Y difference.
	 * @return The new location.
	 */
	public Position transform(int diffX, int diffY) {
		return new Position(x + diffX, y + diffY, height);
	}

	public boolean inLocation(Position southWest, Position northEast, boolean inclusive) {
		return !inclusive
				? this.x > southWest.getX() && this.x < northEast.getX() && this.y > southWest.getY()
						&& this.y < northEast.getY()
				: this.x >= southWest.getX() && this.x <= northEast.getX() && this.y >= southWest.getY()
						&& this.y <= northEast.getY();
	}

	/**
	 * Creates a new location based on this location.
	 *
	 * @param other The difference.
	 * @return The new location.
	 */
	public Position transform(Position other) {
		if (other == null)
			return this;
		return transform(other.getX(), other.getY(), other.getHeight());
	}

	/**
	 * Creates a deep copy of this location.
	 *
	 * @return A deep copy of this location.
	 */
	public Position copy() {
		return new Position(x, y, height);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj instanceof Position) {
			Position other = (Position) obj;
			return x == other.x && y == other.y && height == other.height;
		}

		return false;
	}

	public boolean matches(int x, int y) {
		return this.x == x && this.y == y;
	}

	public boolean matches(int x, int y, int z) {
		return this.x == x && this.y == y && this.height == z;
	}

	@Override
	public int hashCode() {
		return hash(x, y, height);
	}

	@Override
	public String toString() {
		return String.format("pos[x=%d, y=%d, z=%d]", x, y, height);
	}

	public static int hash(int x, int y, int z) {
		return (y << 16) | (x << 8) | z;
	}

	public int getDynamicHeight(Player player) {
		return player.getIndex() * 4;
	}

}
