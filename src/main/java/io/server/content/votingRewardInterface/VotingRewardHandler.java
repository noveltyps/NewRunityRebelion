package io.server.content.votingRewardInterface;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendItemOnInterface;

/**
 * 
 * @author Adam_#6723
 * Voting Interface Handler
 */

public class VotingRewardHandler {
	
	protected Player player;
	
	public VotingRewardHandler(Player player) {
		this.player = player;
	}
	
	int array[] = {995, 13442, 2528, 989, 6199, 455};
	int amount[] = {1000000, 200, 1, 1, 1, 1};

	public void Open() {
		if(player == null) {
			return;
		}		
		if(!player.inventory.contains(new Item(7478, 1))) {
			return;
		}	
			if (array != null) {
				Item items[] = new Item[array.length];
				for (int index = 0, count = 0; index < array.length; index++, count++) {
					Item item = new Item(array[index], amount[index]);
					items[count] = item;
				}
				player.send(new SendItemOnInterface(16426, items));
			}
		player.interfaceManager.open(16400);
	}
}
