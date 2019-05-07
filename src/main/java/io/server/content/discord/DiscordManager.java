package io.server.content.discord;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import io.server.game.world.entity.mob.player.Player;

/**
 * User: Adam_#6723 Date: 24-02-2019 Project: Brutal-OS
 */

public class DiscordManager {

	protected Player player;
	protected String title, message, channel;

	public DiscordManager(String channel, Player player, String title, String message) {
		this.channel = channel;
		this.player = player;
		this.title = title;
		this.message = message;
	}
	
	public DiscordManager(String channel, String title, String message) {
		this.channel = channel;
		this.title = title;
		this.message = message;
	}

	public void log() {
		EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(title).setColor(Color.green)
				.addField(player.getUsername() + "", message, true);
		Discord.api.getChannelById(channel).get().asTextChannel().get().sendMessage(embedBuilder);
	}
	
	public void log1() {
		EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(title).setColor(Color.green)
				.addField(title, message, true);
		Discord.api.getChannelById(channel).get().asTextChannel().get().sendMessage(embedBuilder);
	}
}
