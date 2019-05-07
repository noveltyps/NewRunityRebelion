package io.server.content.discord.commands;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import io.server.content.discord.DiscordCommands;

/**
 * User: Adam_#6723
 * Date: 24-02-2019
 * Project: RebelionX-OS
 */

public class DiscordListCommand implements MessageCreateListener {

    int count = 1;
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        EmbedBuilder embedBuilder = new EmbedBuilder().
                setTitle("Commands").setColor(Color.green);
        DiscordCommands.COMMANDS.forEach((a, b) -> {
            embedBuilder.addField(String.valueOf(count++) + ")", a);
        });
        messageCreateEvent.getChannel().sendMessage(embedBuilder);
    }
}
