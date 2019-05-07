package io.server.content.masterminer;

public class MasterMinerData {
	public long goldPerSec;
	public long totalGold;
	public long timeLastCheckedMs;
	public MobData[] mobData = new MobData[8];
	public int amountToBuy;
	public int totalClicks;
	public int prestigeLevel;

	public MasterMinerData() {
		amountToBuy = 1;
		timeLastCheckedMs = System.currentTimeMillis();
		mobData[0] = new MobData("Duck", 50, 5);
		mobData[1] = new MobData("Goblin", 2500, 30);
		mobData[2] = new MobData("Monk", 90000, 150);
		mobData[3] = new MobData("Guard", 4500000, 900);
		mobData[4] = new MobData("Ice Fiend", 350000000, 15000);
		mobData[5] = new MobData("Greater Demon", 8000000000L, 750000);
		mobData[6] = new MobData("Red Dragon", 15000000000000L, 60000000);
		mobData[7] = new MobData("Corporeal Beast", 100000000000000000L, 4000000000L);
	}
}
