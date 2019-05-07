package io.server.content.dailyTasks;

public enum DailyTaskType {
	
	BOSS_TASK("kill [x] [y]"),
	SKILLING_TASK("[x]"),
	PKING_TASK("get [x] kills"),
	BLOOD_TASK("earn blood points");
	private final String TASK_DESC;
	
	DailyTaskType(String TASK_DESC) {
		this.TASK_DESC = TASK_DESC;
	}
	
	public String getDesc() { return TASK_DESC; }

}
