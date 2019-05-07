package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.AccountSecurity;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

/**
 * @author Adam_#6723
 */

public class KeyCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
	       if (player.right != PlayerRight.HELPER && !PlayerRight.isManagement(player)) {
               return;
           }

			final String key = String.format(parts[1]);

               if (!player.locking.locked()) {
                   player.send(new SendMessage("Your account is not locked, there is no need to enter a key.", MessageColor.RED));
                   
                   return;
               }

               if (AccountSecurity.AccountData.forName(player.getName()).isPresent()) {
                   AccountSecurity.AccountData account = AccountSecurity.AccountData.forName(player.getName()).get();

                   if (account.getKey().equalsIgnoreCase(key)) {
                       player.send(new SendMessage("You have entered the assigned security key for this account. Your session was", MessageColor.RED));
                       player.send(new SendMessage("activated.", MessageColor.RED));
                       player.move(Config.DEFAULT_POSITION);
                       player.locking.unlock();
                       player.setVisible(true);
                   } else {
                       player.send(new SendMessage("You have entered the wrong key! Information has been logged and all available staff", MessageColor.RED));
                       player.send(new SendMessage("gameMembers were notified.", MessageColor.RED));
                   }
               }
           }
       
	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Enters the specified login key.";
	}
}
