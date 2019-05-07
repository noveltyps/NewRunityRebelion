package io.server.content.upgrading;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendString;

/**
 * Display Execution
 * @author Nerik#8690
 *
 */
public class UpgradeDisplay {

	private Player player;
	private Item item;
	
	public UpgradeDisplay(Player player) {
		this.player = player;
		this.item = null;
	}
	
	public UpgradeDisplay(Player player, Item item) {
		this.player = player;
		this.item = item;
	}
	
	public void execute() {
		refreshPanel();
		player.interfaceManager.open(48270);
		player.send(new SendItemOnInterface(48275, UpgradeData.ITEMS));
	}
	
	public void select() {
		for(UpgradeData data : UpgradeData.values()) {
			if(data.getItemReward().equalIds(item)) {
				player.setUpgradeSelected(data);
				player.send(new SendItemOnInterface(48276, item));
				player.send(new SendItemOnInterface(48283, data.getItemInput()));
				player.send(new SendItemOnInterface(48284, data.getEtharRequirement()));
				player.send(new SendString("Chance " + (int) (data.getChance() * 100) + "/100%", 48292));
			}
		}
	}
	
	private void refreshPanel() {
		player.send(new SendItemOnInterface(48276));
		player.send(new SendItemOnInterface(48283));
		player.send(new SendItemOnInterface(48284));
		player.send(new SendString("Chance 0/100%", 48292));
	}

}
