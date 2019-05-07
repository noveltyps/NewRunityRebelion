package io.server.content.dailyTasks;

import java.util.Date;

import io.server.content.worldevent.WorldEventType;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.util.Utility;

public class DailyTask {
	
	private final DailyTaskType TYPE;

	private int bossID;
	private int skillID;
	private int amount;
	private int pointsReward;
	
	private int currentCount = 0;
	
	private Item reward;
	
	private boolean isComplete = false;
	
	private final Date ASSIGNMENT_DATE;
	
	public DailyTask(DailyTaskType type, Date assignmentDate) {
		this.TYPE = type;
		this.ASSIGNMENT_DATE = assignmentDate;
	}
	
	public void process(Player player) {
		
		if (isComplete)
			return;
		
		currentCount++;
		
		if (currentCount >= amount) {
			
			isComplete = true;

			if (TYPE == DailyTaskType.BLOOD_TASK)
				player.setbloodPoints(player.getbloodPoints());
			if (TYPE == DailyTaskType.PKING_TASK)
				player.setpkPoints(player.getpkPoints() + (World.getWorldEventHandler().getEvent(WorldEventType.PKP) == null ? getPointsReward() : 2 * getPointsReward()));
			else
				player.bank.deposit(reward);
			
			player.sendMessage(Utility.highlightBlueText("Congradulations, you've completed your daily task!"));
			player.sendMessage(Utility.highlightBlueText((TYPE == DailyTaskType.PKING_TASK ? "You've earned " + (World.getWorldEventHandler().getEvent(WorldEventType.PKP) == null ? getPointsReward() : 2 * getPointsReward()) + " PK points!" :
				reward.getAmount() + " " + reward.getName() + " has been deposited into your bank!")));
			
		}
		
	}
	
	public String toString() {
		
		switch(TYPE) {
			
			case BOSS_TASK:
				return "to " + TYPE.getDesc().replace("[x]", String.valueOf(amount)).replace("[y]", NpcDefinition.get(getBossID()).getName() + "(s).");
			
			case SKILLING_TASK:
				switch(skillID) {
					case 8:
						return "to chop " + TYPE.getDesc().replace("[x]", String.valueOf(amount)) + " trees.";
					case 10:
						return "to catch " + TYPE.getDesc().replace("[x]", String.valueOf(amount)) + " fish.";
					case 14:
						return "to mine " + TYPE.getDesc().replace("[x]", String.valueOf(amount)) + " ore(s).";
					case 17:
						return "to steal " + TYPE.getDesc().replace("[x]", String.valueOf(amount)) + " items.";
					case 22:
						return "to hunt " + TYPE.getDesc().replace("[x]", String.valueOf(amount)) + " critters.";
				}
			case PKING_TASK:
				return "to kill " + TYPE.getDesc().replace("[x]", String.valueOf(amount)) + " players.";
			case BLOOD_TASK:
				return "to " + TYPE.getDesc().replace("[x]", String.valueOf(amount)).replace("[y]", NpcDefinition.get(getBossID()).getName() + "(s).");
			default:
				return "undefined.";
		
		}
		
	}
	
	public void setBossID(int ID) { this.bossID = ID; }
	
	public void setSkillID(int ID) { this.skillID = ID; }
	
	public void setAmount(int amount) { this.amount = amount; }
	
	public void setPointsReward(int points) { this.pointsReward = points; }
	
	public void setReward(Item reward) { this.reward = reward; }
	
	public DailyTaskType getType() { return TYPE; }
	
	public int getBossID() { return bossID; }
	
	public int getSkillID() { return skillID; }
	
	public int getAmount() { return amount; }
	
	public int getPointsReward() { return pointsReward; }
	
	public Item getReward() { return reward; }
	
	public Date getAssignmentDate() { return ASSIGNMENT_DATE; }

}
