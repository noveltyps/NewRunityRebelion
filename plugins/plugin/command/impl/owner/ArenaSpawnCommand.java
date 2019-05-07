package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.combat.strategy.npc.boss.arena.ArenaUtility;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * @author Adam_#6723
 */

public class ArenaSpawnCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		ArenaUtility.generateSpawn();
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "";
	}
}
