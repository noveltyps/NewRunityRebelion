package io.server.content.discord;

import java.util.HashMap;
import java.util.Map;

import org.javacord.api.listener.message.MessageCreateListener;

import io.server.content.discord.commands.DiscordEventsCommand;
import io.server.content.discord.commands.DiscordHelpCommand;
import io.server.content.discord.commands.DiscordHelpfulLinksCommand;
import io.server.content.discord.commands.DiscordListCommand;
import io.server.content.discord.commands.DiscordOnlineCommand;

/**
 * User: Adam_#6723
 * Date: 24-02-2019
 * Project: RebelionX-OS
 */

public class DiscordCommands {

    public static Map<String, MessageCreateListener> COMMANDS = new HashMap<>();
    public static Map<String, MessageCreateListener> FINAL_COMMAND = new HashMap<>();

    public static int execute() {

        COMMANDS.putIfAbsent("online", new DiscordOnlineCommand());
        COMMANDS.putIfAbsent("commands", new DiscordListCommand());
        COMMANDS.putIfAbsent("help", new DiscordHelpCommand());
        COMMANDS.putIfAbsent("links", new DiscordHelpfulLinksCommand());
        COMMANDS.putIfAbsent("players", new DiscordOnlineCommand());
        COMMANDS.putIfAbsent("events", new DiscordEventsCommand());

        COMMANDS.forEach((com, exe) -> {
            FINAL_COMMAND.putIfAbsent(DiscordConstant.PREFIX + com, exe);
        });

        return FINAL_COMMAND.size();
    }
}
