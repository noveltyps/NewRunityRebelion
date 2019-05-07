package io.server.content.activity.record;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import io.server.content.activity.ActivityType;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendScrollbar;
import io.server.net.packet.out.SendString;
import io.server.util.GsonUtils;
import io.server.util.Utility;

/**
 * Created by Daniel on 2017-11-22.
 */
public class GlobalRecords {

	private static Set<GameRecord> GLOBAL_RECORDS = new HashSet<>();

	public static void display(Player player, ActivityType activity) {
		List<GameRecord> list = getActivities(activity);
		player.gameRecord.clean(list.size());
		player.gameRecord.showActivities(activity);
		int index = 0;
		for (GameRecord record : list) {
			index++;
			String prefix = index == 1 ? "<clan=6>"
					: (index == 6 ? "<clan=5>" : (index == 11 ? "<clan=4>" : (index / 5) + 1 + ")"));
			player.send(new SendString(prefix, 32401 + index));
			index++;
			player.send(new SendString(record.name, 32401 + index));
			index++;
			player.send(new SendString(Utility.getTime(record.time), 32401 + index));
			index++;
			player.send(new SendString(record.date, 32401 + index));
			index++;
		}
		player.send(new SendScrollbar(32400, (list.size() < 9 ? 9 : list.size()) * 28));
		player.interfaceManager.open(32300);
	}

	public static void add(Player player, GameRecord tracker) {
		System.out.println("Addiong record for " + player.getName() + tracker.activityType.name());
		GameRecord record = getTracker(player.getName(), tracker.activityType);
		if (record == null) {
			// System.out.println("Record is null, adding first one");
			// GLOBAL_RECORDS.add(tracker);
			System.out.println("GAMESHIT RECORRDS IS NULL WHOEVER WROTE THIS POS IS FUCKING GAY.");
			return;
		}
		if (tracker.time > record.time) {
			System.out.println("Record is not null, replacing old one");
			GLOBAL_RECORDS.remove(record);
			GLOBAL_RECORDS.add(tracker);
		}
		System.out.println("Not changing record");
	}

	private static GameRecord getTracker(String name, ActivityType activity) {
		Set<GameRecord> set = getGlobalRecords(name);

		if (set == null || set.isEmpty()) {
			return null;
		}

		for (GameRecord t : set) {
			if (t.activityType == activity)
				return t;
		}
		return null;
	}

	static GameRecord getTopRecord(ActivityType activity) {
		List<GameRecord> records = getActivities(activity);
		System.out.println("Getting top record for " + activity.name());
		return records.isEmpty() ? null : records.get(0);
	}

	private static Set<GameRecord> getGlobalRecords(String name) {
		Set<GameRecord> set = new HashSet<>();
		if (GLOBAL_RECORDS == null || GLOBAL_RECORDS.isEmpty()) {
			return set;
		}

		for (GameRecord tracker : GLOBAL_RECORDS) {
			if (!tracker.name.equalsIgnoreCase(name))
				continue;
			set.add(tracker);
		}
		System.out.println("Returning globalRecords for " + name);
		return set;
	}

	private static List<GameRecord> getActivities(ActivityType activity) {
		List<GameRecord> set = new ArrayList<>();
		for (GameRecord tracker : GLOBAL_RECORDS) {
			if (tracker.activityType != activity)
				continue;
			set.add(tracker);
		}
		set.sort(Comparator.comparingLong(record -> record.time));
		return set;
	}

	public static void load() {
		Type type = new TypeToken<Set<GameRecord>>() {
			private static final long serialVersionUID = 1L;
		}.getType();
		Path path = Paths.get("data", "/content/game/game_records.json");
		try (FileReader reader = new FileReader(path.toFile())) {
			JsonParser parser = new JsonParser();
			GLOBAL_RECORDS = new GsonBuilder().create().fromJson(parser.parse(reader), type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		new Thread(() -> {
			try (FileWriter fw = new FileWriter("./data/content/game/game_records.json")) {
				fw.write(GsonUtils.JSON_PRETTY_NO_NULLS.toJson(GLOBAL_RECORDS));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}
