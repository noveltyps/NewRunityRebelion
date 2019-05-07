package plugin.click.button;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class MasterMinerButtonPlugin extends PluginContext {
	private final static int INCREMENT = 10;

	@Override
	protected boolean onClick(Player player, int button) {
		switch (button) {
		case 24311:
			player.masterMinerData.totalGold++;
			player.masterMinerData.totalClicks++;
			// Click button
			return true;
		case 24337:
			// New Task/Claim reward Button
			if (player.masterMinerTask.getCurrentTask().isComplete()) {
				player.masterMinerTask.claimReward(player);
				return true;
			}
			player.masterMinerTask.obtainTask(player);
			return true;
		case 24403:
		case 24403 + INCREMENT:
		case 24403 + INCREMENT * 2:
		case 24403 + INCREMENT * 3:
		case 24403 + INCREMENT * 4:
		case 24403 + INCREMENT * 5:
		case 24403 + INCREMENT * 6:
		case 24403 + INCREMENT * 7: // I KNOW THERE ARE BETTER WAYS FUCK OFF

			player.masterMinerData.mobData[(button - 24403) / INCREMENT].buy(player);
			return true;
		case 24341:
			increaseAmountToBuy(player);
			player.masterMiner.updateAmountToBuy();
			// Increase amount to buy
			return true;
		case 24345:
			decreaseAmountToBuy(player);
			player.masterMiner.updateAmountToBuy();
			// Decrease amount to buy
			return true;
		case 24347:
			player.adventure.open();
			// Go on an adventure
			return true;
		case 24660:
			// Prestige Button
			return true;
		case 24360:
			// Achievements Button
			player.masterMiner.achievements.open();
			return true;
		case 29003:
			// Close Achievements
			player.interfaceManager.close();
			return true;
		}
		return false;
	}

	private void increaseAmountToBuy(Player player) {
		switch (player.masterMinerData.amountToBuy) {
		case 0:
		case 1:
			player.masterMinerData.amountToBuy = 5;
			return;
		case 5:
			player.masterMinerData.amountToBuy = 10;
			return;
		case 10:
			player.masterMinerData.amountToBuy = 50;
			return;
		case -1:
		case 50:
			player.masterMinerData.amountToBuy = -1;
			return;
		}
	}

	private void decreaseAmountToBuy(Player player) {
		switch (player.masterMinerData.amountToBuy) {
		case -1:
			player.masterMinerData.amountToBuy = 50;
			return;
		case 50:
			player.masterMinerData.amountToBuy = 10;
			return;
		case 10:
			player.masterMinerData.amountToBuy = 5;
			return;
		case 0:
		case 5:
			player.masterMinerData.amountToBuy = 1;
			return;
		}
	}
}