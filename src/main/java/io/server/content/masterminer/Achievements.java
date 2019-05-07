package io.server.content.masterminer;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendItemOnInterface;

public class Achievements {
	private Player player;

	public Achievements(Player player) {
		// Initialize everything to false
		this.player = player;
	}

	public void open() {
		player.interfaceManager.close();
		player.interfaceManager.open(29000);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++) {
				Item item = new Item(995 + i * 8 + j);
				player.send(new SendItemOnInterface(29006 + i * 8 + j, item));
			}
		}
	}

	public void clickedOn(int removeSlot, int removeId, int interfaceId) {
		System.out.println("remooveSlot:" + removeSlot + "removeId:" + removeId + "interfaceId:" + interfaceId);
	}
}
