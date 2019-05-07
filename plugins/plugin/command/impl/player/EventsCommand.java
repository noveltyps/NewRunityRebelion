package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.Utility;

public class EventsCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.sendMessage(Utility.highlightBlueText("The current active bonuses/events are listed below:"));
		player.sendEventInfo();
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Lists the current active events.";
	}
}
