package io.server.content.event.impl;

import io.server.game.world.entity.mob.npc.Npc;

public class SecondNpcClick extends NpcInteractionEvent {

	public SecondNpcClick(Npc npc) {
		super(InteractionType.SECOND_CLICK_NPC, npc, 1);
	}
}
