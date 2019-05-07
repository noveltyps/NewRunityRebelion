package io.server.content.event.impl;

import io.server.content.event.InteractionEvent;
import io.server.game.world.entity.mob.Mob;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 16-6-2017.
 */
public final class OnKillEvent extends InteractionEvent {

	private final Mob victim;

	public OnKillEvent(Mob victim) {
		super(InteractionType.ON_KILL);
		this.victim = victim;
	}

	public Mob getVictim() {
		return victim;
	}
}
