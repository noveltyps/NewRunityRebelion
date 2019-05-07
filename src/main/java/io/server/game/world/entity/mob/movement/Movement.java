package io.server.game.world.entity.mob.movement;

import java.util.Deque;
import java.util.LinkedList;

import io.server.game.world.Interactable;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.pathfinding.distance.Manhattan;
import io.server.game.world.pathfinding.path.Path;
import io.server.game.world.pathfinding.path.impl.AStarPathFinder;
import io.server.game.world.pathfinding.path.impl.DijkstraPathFinder;
import io.server.game.world.pathfinding.path.impl.SimplePathFinder;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendConfig;

/**
 * Handles the movement for the player.
 *
 * @author Graham Edgecombe
 */
public class Movement {
	/**
	 * The maximum size of the queue. If there are more points than this size, they
	 * are discarded.
	 */
	private static final int MAXIMUM_SIZE = 50;

	/** The smart path finder. */
	private final AStarPathFinder smartPathFinder;

	/** The smart path finder. */
	private static final SimplePathFinder SIMPLE_PATH_FINDER = new SimplePathFinder();

	/** The smart path finder. */
	private static final DijkstraPathFinder DIJKSTRA_PATH_FINDER = new DijkstraPathFinder();

	/** The mob. */
	private Mob mob;

	/** The queue of waypoints. */
	private Deque<Point> waypoints = new LinkedList<>();

	/** The last direction the mob walked in. Default to south */
	public Direction lastDirection = Direction.SOUTH;

	/** Mob is moving. */
	private boolean isMoving;

	/** Run toggle (button in client). */
	private boolean runToggled = false;

	/** Run for this queue (CTRL-CLICK) toggle. */
	private boolean runQueue = false;

	private int walkingDirection = -1;
	private int runningDirection = -1;

	/** Creates the <code>WalkingQueue</code> for the specified */
	public Movement(Mob mob) {
		this.mob = mob;
		this.smartPathFinder = new AStarPathFinder(mob, new Manhattan());
	}

	/** Walks to a certain position. */
	public void walk(Position position) {
		reset();
		addStep(position.getX(), position.getY());
		finish();
	}

	/** Handles mob walking to certain coordinates. */
	public void walkTo(int x, int y) {
		final int newX = mob.getX() + x;
		final int newY = mob.getY() + y;
		reset();
		addStepInternal(newX, newY);
		finish();
	}

	/*
	 * public void stepAway(Character character) {
	 * if(character.getMovementQueue().canWalk(-1, 0))
	 * character.getMovementQueue().walkStep(-1, 0); else
	 * if(character.getMovementQueue().canWalk(1, 0))
	 * character.getMovementQueue().walkStep(1, 0); else
	 * if(character.getMovementQueue().canWalk(0, -1))
	 * character.getMovementQueue().walkStep(0, -1); else
	 * if(character.getMovementQueue().canWalk(0, 1))
	 * character.getMovementQueue().walkStep(0, 1); } public boolean canWalk(int
	 * deltaX, int deltaY) { final Position to = new
	 * Position(mob.getPlayer().getPosition().getX()+deltaX,
	 * mob.getPlayer().getPosition().getY()+deltaY,
	 * mob.getPlayer().getPosition().getHeight());
	 * if(mob.getPlayer().getPosition().getHeight() == -1 && to.getHeight() == -1 &&
	 * mob.getPlayer().isNpc()) { return true; } return false; }
	 */
	/** Handles mob walking to a certain position. */
	public void walkTo(Position position) {
		reset();
		addStepInternal(position.getX(), position.getY());
		finish();
	}

	/** Sets the run toggled flag. */
	public void setRunningToggled(boolean runToggled) {
		this.runToggled = runToggled;
		if (mob.isPlayer()) {
			mob.getPlayer().send(new SendConfig(152, runToggled ? 1 : 0));
		}
	}

	/** Resets the walking queue so it contains no more steps. */
	public void reset() {
		runQueue = false;
		waypoints.clear();
		waypoints.add(new Point(mob.getX(), mob.getY(), -1));
	}

	/**
	 * Removes the first waypoint which is only used for calculating directions.
	 * This means walking begins at the correct time.
	 */
	public void finish() {
		waypoints.removeFirst();
	}

