package io.server.game.task.impl;

import io.server.Config;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.net.discord.Discord;
import io.server.util.Utility;

/**
 * Sends an update message to the discord channel.
 *
 * @author Daniel
 */
public class DiscordEvent extends Task {

	/** Constructs a new <code>DiscordEvent</code>. */
	public DiscordEvent() {
		super(10000);
	}

	@Override
	protected boolean canSchedule() {
		return !Config.LIVE_SERVER;
	}

	@Override
	public void execute() {
		int size = World.getPlayerCount();
		if (size >= 5) {
			String message = "There are currently " + size + " players online. [Uptime=" + Utility.getUptime() + " ]";
			Discord.communityMessage(message);
		}
	}
}
