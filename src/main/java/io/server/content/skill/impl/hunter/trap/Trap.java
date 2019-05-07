package io.server.content.skill.impl.hunter.trap;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.CustomGameObject;

public class Trap {

	/**
	 * The WorldObject linked to this HunterObject
	 */
	private CustomGameObject gameObject;

	/**
	 * The amount of ticks this object should stay for
	 */
	private int ticks;
	/**
	 * This trap's state
	 */
	private TrapState trapState;

	/**
	 * Reconstructs a new Trap
	 * 
	 * @param object
	 * @param state
	 */
	public Trap(CustomGameObject object, TrapState state, int ticks, Player owner) {
		this.gameObject = object;
		this.trapState = state;
		this.ticks = ticks;
		this.owner = owner;
	}

	/**
	 * Gets the GameObject
	 */
	public CustomGameObject getGameObject() {
		return gameObject;
	}

	/**
	 * @return the ticks
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Gets a trap's state
	 */
	public TrapState getTrapState() {
		return trapState;
	}

	/**
	 * Sets the GameObject
	 * 
	 * @param gameObject
	 */
	public void setGameObject(CustomGameObject gameObject) {
		this.gameObject = gameObject;
	}

	/**
	 * @param ticks the ticks to set
	 */
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	/**
	 * Sets a trap's state
	 * 
	 * @param state
	 */
	public void setTrapState(TrapState state) {
		trapState = state;
	}

	private Player owner;

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player player) {
		this.owner = player;
	}
}
