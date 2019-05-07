package io.server.content.discord.commands;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import io.server.content.discord.DiscordCommands;

/**
 * User: Adam_#6723
 * Date: 24-02-2019
 * Project: Brutal-OS
 */
public class DiscordHelpCommand implements MessageCreateListener {

    int count = 1;
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        EmbedBuilder embedBuilder = new EmbedBuilder().
                setTitle("Commands").setColor(Color.green)
                .addField("If you require assistance contact the staff team or the owner para", "have a great day fella.");        
        DiscordCommands.COMMANDS.forEach((a, b) -> {
        	
        });
        messageCreateEvent.getChannel().sendMessage(embedBuilder);
        System.err.println("User attempted to execute this");
    }
}
