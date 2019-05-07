package plugin.command.impl.player;

import io.server.Config;
import io.server.content.activity.impl.barrows.Barrows;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

/**
 * @author Adam_#6723
 */

public class BarrowsFix implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, Config.BARROWS_CHEST);
		player.send(new SendMessage("Welcome to Barrows fix"));
		player.send(new SendMessage("Use the inconspicuous hole to exit barrows."));
		Barrows barrow = new Barrows(player);
		Barrows.ResetBarrows(player);
		barrow.finish();
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}
	@Override
	public String description() {
		return "Teleports you to the barrows chest.";
	}

}
