package plugin.command.impl.owner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.server.content.command.Command;
import io.server.content.worldevent.WorldEvent;
import io.server.content.worldevent.WorldEventType;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

public class StartEventCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		try {
			Calendar calendar = new GregorianCalendar();
			String type = parts[1].toUpperCase();
			int days = Integer.parseInt(parts[2]);
			int hours = Integer.parseInt(parts[3]);
			double modifier = 1.0;
			if (parts.length > 4)
				modifier = Integer.parseInt(parts[4]);
			WorldEvent event = new WorldEvent(WorldEventType.valueOf(type), calendar.getTime(), days, hours, modifier);
			World.eventHandler.addEvent(event);
		} catch (Exception e) {
			player.sendMessage("Use as ::event-type-days-hours-modifier");
		}
	}
	
	@Override
	public String description() {
		return "";
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

}
