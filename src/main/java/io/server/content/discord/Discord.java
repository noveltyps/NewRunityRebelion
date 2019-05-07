package io.server.content.discord;


import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

/**
 * User: Adam_#6723
 * Date: 24-02-2019
 * Project: RebelionX-OS
 */

public class Discord {

    public static DiscordApi api;
    
    public final long id = 468529686952149004L;

    public void start() {
        api = new DiscordApiBuilder().setToken(DiscordConstant.BOT_KEY).login().join();
        new DiscordCommands();
		EmbedBuilder embed = new EmbedBuilder()
                .setTitle("RebelionX OS")
                .addField("Client Id", String.valueOf(api.getClientId()), true)
                .addField("Channels", String.valueOf(api.getChannels().size()), true)
                .addField("Status", String.valueOf(api.getStatus()), true)
                .addField("Commands Loaded", String.valueOf(DiscordCommands.execute()), true)
                .addField("Total Server Member Count: ", String.valueOf(api.getServerById(id).get().getMemberCount()), true)
                .addField("Prefix", ">[COMMAND]", true);
        api.getChannelById("548993182189223938").get().asTextChannel().get().sendMessage(embed);
        api.addMessageCreateListener(event -> {
            DiscordCommands.FINAL_COMMAND.forEach((command, execution) -> {
                if(command.equalsIgnoreCase(event.getMessage().getContent())) {
                    execution.onMessageCreate(event);
                }
            });
        });
    }
}
