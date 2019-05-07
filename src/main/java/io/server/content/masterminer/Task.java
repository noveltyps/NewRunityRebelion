package io.server.content.masterminer;

import java.util.Random;

public class Task {

	public static final int NONE = -1;
	public static final int COMPLETED = 0;
	public static final int WOODCUTTING = 1;

	private int ID;
	private int taskDenominator, taskNumerator;

	public Task() { // Generate a new task
		Random rand = new Random();
		ID = WOODCUTTING; // Very random
		taskNumerator = 0;
		taskDenominator = rand.nextInt(10) + 1;
	}

	public Task(String fromString) { // Read task from save file
		String parse[] = fromString.split("<");
		ID = Integer.parseInt(parse[0]);
		taskDenominator = Integer.parseInt(parse[1]);
		taskNumerator = Integer.parseInt(parse[2]);
	}

	public Task(int i) {
		ID = NONE;
		taskDenominator = 0;
		taskNumerator = 0;
	}

	@Override
	public String toString() {
		return ID + "<" + taskDenominator + "<" + taskNumerator;
	}

	public int getID() {
		return ID;
	}

	public void incrementTask() {
		taskNumerator++;
	}

	public String getProgress() {
		if (getDenominator() == 0) {
			return "---";
		}
		return 100.0 * (double) taskNumerator / (double) taskDenominator + "%";
	}

	public boolean isComplete() {
		if (taskNumerator >= taskDenominator && taskDenominator != 0) {
			taskNumerator = taskDenominator;
			ID = COMPLETED;
			return true;
		}
		return false;
	}

	public String taskDescription() {
		switch (ID) {
		case NONE:
			return "None!";
		case COMPLETED:
			return "Completed!";
		case WOODCUTTING:
			return "Cut " + taskDenominator + " trees!";
		default:
			return "None!";
		}
	}

	public int getDenominator() {
		return taskDenominator;
	}

	public int getNumerator() {
		return taskNumerator;
	}
}
