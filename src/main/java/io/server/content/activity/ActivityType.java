package io.server.content.activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds all activity types that are timed.
 *
 * @author Daniel
 */
public enum ActivityType {
	
	SCHOOL_GAME(false), BATTLE_REALM(false), FIGHT_CAVES(true),ALLVSONE2(true),  ALLVSONE3(true), ALLVSONE4(true), Double_Threat(true), ALLVSONE(true), INFERNO(true), KOLODION_ARENA(true), RECIPE_FOR_DISASTER(true),
	BARROWS(true), ZULRAH(true), KRAKEN(true), DUEL_ARENA(false), TUTORIAL(false), PEST_CONTROL(false), CERBERUS(true),
	LMS(true), JAIL(false), WARRIOR_GUILD(false), CORP_INSTANCE(true), RANDOM_EVENT(false), VORKATH(false), SOLORAIDS1(true), SOLORAIDS2(true), SOLORAIDS3(true), SOLORAIDS4(true), SOLORAIDS5(true), RAIDS1(true), RAIDS2(true), RAIDS3(true), RAIDS4(true), RAIDS5(true),
	MOLE_INSTANCE(true), LIZARD_INSTANCE(true), BOSS_CONTROL(true), ZOMBIERAID(true), GENERAL_GRAADOR_INSTANCE(true), FREE_FOR_ALL(false);

	final boolean record;
	private static final List<ActivityType> RECORDABLE = new LinkedList<>();

	static {
		for (ActivityType activity : values()) {
			if (activity.record)
				RECORDABLE.add(activity);
		}
	}

	ActivityType(boolean record) {
		this.record = record;
	}

	public static List<ActivityType> getRecordable() {
		return new LinkedList<>(RECORDABLE);
	}

	public static ActivityType getOrdinal(int ordinal) {
		if (ordinal < 0 || ordinal > RECORDABLE.size())
			return null;
		return RECORDABLE.get(ordinal);
	}

	public static ActivityType getFirst() {
		return getOrdinal(0);
	}
}
