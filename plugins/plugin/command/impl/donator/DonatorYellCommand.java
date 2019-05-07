package plugin.command.impl.donator;

import io.server.content.Yell;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */
public class DonatorYellCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		try {
			final String message = command.substring(4, command.length());
			Yell.yell(player, message);
		} catch (final Exception e) {
			player.send(new SendMessage("@or2@Invalid yell format, syntax: -messsage"));
		}
	}

	@Override
	public boolean canUse(Player player) {
		return (PlayerRight.isDonator(player) || PlayerRight.isKing(player) || PlayerRight.isSupreme(player) || PlayerRight.isExtreme(player)
				|| PlayerRight.isElite(player) || PlayerRight.isDeveloper(player));
	}

	@Override
	public String description() {
		return "Sends a message to all players.";
	}

}
