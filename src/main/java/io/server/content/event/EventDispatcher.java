package io.server.content.event;

import io.server.content.activity.Activity;
import io.server.content.event.InteractionEvent.InteractionType;
import io.server.game.world.entity.mob.player.Player;

public class EventDispatcher {

	private final InteractionEvent interactionEvent;

	public EventDispatcher(InteractionEvent interactionEvent) {
		this.interactionEvent = interactionEvent;
	}

	public static boolean execute(Player player, InteractionEvent event) {
		return player.inActivity() && Activity.evaluate(player, it -> it.onEvent(player, event)) || player.quest.onEvent(event) || player.skills.onEvent(event);
	}

	public void dispatch(InteractionType type, EventHandler eventHandler) {
		if (interactionEvent.isHandled()) {
			return;
		}

		if (interactionEvent.getType() == type) {
			interactionEvent.setHandled(eventHandler.handle(interactionEvent));
		}
	}

}
