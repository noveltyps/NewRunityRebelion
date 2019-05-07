package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.combat.strategy.npc.boss.galvek.GalvekUtility;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * @author Adam_#6723
 */

public class GalvekSpawnCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		GalvekUtility.generateSpawn();
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Spawns Galvek.";
	}
}
