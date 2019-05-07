package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendLogout;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class KickAllCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		for (Player players : World.getPlayers()) {
			if(PlayerRight.isDeveloper(players)) {
				System.out.println("Didn't kick " + players.getName() + " because he is a developer!");
				return;
			}
         if(players != null) {
        	 if(players.isBot) {
        		 continue;
        	 }
 			players.send(new SendLogout());
 			World.queueLogout(players);
     		player.send(new SendMessage("@or2@You have kicked " + players.getName() + "!"));
           // players.logout(true);
         }
         
		}
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Kicks all online players offline.";
	}
}
