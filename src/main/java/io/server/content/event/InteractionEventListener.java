package io.server.content.event;

import io.server.game.world.entity.mob.player.Player;

public interface InteractionEventListener {

	boolean onEvent(Player player, InteractionEvent interactionEvent);
}