package io.server.content.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import io.server.game.world.entity.mob.Mob;
import io.server.net.packet.out.SendMessage;

/**
 * A {@code GroupActivity} is an extension of {@link Activity} that holds a list
 * of active activities.
 *
 * @author Michael | Chex
 */
public abstract class GroupActivity extends Activity {

	/** A map of activities that handles each mob individually. */
	public final Map<Mob, Activity> activities;

	/** The next instance level. */
	private static int nextInstance;

	/**
	 * Constructs a new {@link GroupActivity} object.
	 *
	 * @param cooldown the initial cooldown in ticks
	 */
	protected GroupActivity(int cooldown, int capacity) {
		super(cooldown, getNextInstance());
		this.activities = new HashMap<>(capacity);
	}

	@Override
	protected void start() {
		activities.values().forEach(Activity::start);
	}

	@Override
	public void finish() {
		for (Map.Entry<Mob, Activity> entry : activities.entrySet()) {
			entry.getValue().finish();
			entry.getValue().cleanup();
		}
	}

	@Override
	public void cleanup() {
		activities.forEach((key, value) -> value.cleanup());
	}

	protected void removeAll() {
		activities.forEach((key, value) -> value.remove(key));
		activities.clear();
	}

	/**
	 * Checks if this group is active.
	 *
	 * @return {@code true} if there are active activities
	 */
	public final boolean isActive() {
		return !activities.isEmpty();
	}

	/**
	 * Gets the size of the activities in this group.
	 *
	 * @return the amount of activities in this group
	 */
	public int getActiveSize() {
		return activities.size();
	}

	/**
	 * Loops through all the activities.
	 *
	 * @param activity the consumer
	 */
	protected void forEachActivity(BiConsumer<Mob, Activity> activity) {
		activities.forEach(activity);
	}

	/**
	 * Adds an activity to the {@code activities} group.
	 *
	 * @param mob      the mob to add
	 * @param activity the mob's activity
	 */
	protected final void addActivity(Mob mob, Activity activity) {
		if (!activities.containsKey(mob)) {
			activities.put(mob, activity);
			add(mob);
		}
	}

	/**
	 * Removes an activity from the {@code activities} group.
	 *
	 * @param mob the mob that owns the activity
	 */
	protected final void removeActivity(Mob mob) {
		if (activities.containsKey(mob)) {
			activities.get(mob).finish();
			activities.get(mob).cleanup();
			activities.remove(mob);
			remove(mob);
		}
	}

	/**
	 * Sends a message to all the players in the group.
	 *
	 * @param message the message to send
	 */
	protected final void groupMessage(String message) {
		activities.keySet().forEach(mob -> {
			if (mob.isPlayer())
				mob.getPlayer().send(new SendMessage(message));
		});
	}

	/**
	 * Gets the next instance.
	 *
	 * @return the instacne level
	 */
	private static int getNextInstance() {
		return nextInstance++;
	}

}
