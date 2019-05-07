package plugin.command.impl.moderator;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.worldevent.WorldEventType;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * 
 * @author Adam_#6723
 * @updated Neytorokx#8707
 *
 */

public class DisableDoubleDrops implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
//		Config.DOUBLE_DROPS = false;
		World.getWorldEventHandler().clearFromAllEvents(WorldEventType.DROP_RATE);
		World.sendBroadcast(60, "[DAILY SERVER EVENTS] Double Drop Rate has been deactivated!", false);
	}
	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isPriviledged(player);
	}
	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "";
	}

}
