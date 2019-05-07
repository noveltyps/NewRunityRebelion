package plugin.command.impl.owner;
import io.server.content.command.Command;
import io.server.game.world.entity.combat.strategy.npc.boss.Chimera;
import io.server.game.world.entity.combat.strategy.npc.boss.chimera.ChimeraUtility;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;;

public class ChimeraSpawnCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		ChimeraUtility.generateSpawn();
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Spawns Chimera.";
	}
}
