package io.server.content.activity.record;

import java.util.Objects;

import io.server.content.activity.ActivityType;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.Utility;

/*
 * handles the construction of the class, creating object for it
 */

public class GameRecord {
	public final String name;
	public final String date;
	public final int rank;
	public final long time;
	final ActivityType activityType;

	private GameRecord(String name, String date, int rank, long time, ActivityType activityType) {
		this.name = name;
		this.date = date;
		this.rank = rank;
		this.time = time;
		this.activityType = activityType;
	}

	GameRecord(Player player, long time, ActivityType activity) {
		this(player.getName(), Utility.getSimpleDate(), player.right.getCrown(), time, activity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, time);
	}

	@Override
	public String toString() {
		return "name=" + name + " time=" + time + " type=" + activityType;
	}

}
