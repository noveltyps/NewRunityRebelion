package plugin.command.impl.donator;

import io.server.Config;

import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

/* public class DZoneCommand implements Command {

	/*@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, Config.DONATOR_ZONE);
		player.send(new SendMessage("You have teleported to the Donator Zone!"));
		//AllVsOne2.create(player);
	} */
   
	/*
	@Override
	public boolean canUse(Player player) {
		return (PlayerRight.isDonator(player) || PlayerRight.isKing(player) || PlayerRight.isSupreme(player) || PlayerRight.isExtreme(player)
				|| PlayerRight.isElite(player) || PlayerRight.isDeveloper(player));
	}

	@Override
	public String description() {
		return "Teleports you to the donator zone.";
		
	}
}
*/
