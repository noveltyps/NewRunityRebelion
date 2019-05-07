package io.server.game;

import io.server.game.world.World;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.position.Position;

public class Projectile {

	/** Magic combat projectile delays. */
	public static final int[] MAGIC_DELAYS = { 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5 };

	/** Ranged combat projectile delays. */
	public static final int[] RANGED_DELAYS = { 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4 };

	private int id;
	private int delay;
	private int duration;
	private int startHeight;
	private int endHeight;
	private int curve;

	public Projectile(int id, int delay, int duration, int startHeight, int endHeight) {
		setId(id);
		setDelay(delay);
		setDuration(duration);
		setStartHeight(startHeight);
		setEndHeight(endHeight);
		setCurve(16);
	}

	public Projectile(int id) {
		this(id, 52, 68, 43, 31);
	}

	public void send(Mob source, Mob target) {
		World.sendProjectile(source, target, this);
	}

	public void send(Mob source, Position target) {
		World.sendProjectile(source, target, this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public void setStartHeight(int startHeight) {
		this.startHeight = startHeight;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public void setEndHeight(int endHeight) {
		this.endHeight = endHeight;
	}

	public int getCurve() {
		return curve;
	}

	public void setCurve(int curve) {
		this.curve = curve;
	}

	public Projectile copy() {
		Projectile projectile = new Projectile(id, delay, duration, startHeight, endHeight);
		projectile.setCurve(curve);
		return projectile;
	}
}
