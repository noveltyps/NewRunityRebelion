package io.server.content.masterminer;

import static io.server.content.masterminer.Util.toNiceString;
import static io.server.content.masterminer.Util.toTimeLong;

import io.server.content.clanchannel.content.ClanTaskKey;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class MasterMinerTaskHandler {
	public static final long SIXHOUR = 21600000;
	private int tasksComplete;
	private Task currentTask;
	private long timeLastCompelted;

	public MasterMinerTaskHandler() {
		tasksComplete = 0;
		currentTask = new Task();
		timeLastCompelted = 0;
	}

	public void obtainTask(Player player) {
		if (currentTask.getID() > 0) {
			DialogueFactory factory = new DialogueFactory(player);
			factory.sendNpcChat(250, Expression.CALM_TALK, "You already have an active task!");
			factory.execute();
			return;
		} else if (timeLastCompelted + SIXHOUR > System.currentTimeMillis()) {
			DialogueFactory factory = new DialogueFactory(player);
			factory.sendNpcChat(250, Expression.DEFAULT,
					"You must wait another " + toTimeLong(
							player.masterMinerTask.getTimeLastCompelted() + SIXHOUR - System.currentTimeMillis()),
					"before getting another task!");
			factory.execute();
			return;
		}
		currentTask = new Task();
		DialogueFactory factory = new DialogueFactory(player);
		factory.sendNpcChat(250, Expression.HAPPY, "You must " + currentTask.taskDescription());
		factory.execute();
	}

	public void claimReward(Player player) {
		tasksComplete++;
		long reward = getReward(player);
		player.send(new SendMessage(
				"Successfully completed a task! You've been awarded " + toNiceString(reward) + " extra gold!"));
		player.forClan(channel -> channel.activateTask(ClanTaskKey.COMPLETE_MASTERMINER_TASK_KEY, player.getName()));
		player.masterMinerData.totalGold += reward;
		this.currentTask = new Task(0);
		this.timeLastCompelted = System.currentTimeMillis();
	}

	private long getReward(Player player) {
		MobData[] d = player.masterMinerData.mobData;
		long sum = 0;
		for (int i = 0; i < 8; i++) {
			sum += d[i].getLevel() * d[i].getGPS() * 1000;
		}
		return sum;
	}

	public void sendTask(int task, Player player) {
		if (task == currentTask.getID()) {
			currentTask.incrementTask();
			if (currentTask.isComplete()) {
				DialogueFactory factory = new DialogueFactory(player);
				factory.sendNpcChat(250, Expression.HAPPY, "Congratulations! You've completed a task.");
				factory.execute();
				player.send(new SendMessage("Congradulations! You've completed a task."));
				player.send(new SendMessage("Head back to the Master Miner to claim your reward."));
			}
		}
	}

	public MasterMinerTaskHandler(String fromString) {
		try {
			String parts[] = fromString.split(">");
			this.timeLastCompelted = Long.parseLong(parts[0]);
			this.tasksComplete = Integer.parseInt(parts[1]);
			this.currentTask = new Task(parts[2]);
		} catch (Exception e) {
			System.out.println("Something went wrong reading MasterMinerTask. Setting all values to default");
			this.timeLastCompelted = 0;
			this.tasksComplete = 0;
			this.currentTask = new Task();
		}
	}

	@Override
	public String toString() {
		return timeLastCompelted + ">" + tasksComplete + ">" + currentTask;
	}

	public int getTasksComplete() {
		return tasksComplete;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public String getProgress() {
		return currentTask.getProgress();
	}

	public long getTimeLastCompelted() {
		return timeLastCompelted;
	}
}
