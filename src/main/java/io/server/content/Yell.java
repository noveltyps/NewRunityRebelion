package io.server.content;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.punishments.PunishmentExecuter;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Handles the yelling command.
 *
 * @author Daniel
 */
public class Yell {

	/** Array of all invalid strings. */
	private static final String[] INVALID = { ".com", "@cr", "<img=", "</col", "<col=", "@whi@", "@blu@", "@gre@",
			"@red@", "@mag@", "@cya@" };

	/** Yells a message to the server. */
	public static void yell(Player player, String message) {

		if (PunishmentExecuter.muted(player.getUsername()) || PunishmentExecuter.IPMuted(player.lastHost)) {
			player.send(new SendMessage("You are muted and cannot chat."));
			return;
		}

		if (!PlayerRight.isDonator(player)) {
			player.send(new SendMessage("You must be a donator to use this command!"));
			return;
		}

		if (!player.settings.yell) {
			player.send(new SendMessage("You can not send a yell message as you have the yell setting disabled!"));
			return;
		}

		if (Arrays.stream(INVALID).anyMatch(message::contains)) {
			player.send(new SendMessage("Your message contains invalid characters."));
			return;
		}

		final String prefix = "[<col=" + player.right.getColor() + ">" + player.right.getName() + "</col>] <col="
				+ player.right.getColor() + ">" + player.getName();
		final String formatted_message = prefix + "</col>: " + Utility.capitalizeSentence(message);
		World.sendMessage(formatted_message, exception -> exception.settings.yell);
		player.yellDelay.reset();
	}
}
