package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Position;

public class TeleCommand1 implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {

		System.out.println("here..");
		Teleportation.teleport(player, new Position(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
				Integer.parseInt(parts[3])));

		player.message("You have teleported to : " + parts[1] + " - " + parts[2] + " - " + parts[3]);
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Teleports your character to the specified coordinates.";
	}
}
