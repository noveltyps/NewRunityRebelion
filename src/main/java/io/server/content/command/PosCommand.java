package io.server.content.command;

import io.server.content.store.impl.PersonalStore;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

public class PosCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {

		switch (Integer.valueOf(parts[1])) {

		case 1:
			PersonalStore.openPanel(player);
			return;

		case 2:
			player.personalStore.claimCoins(player);
			return;

		case 3:
			player.interfaceManager.open(38300);
			return;

		case 4:
			PersonalStore.myShop(player);
			return;

		case 5:
			PersonalStore.openMenu(player);
			return;
		}
	}

	@Override
	public boolean canUse(Player player) {
		// TODO Auto-generated method stub
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Opens the player owned shops.";
	}

}
