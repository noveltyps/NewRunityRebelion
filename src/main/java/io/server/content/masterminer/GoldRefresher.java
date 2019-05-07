package io.server.content.masterminer;

import io.server.game.world.entity.mob.player.Player;

public class GoldRefresher extends Thread {
	private Player player;

	public GoldRefresher(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		while (player.interfaceManager.isInterfaceOpen(24300)) {
			player.masterMiner.updateInterface();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
