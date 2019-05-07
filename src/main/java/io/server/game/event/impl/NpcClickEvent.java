package io.server.game.event.impl;

import io.server.game.event.Event;
import io.server.game.world.entity.mob.npc.Npc;

public class NpcClickEvent implements Event {

	private final int type;

	private final Npc npc;

	public NpcClickEvent(int type, Npc npc) {
		this.type = type;
		this.npc = npc;
	}

	public int getType() {
		return type;
	}

	public Npc getNpc() {
		return npc;
	}

}
