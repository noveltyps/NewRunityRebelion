package io.server.game.world.entity.mob.movement.waypoint;

import io.server.game.world.Interactable;
import io.server.game.world.entity.mob.Mob;
import io.server.util.Utility;

public class WalkToWaypoint extends Waypoint {
	private Runnable onDestination;

	public WalkToWaypoint(Mob mob, Interactable target, Runnable onDestination) {
		super(mob, target);
		this.onDestination = onDestination;
	}

	@Override
	public void onDestination() {
		mob.movement.reset();
		mob.face(Utility.findBestInside(mob, target));
		onDestination.run();
		cancel();
	}

}
