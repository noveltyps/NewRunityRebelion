package io.server.content.discord.commands;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

/**
 * User: Adam_#6723
 * Date: 24-02-2019
 * Project: RebelionX-OS
 */
public class DiscordPriceCommand implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Giveaway").
                addField("React with party popper!", "").
                addField("Time left", "1 Minute", true);
            messageCreateEvent.getChannel().getMessagesAsStream().forEach(p -> {
                System.out.println(p);
            });

    }
}
