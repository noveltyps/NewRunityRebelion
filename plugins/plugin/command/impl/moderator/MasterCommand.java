package plugin.command.impl.moderator;

import io.server.content.achievement.AchievementHandler;
import io.server.content.command.Command;
import io.server.content.emote.EmoteHandler;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

/**
 * @author Adam_#6723
 */

public class MasterCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.skills.master();
		AchievementHandler.completeAll(player);
		EmoteHandler.unlockAll(player);
		player.send(new SendMessage("Your account is now maxed out.", MessageColor.BLUE));

	}

	@Override
	public boolean canUse(Player player) {
           return (PlayerRight.isDeveloper(player));
	}
	@Override
	public String description() {
		return "Sets all of your skills to 99.";
	}
}
