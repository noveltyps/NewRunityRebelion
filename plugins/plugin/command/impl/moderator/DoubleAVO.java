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
 *
 */

public class DoubleAVO implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
//		Config.DOUBLE_AVO_POINTS = true;
		World.eventHandler.addEvent(new WorldEvent(WorldEventType.AVO, 0, 24).setModifier(2));
		World.sendBroadcast(60,"[DAILY SERVER EVENTS] Double Tickets at the AvO Minigame is now activated for 24 hours!", false);
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
