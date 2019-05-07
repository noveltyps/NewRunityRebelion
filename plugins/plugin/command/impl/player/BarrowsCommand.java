package plugin.command.impl.player;

import io.server.content.activity.impl.barrows.Barrows;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;

public class BarrowsCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, new Position(3565, 3315, 0), 20, () -> Barrows.create(player));
		player.send(new SendMessage("@or2@Welcome to Barrows, " + player.getName() + "!"));
		player.send(new SendMessage("@red@Goodluck with grinding!"));
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Teleports you to the Barrows Minigame.";
	}
}
