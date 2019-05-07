package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendURL;

/**
 * @author Adam_#6723
 */

public class DiscordCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.send(new SendURL("https://discord.gg/u9Y4pXT"));
		player.send(new SendMessage("Launching BrutalOS Discord."));
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Opens the discord invite link.";
	}
}
