package io.server.content.achievement;

import java.util.HashMap;

/**
 * Handles the clicking on the achievement tab itemcontainer.
 *
 * @author Daniel
 */
public class AchievementButton {
	/** Holds all the achievement buttons. */
	public static final HashMap<Integer, AchievementList> ACHIEVEMENT_BUTTONS = new HashMap<Integer, AchievementList>();

	/** Holds all the achievement titles. */
	public static final HashMap<Integer, AchievementDifficulty> ACHIEVEMENT_TITLES = new HashMap<Integer, AchievementDifficulty>();

	static HashMap<Integer, AchievementList> getAchievementButtons() {
		return ACHIEVEMENT_BUTTONS;
	}

	static HashMap<Integer, AchievementDifficulty> getAchievementTitles() {
		return ACHIEVEMENT_TITLES;
	}
}
