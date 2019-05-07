package io.server.game.task;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;

/**
 * The class that handles scheduling tasks, and processing them.
 *
 * @author nshusa
 */
public final class TaskManager {

	/**
	 * The single logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger();

	/**
	 * The queue of tasks that are waiting to be executed. A
	 * {@link ConcurrentLinkedQueue} is used here so tasks can be added safely from
	 * multiple threads.
	 */
	private final static Queue<Task> pending = new ConcurrentLinkedQueue<>();

	/**
	 * The list of tasks that are currently running. A {@link ArrayList} is used
	 * here for very fast iteration speed.
	 */
	private final List<Task> active = new ArrayList<>();

	/**
	 * Schedules a {@link Task} to be ran in the future.
	 *
	 * @param task The task to schedule.
	 */
	public static void schedule(Task task) {
		Preconditions.checkNotNull(task);

		try {
			if (!task.canSchedule()) {
				return;
			}

			task.setRunning(true);
			task.onSchedule();

			if (task.isInstant()) {
				try {
					task.execute();
				} catch (Exception ex) {
					logger.warn(String.format("error executing task: %s", task.getClass().getSimpleName()), ex);
					return;
				}
			}
			pending.add(task);
		} catch (Exception ex) {
			logger.error(String.format("error scheduling task: %s", task.getClass().getSimpleName()));
		}
	}

	/**
	 * This method handles adding new tasks and processing of active tasks.
	 */
	public void processTasks() {
		Task t;
		while ((t = pending.poll()) != null) {
			active.add(t);
		}

		for (final ListIterator<Task> itr = active.listIterator(); itr.hasNext();) {
			final Task task = itr.next();

			try {
				task.process();
			} catch (Exception ex) {
				logger.warn(String.format("Error executing task: %s [pendingQueue: %d executionQueue: %d], ",
						task.getClass().getSimpleName(), pending.size(), active.size()), ex);
				task.cancel();
			}

			if (!task.isRunning()) {
				itr.remove();
			}
		}
	}

	/**
	 * Cancels a task that has a specific {@code attachment}.
	 *
	 * @param attachment The object that is attached to the task.
	 */
	public void cancel(Object attachment) {
		cancel(attachment, false);
	}

	/**
	 * Cancels a task that has a specific {@code attachment}.
	 *
	 * @param attachment The object that is attached to the task.
	 *
	 * @param logout     When the task gets canceled this flag determines if the
	 *                   player should logout.
	 */
	public void cancel(Object attachment, boolean logout) {
		for (Task task : pending) {
			if (attachment.equals(task.getAttachment().orElse(null))) {
				task.cancel(logout);
			}
		}
	}

}