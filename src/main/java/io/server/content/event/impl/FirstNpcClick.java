package io.server.content.event.impl;

import io.server.game.world.entity.mob.npc.Npc;

public class FirstNpcClick extends NpcInteractionEvent {

	public FirstNpcClick(Npc npc) {
		super(InteractionType.FIRST_CLICK_NPC, npc, 0);
	}
}
