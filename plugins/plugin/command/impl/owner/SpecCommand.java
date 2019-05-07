package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.combat.strategy.player.special.CombatSpecial;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * 
 * @author Adam_#6723
 *
 */

public class SpecCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		CombatSpecial.restore(player, 10000);
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}
	@Override
	public String description() {
		return "Gives you 10000 special attack.";
	}
}
