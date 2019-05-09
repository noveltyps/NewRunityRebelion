package io.server.content.managers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import io.server.content.managers.Manager;
import io.server.content.model.LotteryEntry;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.Utility;

public class LotteryManager extends Manager {
	
	private static long pot = 0;
	private long startTime;
	private static boolean isOpen = true;
	private static boolean restarting = false;
	private final List<Npc> LOTTERY_MASTERS = new ArrayList<>();
	private static final List<LotteryEntry> ENTRIES = new ArrayList<>();
	
	public LotteryManager() {
		super("Lottery Manager", 1000); // 300000
		LOTTERY_MASTERS.addAll(World.getNpcsByID(379));
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void process() { // 3600000
		
		if (restarting) {
			
			try {
				
				restart();
				
				announce("Restarting...");
				
				sleep(10000); // 300000
				
				restarting = false;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		if (System.currentTimeMillis() - startTime >= 3600000) // 3600000
			if (ENTRIES.size() <= 0) {
				announce("No entries this round, no winners!");
				restarting = true;
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else
				determineWinner();
		else
			announce("Current pot is " + Utility.formatPrice(pot) + "! Closes in " + Utility.getShortenedFormattedSeconds((3600000 - (System.currentTimeMillis() - startTime)) / 1000));
		
	}
	
	public void announce(String message) {
		
		LOTTERY_MASTERS.stream().forEach((npc) -> {
			npc.speak(message);
		});
		
	}
	
	public void determineWinner() {
		
		LotteryEntry winningEntry = null;
		
		do {
			
			if (ENTRIES.size() <= 0)
				break;
			
			winningEntry = ENTRIES.get(ThreadLocalRandom.current().nextInt(0, ENTRIES.size()));
			
			if (World.getPlayerByName(winningEntry.getPlayer().getName()) == null)
				ENTRIES.remove(winningEntry);
			
		} while (World.getPlayerByName(winningEntry.getPlayer().getName()) == null);
		
		if (winningEntry == null || World.getPlayerByName(winningEntry.getPlayer().getName()) == null) {
			restarting = true;
			return;
		}
		
		int reward = (int) (pot * .8);
		
		announce(winningEntry.getPlayer().getName() + " has won the lottery! Winnings: " + Utility.formatPrice(reward) + ".");
		World.sendBroadcast(1, winningEntry.getPlayer().getName() + " has won the lottery! Winnings: " + Utility.formatPrice(reward) + ".", true);

		winningEntry.getPlayer().sendMessage(Utility.highlightGreenText("You won the lottery!"));
		winningEntry.getPlayer().bankVault.add(reward, true);
		
		restarting = true;
		
		try {
			sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void restart() {
		
		ENTRIES.clear();
		pot = 0;
		startTime = System.currentTimeMillis();
		
	}
	
	public static void submitEntry(LotteryEntry entry) {
		ENTRIES.add(entry);
		pot += entry.getAmount();
	}
	
	public static boolean hasEntered(Player player) {
		
		boolean found = false;
		
		for (LotteryEntry entry : ENTRIES)
			if (entry.getPlayer().getName().toLowerCase().contentEquals(player.getName().toLowerCase())) {
				found = true;
				break;
			}
		
		return found;
		
	}
	
	public static boolean isOpen() { return isOpen; }
	
	public static boolean isRestarting() { return restarting; }

}
