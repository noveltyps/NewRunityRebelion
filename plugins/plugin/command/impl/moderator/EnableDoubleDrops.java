package plugin.command.impl.moderator;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.worldevent.WorldEvent;
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

public class EnableDoubleDrops implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
//		Config.DOUBLE_DROPS = true;
		World.getWorldEventHandler().addEvent(new WorldEvent(WorldEventType.DROP_RATE, 0, 1).setModifier(2));
		World.sendBroadcast(60, "[DAILY SERVER EVENTS] Double Drops is now activated for 1 Hour!", false);
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
