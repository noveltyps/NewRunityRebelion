package io.server.content.discord.commands;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

/**
 * User: Adam_#6723
 * Date: 24-02-2019
 * Project: Brutal-OS
 */

public class DiscordHelpfulLinksCommand implements MessageCreateListener {

    int count = 1;
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        EmbedBuilder embedBuilder = new EmbedBuilder().
                setTitle("Commands").setColor(Color.green)
                .addField("Forums Link: http://brutalos.org/", "Client Download Link: https://www.dropbox.com/s/imf245ag9a7aemr/BrutalOS.jar?dl=1")
                .addField("Store Link: https://www.brutalos.org/store", "Vote Link: https://www.brutalos.org/vote");        
        	messageCreateEvent.getChannel().sendMessage(embedBuilder);
        System.err.println("User attempted to execute this");
    }
}
