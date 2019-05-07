package io.server.content.masterminer;

import static io.server.content.masterminer.MasterMinerTaskHandler.SIXHOUR;
import static io.server.content.masterminer.Util.toNiceString;
import static io.server.content.masterminer.Util.toTime;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;
import io.server.net.packet.out.SendTooltip;

public class MasterMinerGUI {
	private final Player player;
	private MasterMinerData data;

	private final static int INCREMENT = 10;

	private boolean isPrestigeShowing = false;
	public Achievements achievements;

	public MasterMinerGUI(Player player) {
		this.player = player;
		//this.data = data;
		this.achievements = new Achievements(player);
	}

	public void open() {
		player.interfaceManager.open(24300);
		populateStatsBar();
		updateAmountToBuy();
		updateTitle();
		updateMobDetails();
		updateInterface();

		GoldRefresher refreshThread = new GoldRefresher(player);
		refreshThread.setPriority(Thread.MIN_PRIORITY);
		refreshThread.start();
	}

	public void updateInterface() {
		updateGoldCount();
		updateSpecialButtons();

		populateStatsBar();
	}

	private void updateSpecialButtons() {
		if (!isPrestigeShowing && data.mobData[7].getLevel() > 0) {
			player.send(new SendString("Prestige", 24663));
		} else {
			player.send(new SendString("?", 24663));
		}

		// New Task really isn't a special button, but..
		if (player.masterMinerTask.getCurrentTask().isComplete()) {
			player.send(new SendString("Claim Reward", 24340));
		} else if (player.masterMinerTask.getTimeLastCompelted() + SIXHOUR > System.currentTimeMillis()) {
			player.send(new SendString(
					toTime(player.masterMinerTask.getTimeLastCompelted() + SIXHOUR - System.currentTimeMillis()),
					24340));
		} else {
			player.send(new SendString("New Task", 24340));
		}
	}

	private void updateTitle() {
		player.send(new SendString("Master Miner (Lvl " + data.prestigeLevel + ")", 24305));
	}

	protected void updateGoldCount() {
		if (data.timeLastCheckedMs != 0) {
			data.totalGold += data.goldPerSec * ((System.currentTimeMillis() - data.timeLastCheckedMs) / 1000);
		}
		player.send(new SendString(toNiceString(data.totalGold), 24336));
		data.timeLastCheckedMs = System.currentTimeMillis();
	}

	public void updateAmountToBuy() {
		if (data.amountToBuy == -1) {
			player.send(new SendString("Max", 24344));
		} else {
			player.send(new SendString(data.amountToBuy, 24344));
		}
	}

	protected void updateMobDetails() {
		for (int i = 0; i < 8; i++) {
			player.send(new SendString(data.mobData[i].getName(), 24402 + i * INCREMENT)); // Name
			player.send(new SendString("Buy", 24406 + i * INCREMENT));
			player.send(new SendString("Lvl: " + toNiceString(data.mobData[i].getLevel()), 24407 + i * INCREMENT));
			// player.send(new SendString("Price: " +
			// toNiceString(data.mobData[i].getPrice()), 24408 + i * INCREMENT));
			player.send(
					new SendTooltip("This does absolutely fucking nothing " + toNiceString(data.mobData[i].getGPS()),
							24409 + i * INCREMENT));
			// player.send(new SendString(toNiceString(data.mobData[i].getGPS()), 24410 + i
			// * INCREMENT));

			// player.send(new SendString(data.mobData[i].getName(), 24665 + i*4));
			// player.send(new SendString("- Gives " +
			// toNiceString(data.mobData[i].getGPS()) + " gold per second.", 24666 + i*4));
			// player.send(new SendString("- Base cost: ", 24667 + i*4));
			// player.send(new SendString("", 24668 + i*4));
		}
	}

	private void populateStatsBar() {
		String statsArray[] = new String[20];
		for (int i = 0; i < 20; i++)
			statsArray[i] = "";

		statsArray[0] = "Gold/sec: " + toNiceString(data.goldPerSec);
		statsArray[1] = "Total Clicks: " + data.totalClicks;
		statsArray[2] = "Tasks Completed: " + player.masterMinerTask.getTasksComplete();
		statsArray[3] = "Current Task: " + player.masterMinerTask.getCurrentTask().taskDescription();
		statsArray[4] = "Progress: " + player.masterMinerTask.getProgress();

		for (int i = 0; i < 20; i++) {
			player.send(new SendString(statsArray[i], 24315 + i));
		}
	}

	protected void updateGoldPerSecond() {
		long sum = 0;

		for (int i = 0; i < 8; i++) {
			sum += data.mobData[i].getGPS() * data.mobData[i].getLevel();
		}

		data.goldPerSec = sum;
	}
}