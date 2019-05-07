package io.server.game.task.impl;

import io.server.game.Animation;
import io.server.game.task.Task;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.ForceMovement;
import io.server.game.world.position.Position;

public class ForceMovementTask extends Task {
	private Mob mob;
	private Position start;
	private Position end;
	private Animation animation;
	private ForceMovement forceMovement;

	private final int moveDelay;
	private int tick;

	public ForceMovementTask(Mob mob, int delay, ForceMovement forceMovement, Animation animation) {
		this(mob, delay, 0, forceMovement, animation);
	}

	public ForceMovementTask(Mob mob, int delay, int moveDelay, ForceMovement forceMovement, Animation animation) {
		super(delay == 0, delay);
		this.mob = mob;
		this.start = forceMovement.getStart().copy();
		this.end = forceMovement.getEnd().copy();
		this.animation = animation;
		this.forceMovement = forceMovement;
		this.moveDelay = moveDelay;
	}

	@Override
	protected boolean canSchedule() {
		return mob.forceMovement == null;
	}

	@Override
	protected void onSchedule() {
		mob.getCombat().reset();
		mob.movement.reset();
		mob.animate(animation);
		mob.setForceMovement(forceMovement);
	}

	@Override
	public void execute() {
		if (tick >= moveDelay) {
			final int x = start.getX() + end.getX();
			final int y = start.getY() + end.getY();
			mob.move(new Position(x, y, mob.getHeight()));
			mob.setForceMovement(null);
			cancel();
		}
		tick++;
	}
}
