package plugin.command.impl.donator;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;

public class NewDZoneCommandOne implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, new Position(1995, 4494));
		player.send(new SendMessage("You have teleported to the Donator Zone!"));
		//AllVsOne2.create(player);
	}

	@Override
	public boolean canUse(Player player) {
		return (PlayerRight.isDonator(player) || PlayerRight.isKing(player) || PlayerRight.isSupreme(player) || PlayerRight.isExtreme(player)
				|| PlayerRight.isElite(player) || PlayerRight.isDeveloper(player) ||  PlayerRight.isSuper(player));
	}

	@Override
	public String description() {
		return "Teleports you to the donator zone.";
	}
}
