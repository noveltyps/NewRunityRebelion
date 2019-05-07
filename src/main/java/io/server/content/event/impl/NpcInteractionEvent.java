package io.server.content.event.impl;

import io.server.content.event.InteractionEvent;
import io.server.game.world.entity.mob.npc.Npc;

public class NpcInteractionEvent extends InteractionEvent {

	private final Npc npc;
	private final int opcode;

	public NpcInteractionEvent(InteractionType type, Npc npc, int opcode) {
		super(type);
		this.npc = npc;
		this.opcode = opcode;
	}

	public Npc getNpc() {
		return npc;
	}

	public int getOpcode() {
		return opcode;
	}

}
