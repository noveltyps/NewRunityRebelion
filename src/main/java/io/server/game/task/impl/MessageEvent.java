package io.server.game.task.impl;

import io.server.Config;
import io.server.content.triviabot.TriviaBot;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.util.Utility;

/**
 * Sends game messages to all the online players.
 *
 * @author Daniel
 */
public class MessageEvent extends Task {

	/** The message randomevent ticks. */
	private int tick;

	/** Constructs a new <code>MessageEvent</code>. */
	public MessageEvent() {
		super(180);
		this.tick = 0;
	}

	@Override
	public void execute() {
		tick++;

		if (tick % 2 == 0) {
			String message = Utility.randomElement(Config.MESSAGES);
			if (message.length() < 85) {
				World.sendMessage("@red@" + message);
			} else {
				World.sendMessage("@red@" + message.substring(0, 84));
				World.sendMessage("@red@" + message.substring(84));
			}
		} else {
			TriviaBot.assign();
		}
	}
}
