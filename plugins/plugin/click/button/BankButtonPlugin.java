package plugin.click.button;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.net.packet.out.SendTooltip;

public class BankButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (!player.interfaceManager.isInterfaceOpen(60_000) && !player.interfaceManager.isInterfaceOpen(4_465))
			return false;
		if (button >= -5505 && button <= -5468) {
			final int tab = (5505 + button) / 4;
			if (button % 2 == 0) {
				player.bank.bankTab = tab;
				player.send(new SendString("", 60019));
			} else {
				player.bank.collapse(tab, tab == 0);
				player.bank.refresh();
			}
			player.bank.sendValue();
			return true;
		}
		switch (button) {
		case -5462:
			player.bankPin.open();
			return true;
		case -5458:
			player.bankVault.value();
			return true;
		case -5459:
			player.send(new SendInputAmount("Enter the amount of coins you want to deposit:", 10,
					input -> player.bankVault.deposit(Integer.parseInt(input))));
			return true;
		case -5522:
			player.send(new SendInputAmount("Enter the amount of coins you want to withdraw:", 10,
					input -> player.bankVault.withdraw(Long.parseLong(input))));
			return true;
		case -5528:
			player.bank.depositeInventory();
			return true;
		case -5525:
			player.bank.depositeEquipment();
			return true;
		case -5463:
			player.bank.placeHolder = !player.bank.placeHolder;
			player.send(new SendConfig(116, player.bank.placeHolder ? 1 : 0));
			player.send(new SendTooltip((player.bank.placeHolder ? "Disable" : "Enable") + " place holders", 60073));
			return true;
		case -5464:
			int count = 0;
			boolean toggle = player.bank.placeHolder;
			player.bank.placeHolder = false;
			player.bank.setFiringEvents(false);
			for (Item item : player.bank.toArray()) {
				if (item != null && item.getAmount() == 0) {
					int slot = player.bank.computeIndexForId(item.getId());
					int tab = player.bank.tabForSlot(slot);
					player.bank.changeTabAmount(tab, -1);
					player.bank.remove(item);
					player.bank.shift();
					count++;
				}
			}
			player.bank.placeHolder = toggle;
			player.bank.setFiringEvents(true);
			player.bank.refresh();
			player.send(new SendMessage(count == 0 ? "There are no place holders available for you to release."
					: "You have released " + count + " place holders."));
			return true;
		case -5530:
			player.bank.inserting = !player.bank.inserting;
			player.send(new SendConfig(304, player.bank.inserting ? 1 : 0));
			player.send(new SendTooltip((player.bank.inserting ? "Enable swapping" : "Enable inserting"), 60006));
			return true;
		case -5529:
			player.bank.noting = !player.bank.noting;
			player.send(new SendConfig(115, player.bank.noting ? 1 : 0));
			player.send(new SendTooltip((player.bank.noting ? "Disable" : "Enable") + " noting", 60007));
			return true;
		}
		return false;
	}
}
