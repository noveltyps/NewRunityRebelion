package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class AllToMeCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Position position = player.getPosition().copy();
		World.getPlayers().forEach(players -> {
			if (!players.equals(player)) {
				players.move(position);
				players.send(new SendMessage("You have been mass teleported by " + player.getName()));
			}
		});
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Teleports everyone online to you.";
	}
}
