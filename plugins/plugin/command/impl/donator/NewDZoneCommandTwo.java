package plugin.command.impl.donator;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
//kk, teleports are in, just need to go in and choose which zone for which rank kk where do I do that
public class NewDZoneCommandTwo implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, new Position(1999, 4537));
		player.send(new SendMessage("You have teleported to the Donator Zone!"));
		//AllVsOne2.create(player);
	}

	@Override
	public boolean canUse(Player player) {
		return (PlayerRight.isDonator(player) || PlayerRight.isKing(player) || PlayerRight.isSupreme(player) || PlayerRight.isExtreme(player)
				|| PlayerRight.isElite(player) || PlayerRight.isDeveloper(player) ||  PlayerRight.isSuper(player));
	}
// ty i see np, needs restart for the commands or you can just teleport to the positions using ::tele for now yea ty I got another issue but dont wanna bother u xd
	//don't forget we're using the same source lmao if i  ikfix an issue for you it's probably gonna apply to me , basically I made this raid store right ah kk , ,issue is when dani pushed it deleted it , is there a way on github to get it back
	
	@Override
	public String description() {
		return "Teleports you to the donator zone.";
	}
}
