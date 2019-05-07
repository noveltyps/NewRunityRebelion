package io.server.content.discord.commands;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import io.server.content.worldevent.WorldEvent;
import io.server.game.world.World;

public class DiscordEventsCommand implements MessageCreateListener {

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setTitle("Currently active bonuses & events:");
		
		if (World.getWorldEventHandler().getActiveEvents().size() <= 0)
			builder.setDescription("No active bonuses or events!");
		else
			for (WorldEvent e : World.getWorldEventHandler().getActiveEvents())
				builder.addField(e.getType().getName(), "Ending in: " + e.getFormattedSeconds() + "!");
		
		event.getChannel().sendMessage(builder);
		
	}

}
