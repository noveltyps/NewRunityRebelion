package io.server.content.discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import io.server.Config;


/**
 * User: Adam_#6723
 * Date: 24-02-2019
 * Project: RebelionX-OS
 */

public class DiscordMassMessage {

	public static DiscordApi api;

	public static long id = 420358997288419338L;

	public static long Adam = 400026363924316160L;

	public static void start() {
		api = new DiscordApiBuilder().setToken(DiscordConstant.BOT_KEY).login().join();
		EmbedBuilder embed = new EmbedBuilder().setTitle("Venom Updates!");
				/*.addField("We have finally launched the forums!",
						"You can visit it here: http://venomps.io/community/index.php?/register/")
				.addField("We are also hosting a forums event! All you have to do is sign up to enter", "http://venomps.io/community/index.php?/topic/77-venoms-official-forum-event/")
				.addField("Check out our updates and progression", "http://venomps.io/community/index.php?/topic/28-mediashowcasing-features-of-venom-1-near-reality-remake/&tab=comments#comment-29")
				.addField("We're also hosting a 250 Million OSRS GP and 50$ Paypal Giveaway!", "Check #Announcements for more information!")
				.addField("Can't wait to deliver this server to you guys!", "<3").setUrl("http://venomps.io/community/index.php?/register/");*/
		api.getChannelById("534858714440794128").get().asTextChannel().get().sendMessage(embed);
		api.getServerById(id).get().getMembers().forEach(user -> {
			if(user == null || user != null) {
				user.getMentionTag();
				user.sendMessage("**We have finally Launched the forums!**");
				user.sendMessage("`You can visit it here: `http://venomps.io/community/index.php?/register/");
				user.sendMessage("`We are also hosting a forums event! All you have to do is sign up to enter.`");
				user.sendMessage("http://venomps.io/community/index.php?/topic/77-venoms-official-forum-event/");
				user.sendMessage("`We're also hosting a 250 Million OSRS GP and 50$ Paypal Giveaway! Check #Announcements for more information!`");
				user.sendMessage("`We cannot wait to deliver and launch venom! It's going to be a blast!!`"); 
				System.err.println("Sent To: " + user.getName());
			}
		});
		api.addMessageCreateListener(event -> {
			DiscordCommands.FINAL_COMMAND.forEach((command, execution) -> {
				if (command.equalsIgnoreCase(event.getMessage().getContent())) {
					execution.onMessageCreate(event);
				}
			});
		});
	}

	public static void main(String[] args) {
		if (!Config.DISCORD_MASS_MESSAGE) {
			return;
		}
		start();
	}
}


/* 
				user.sendMessage("We have finally Launched the forums!");
				user.sendMessage("You can visit it here: http://venomps.io/community/index.php?/register/");
				user.sendMessage("We are also hosting a forums event! All you have to do is sign up to enter.");
				user.sendMessage("http://venomps.io/community/index.php?/topic/77-venoms-official-forum-event/");
				user.sendMessage("We're also hosting a 250 Million OSRS GP and 50$ Paypal Giveaway! Check #Announcements for more information!");
				user.sendMessage("We cannot wait to deliver and launch venom! It's going to be a blast!!");

*/