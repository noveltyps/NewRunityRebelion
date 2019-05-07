package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.region.dynamic.DynamicRegion;
import io.server.game.world.region.dynamic.DynamicRegion.RegionType;


public class ZulrahCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.setDynamicRegion(new DynamicRegion(player, RegionType.ZULRAH));
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Teleports you to Zulrah.";
	}
}
