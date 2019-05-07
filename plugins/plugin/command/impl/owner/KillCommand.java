package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * @author Adam_#6723
 */

public class KillCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1]);

		World.search(name.toString()).ifPresent(other -> other.damage(new Hit(other.getCurrentHealth())));

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Kills the specified player.";
	}
}
