package io.server.net.discord;

import io.server.game.world.World;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * The discord dispatcher.
 *
 * @author Daniel
 */
public class DiscordDispatcher {

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event) throws DiscordException, MissingPermissionsException {
		IMessage message = event.getMessage();
		long channel = event.getChannel().getLongID();
		//String name = event.getAuthor().getName();
		String command = message.getContent();

		if (Discord.community == null && channel == Discord.COMMUNITY_CHANNEL) {
			Discord.community = event.getChannel();
		}

		if (command.equals("::players")) {
			Discord.communityMessage("There are currently " + World.getPlayerCount() + " players online!");
		} else if (command.equals("::staffonline")) {
			Discord.communityMessage("There are currently " + World.getStaffCount() + " staff online!");

		}
	}

}