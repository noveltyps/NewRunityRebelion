package io.server.game;

import java.util.Objects;

/**
 * Class that models a single animation used by an entity.
 * 
 * @author Michael | Chex
 */
public class Animation implements Comparable<Animation> {

	public static final Animation RESET = new Animation(65535, UpdatePriority.VERY_LOW);

	/** The animation id. */
	private final int id;

	/** The delay before playing the animation. */
	private final int delay;

	/** The animation priority. */
	private final UpdatePriority priority;

	/**
	 * Creates a new instance of the animation with a hidden delay of 0.
	 * 
	 * @param id The id of the animation being used.
	 */
	public Animation(int id) {
		this(id, 0, UpdatePriority.NORMAL);
	}

	/**
	 * Creates a new instance of the animation with a specified delay.
	 * 
	 * @param id    The id of the animation being used.
	 * @param delay The delay of the animation in seconds.
	 */
	public Animation(int id, int delay) {
		this(id, delay, UpdatePriority.NORMAL);
	}

	/**
	 * Creates a new instance of the animation with a hidden delay of 0.
	 * 
	 * @param id       The id of the animation being used.
	 * @param priority The priority level of the animation.
	 */
	public Animation(int id, UpdatePriority priority) {
		this(id, 0, priority);
	}

	/**
	 * Creates a new instance of the animation with a specified delay.
	 * 
	 * @param id       The id of the animation being used.
	 * @param delay    The delay of the animation in seconds.
	 * @param priority The priority level of the animation.
	 */
	public Animation(int id, int delay, UpdatePriority priority) {
		this.priority = priority;
		this.id = id;
		this.delay = delay;
	}

	/**
	 * Gets the animation delay.
	 * 
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the animation id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, delay, priority);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Animation) {
			Animation other = (Animation) obj;
			return id == other.id && delay == other.delay && priority == other.priority;
		}
		return obj == this;
	}

	@Override
	public int compareTo(Animation other) {
		if (other == null || other.priority == null) {
			return 1;
		}

		return other.priority.compareTo(priority);
	}

	@Override
	public String toString() {
		return String.format("Animation[priority=%s, id=%s, delay=%s]", priority, id, delay);
	}

}
