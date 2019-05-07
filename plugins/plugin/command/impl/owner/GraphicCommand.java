package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.Graphic;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

/**
 * @author Adam_#6723
 */

public class GraphicCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		int id = Integer.parseInt(parts[1]);
		player.graphic(new Graphic(id));
		player.send(new SendMessage("Performing graphic = " + id));

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Performs the specified gfx.";
	}
}
