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

public class DisableDoubleXP implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
//		Config.DOUBLE_EXPERIENCE = false;
		World.getWorldEventHandler().clearFromAllEvents(WorldEventType.EXP);
		World.sendBroadcast(60, "[DAILY SERVER EVENTS] Double Experience has been deactivated!", false);
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
