package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.content.worldevent.WorldEventType;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

public class DisablePKP implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		World.getWorldEventHandler().clearFromAllEvents(WorldEventType.PKP);
		World.sendBroadcast(60, "[DAILY SERVER EVENTS] Double PK Points has been deactivated!", false);
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isPriviledged(player);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
