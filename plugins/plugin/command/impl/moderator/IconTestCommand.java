package plugin.command.impl.moderator;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.worldevent.WorldEventType;
import io.server.game.world.World;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * 
 * @author Adam_#6723
 * @updated Neytorokx#8707
 *
 */

public class IconTestCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {

		player.headIcon = player.right.getCrown() + 19;
		player.updateFlags.add(UpdateFlag.APPEARANCE);
		
	}
	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isPriviledged(player);
	}
	@Override
	public String description() {
		return "";
	}

}
