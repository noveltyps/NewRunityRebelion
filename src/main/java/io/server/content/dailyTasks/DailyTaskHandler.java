package io.server.content.dailyTasks;

import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.util.Utility;

public class DailyTaskHandler {
	
	private static final int[] BOSSES = new int[]{ 0, 1 };
	
	private static final int[] SKILLS = new int[]{ 8, 10, 14, 17 };
	// 10, 25, 50, 75, 100
	private static final int[] AMOUNTS = new int[]{ 1, 1, 1, 1, 1 };
	
	private static final int[] BLOOD_POINT_REWARD = new int[]{ 5, 10, 15, 20, 25 };
	
	private static final int[] PVP_REWARDS = new int[]{ 5, 10, 15, 20, 25 };
	
	private static final Item[] BLOOD_REWARDS = new Item[]{ new Item(995, 10000), new Item(995, 10000) };
	
	private static final Item[] REWARDS = new Item[]{ new Item(995, 10000), new Item(995, 10000) };
	
	public static boolean assign(Player player, int type) {
		
		if (player.getDailyTask() != null)
			if (new GregorianCalendar().getTime().getTime() - player.getDailyTask().getAssignmentDate().getTime() < 86400000) {
				
				player.sendMessage(Utility.highlightText("You've already completed your daily task for the day!"));
				return false;
				
			}
		
		switch(type) {
		
			case 0:
				player.setDailyTask(buildBossTask());
				break;
			case 1:
				player.setDailyTask(buildSkillTask());
				break;
			case 2:
				player.setDailyTask(buildPvPTask());
				break;
			case 3:
				player.setDailyTask(buildBloodTask());
				
		}
		
		player.sendMessage(Utility.highlightBlueText("Daily task assigned! Your task is " + player.getDailyTask().toString()));
		
		return true;
		
	}
	
	private static DailyTask buildBossTask() {
		
		DailyTask task = new DailyTask(DailyTaskType.BOSS_TASK, new GregorianCalendar().getTime());
		
		task.setSkillID(BOSSES[ThreadLocalRandom.current().nextInt(0, BOSSES.length - 1)]);
		task.setAmount(AMOUNTS[ThreadLocalRandom.current().nextInt(0, AMOUNTS.length - 1)]);
		task.setReward(REWARDS[ThreadLocalRandom.current().nextInt(0, REWARDS.length - 1)]);
		
		return task;
		
	}
	
	private static DailyTask buildSkillTask() {
		
		DailyTask task = new DailyTask(DailyTaskType.SKILLING_TASK, new GregorianCalendar().getTime());
		
		task.setSkillID(SKILLS[ThreadLocalRandom.current().nextInt(0, SKILLS.length - 1)]);
		task.setAmount(AMOUNTS[ThreadLocalRandom.current().nextInt(0, AMOUNTS.length - 1)]);
		task.setReward(REWARDS[ThreadLocalRandom.current().nextInt(0, REWARDS.length - 1)]);
		
		return task;
		
	}
	
	private static DailyTask buildPvPTask() {
		
		DailyTask task = new DailyTask(DailyTaskType.PKING_TASK, new GregorianCalendar().getTime());
		
		task.setAmount(AMOUNTS[ThreadLocalRandom.current().nextInt(0, AMOUNTS.length - 1)]);
		task.setPointsReward(PVP_REWARDS[ThreadLocalRandom.current().nextInt(0, PVP_REWARDS.length - 1)]);
		
		return task;
		
	}
	
	private static DailyTask buildBloodTask() {
		
		DailyTask task = new DailyTask(DailyTaskType.BLOOD_TASK, new GregorianCalendar().getTime());
		
		task.setAmount(AMOUNTS[ThreadLocalRandom.current().nextInt(0, AMOUNTS.length - 1)]);
		task.setPointsReward(BLOOD_POINT_REWARD[ThreadLocalRandom.current().nextInt(0, BLOOD_POINT_REWARD.length - 1)]);
		task.setReward(BLOOD_REWARDS[ThreadLocalRandom.current().nextInt(0, BLOOD_REWARDS.length - 1)]);
		
		return task;
		
	}

}