	/**
	 * Adds a single step to the walking queue, filling in the points to the
	 * previous point in the queue if necessary.
	 */
	public void addStep(int x, int y) {
//        if (mob.locked()) return;
		if (waypoints.size() == 0)
			reset();
		Point last = waypoints.peekLast();
		int diffX = x - last.x;
		int diffY = y - last.y;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
			addStepInternal(x - diffX, y - diffY);
		}
	}

	/**
	 * Adds a single step to the queue internally without counting gaps. This method
	 * is unsafe if used incorrectly so it is private to protect the queue.
	 */
	private void addStepInternal(int x, int y) {
		if (waypoints.size() >= MAXIMUM_SIZE)
			return;
		Point last = waypoints.peekLast();
		int diffX = x - last.x;
		int diffY = y - last.y;
		int dir = Direction.direction(diffX, diffY);
		if (dir > -1)
			waypoints.add(new Point(x, y, dir));
	}

	/** Processes the next player's movement. */
	public void processNextMovement() {
		boolean teleporting = mob.teleportTarget != null;
		if (teleporting) {
			reset();
			mob.positionChange = true;
			mob.setPosition(mob.teleportTarget);
			mob.clearTeleportTarget();
		} else {
			Point walkPoint, runPoint = null;
			walkPoint = getNextPoint();

			if (runToggled || runQueue) {
				runPoint = getNextPoint();
			}

			int walkDir = walkPoint == null ? -1 : walkPoint.dir;
			int runDir = runPoint == null ? -1 : runPoint.dir;
			if (runDir != -1)
				lastDirection = Direction.DIRECTIONS.get(runDir);
			else if (walkDir != -1)
				lastDirection = Direction.DIRECTIONS.get(walkDir);
			this.walkingDirection = walkDir;
			this.runningDirection = runDir;
		}
		int diffX = mob.getPosition().getLocalX(mob.lastPosition);
		int diffY = mob.getPosition().getLocalY(mob.lastPosition);
		boolean changed = false;
		if (diffX < 16) {
			changed = true;
		} else if (diffX >= 88) {
			changed = true;
		}
		if (diffY < 16) {
			changed = true;
		} else if (diffY >= 88) {
			changed = true;
		}
		if (changed)
			mob.regionChange = true;

//        if (mob.attributes.has("mob-following")) {
//            Waypoint waypoint = mob.attributes.get("mob-following");
//            waypoint.onChange();
//        }
	}

	/** Gets the next point of movement. */
	private Point getNextPoint() {
		Point p = waypoints.poll();
		if (p == null || p.dir == -1) {
			if (isMoving)
				isMoving = false;
			return null;
		} else {
			int diffX = Direction.DELTA_X[p.dir];
			int diffY = Direction.DELTA_Y[p.dir];
			mob.setPosition(mob.getPosition().transform(diffX, diffY));
			if (!isMoving)
				isMoving = true;
			mob.onStep();
			return p;
		}
	}

	/** Finds a smart path to the target. */
	public boolean simplePath(Position destination) {
		return addPath(SIMPLE_PATH_FINDER.find(mob, destination));
	}

	/** Finds a medium path to the target. */
	public boolean dijkstraPath(Position destination) {
		return addPath(DIJKSTRA_PATH_FINDER.find(mob, destination));
	}

	/** Finds a smart path to the target. */
	public boolean aStarPath(Position destination) {
		return addPath(smartPathFinder.find(mob, destination));
	}

	/** Finds a smart path to the target. */
	public boolean simplePath(Interactable interactable) {
		return addPath(SIMPLE_PATH_FINDER.find(mob, interactable));
	}

	/** Finds a medium path to the target. */
	public boolean dijkstraPath(Interactable interactable) {
		return addPath(DIJKSTRA_PATH_FINDER.find(mob, interactable));
	}

	/** Finds a smart path to the target. */
	public boolean aStarPath(Interactable interactable) {
		return addPath(smartPathFinder.find(mob, interactable));
	}

	/** Finds a smart path to the target. */
	public boolean addPath(Path path) {
		if (!path.isPossible())
			return false;
		reset();
		for (Position next : path.getMoves()) {
//            if (mob.isNpc() && Region.isMobOnTile(mob.width(), next)) {
//                continue;
//            }
			addStep(next.getX(), next.getY());
		}
		finish();
		return true;
	}

	/** Sets the run queue flag. */
	public void setRunningQueue(boolean runQueue) {
		this.runQueue = runQueue;
	}

	/** Gets the run toggled flag. */
	public boolean isRunningToggled() {
		return runToggled;
	}

	/** Gets the running queue flag. */
	public boolean isRunningQueue() {
		return runQueue;
	}

	/** Checks if any running flag is set. */
	public boolean isRunning() {
		return runToggled || runQueue;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public boolean hasSteps() {
		return !waypoints.isEmpty();
	}

	public boolean needsPlacement() {
		return isMoving || hasSteps();
	}

	public AStarPathFinder getSmartPathFinder() {
		return smartPathFinder;
	}

	public int getWalkingDirection() {
		return walkingDirection;
	}

	public int getRunningDirection() {
		return runningDirection;
	}

}
