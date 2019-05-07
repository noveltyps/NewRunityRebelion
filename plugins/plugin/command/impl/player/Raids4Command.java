package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;

/**
 * @author Adam_#6723
 */

public class Raids4Command implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {

		player.dialogueFactory.sendOption("@red@Teleport me", () -> {

			player.move(new Position(2527, 2894, player.getHeight()));
				player.message("You have teleported to Raids 4");

		}, "Cancel", () -> {

			player.dialogueFactory.clear();
		}).execute();
	}
	

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Teleports you to Raids 4.";
	}
}
