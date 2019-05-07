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

public class Enable30DR implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
//		Config.DR_30_BOOST = true;
		World.getWorldEventHandler().addEvent(new WorldEvent(WorldEventType.DROP_RATE_BOOST, 0, 24).setModifier(30));
		World.sendBroadcast(60, "[DAILY SERVER EVENTS] 30% Drop Rate Boost is now activated for 24 hours!", false);
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
