package io.server.game.world.object;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The enumerated type whose elements represent the directions for objects.
 *
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum ObjectDirection {

	/** The north orientation. */
	NORTH(1),

	/** The south orientation. */
	SOUTH(3),

	/** The east orientation. */
	EAST(2),

	/** The west orientation. */
	WEST(0);

	/** The identification of this direction. */
	private final int id;

	/** Creates a new {@link ObjectDirection}. */
	ObjectDirection(int id) {
		this.id = id;
	}

	/**
	 * Gets the identification of this direction.
	 *
	 * @return the identification of this direction.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * A mutable {@link Map} of {@code int} keys to {@link ObjectDirection} values.
	 */
	private static final Map<Integer, ObjectDirection> values = new HashMap<>();

	/* Populates the {@link #values} cache. */
	static {
		for (ObjectDirection orientation : values()) {
			values.put(orientation.getId(), orientation);
		}
	}

	/**
	 * Returns a {@link ObjectDirection} wrapped in an {@link Optional} for the
	 * specified {@code id}.
	 *
	 * @param id The game object orientation id.
	 * @return The optional game object orientation.
	 */
	public static Optional<ObjectDirection> valueOf(int id) {
		return Optional.ofNullable(values.get(id));
	}

}
