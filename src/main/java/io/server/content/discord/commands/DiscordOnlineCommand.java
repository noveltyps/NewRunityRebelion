package io.server.content.discord.commands;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import io.server.game.world.World;

/**
 * User: Adam_#6723
 * Date: 24-02-2019
 * Project: RebelionX-OS
 */
public class DiscordOnlineCommand implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        EmbedBuilder embedBuilder = new EmbedBuilder().
                setTitle("Players online").
                addField("Current players online: ", String.valueOf(World.getPlayerCount()), true);
        messageCreateEvent.getChannel().sendMessage(embedBuilder);
    }
}
