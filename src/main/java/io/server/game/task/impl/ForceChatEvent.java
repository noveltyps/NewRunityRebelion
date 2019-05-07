package io.server.game.task.impl;

import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.parser.impl.NpcForceChatParser.ForcedMessage;

/**
 * An randomevent which starts the forced message randomevent.
 *
 * @author Daniel | Obey
 */
public class ForceChatEvent extends Task {

	private final Npc npc;

	private final ForcedMessage forcedMessage;

	public ForceChatEvent(Npc npc, ForcedMessage forcedMessage) {
		super(forcedMessage.getInterval());
		this.npc = npc;
		this.forcedMessage = forcedMessage;
	}

	@Override
	public void execute() {
		if (npc == null || !World.getNpcs().contains(npc)) {
			cancel();
			return;
		}

		npc.speak(forcedMessage.nextMessage());
	}

}
