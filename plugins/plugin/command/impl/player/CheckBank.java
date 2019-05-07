package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.InterfaceConstants;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

/**
 * 
 * @author Adam_#6723
 *
 */

public class CheckBank implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String name = String.format(parts[1].replaceAll("_", " "));
		Player other = World.getPlayerByName(name);
        handle(player, other);
		
	}

	public void handle(Player player, Player other) {
		player.interfaceManager.openInventory(60000, 5063);
		player.send(new SendItemOnInterface(InterfaceConstants.WITHDRAW_BANK, other.bank.tabAmounts,
				other.bank.toArray()));
		player.send(new SendItemOnInterface(InterfaceConstants.INVENTORY_STORE, other.inventory.toArray()));
		player.send(new SendMessage(
				"You have checked " + other.getName() + ". Use ::refresh to revert your inventory/bank to normal.",
				MessageColor.DARK_BLUE));
	}
	
	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Checks the specified player's bank.";
	}
}
