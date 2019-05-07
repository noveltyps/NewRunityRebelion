package io.server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.server.Server;
import io.server.game.world.Interactable;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.pathfinding.path.Path;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;

/**
 * Handles miscellaneous methods.
 *
 * @author Daniel
 * @edited Adam_#6723
 * @updated Neytorokx#8707
 */
public class Utility {

	/** Random instance, used to generate pseudo-random primitive types. */
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private final static char[] CHARACTERS_SCOPE = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'n', 'm', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'N', 'M', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/** Array of all valid characters. */
	private static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '/' };

	/** Gets a percentage amount. */
	public static double getPercentageAmount(int progress, int total) {
		return 100 * progress / total;
	}

	/** sets a percentage amount. */
	public static double setPercentageAmount(int total) {
		return total;
	}

	/** Formats digits for integers. */
	public static String formatDigits(final int amount) {
		return NumberFormat.getInstance().format(amount);
	}

	/** Formats digits for longs. */
	public static String formatDigits(final long amount) {
		return NumberFormat.getInstance().format(amount);
	}

	/** Formats digits for doubles. */
	public static String formatDigits(final double amount) {
		return NumberFormat.getInstance().format(amount);
	}

	/** Formats a price for longs. */
	public static String formatPrice(final long amount) {
		if (amount >= 0 && amount < 1_000)
			return "" + amount;
		if (amount >= 1_000 && amount < 1_000_000) {
			return (amount / 1_000) + "K";
		}
		if (amount >= 1_000_000 && amount < 1_000_000_000) {
			return (amount / 1_000_000) + "M";
		}
		if (amount >= 1_000_000_000 && amount < Integer.MAX_VALUE) {
			return (amount / 1_000_000_000) + "B";
		}
		return "<col=fc2a2a>Lots!";
	}

	public static boolean hasColumn(ResultSet rs, String column) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		for (int x = 1; x <= columns; x++) {
			if (column.equals(rsmd.getColumnName(x))) {
				return true;
			}
		}
		return false;
	}

	public static String capitalizeFully(String value) {
		String[] arr = value.split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
		}
		return sb.toString().trim();
	}

	public static String[] getArrayFromString(String delimiter, String value) {

		if (value == null || value.isEmpty()) {
			return new String[0];
		} else {
			return value.split(Pattern.quote(delimiter));
		}

	}

	public static long stringToLong(String string) {
		long l = 0L;
		for (int i = 0; i < string.length() && i < 12; i++) {
			char c = string.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L)
			l /= 37L;
		return l;
	}

	/**
	 * Creates a random string with a maximum length of characters. Characters
	 * selected from the scope
	 *
	 * @param length the maximum length
	 * @return the random string
	 */
	public static String createRandomString(int length) {

		if (length < 1) {
			throw new IndexOutOfBoundsException();
		}

		char[] characters = new char[length];

		for (int i = 0; i < length; i++) {
			characters[i] = (CHARACTERS_SCOPE[new Random().nextInt(CHARACTERS_SCOPE.length - 1)]);
		}

		return new String(characters);

	}

	/** Formats name of enum. */
	public static String formatEnum(final String string) {
		return capitalizeSentence(string.toLowerCase().replace("_", " "));
	}

	/** Formats the player name. */
	public static String rank(final String string) {
		return Stream.of(string.trim().split("\\s")).filter(word -> word.length() > 0)
				.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
	}

	/** Capitalize each letter after . */
	public static String capitalizeSentence(final String string) {
		int pos = 0;
		boolean capitalize = true;
		StringBuilder sb = new StringBuilder(string);
		while (pos < sb.length()) {
			if (sb.charAt(pos) == '.') {
				capitalize = true;
			} else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
				sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
				capitalize = false;
			}
			pos++;
		}
		return sb.toString();
	}

	/** Add A or an */
	public static String getAOrAn(String nextWord) {
		String s = "a";
		char c = nextWord.toUpperCase().charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
			s = "an";
		}
		return s;
	}
	/** Remove A or an */
	public static String removeAOrAn(String input) {
		return input.replace("a ", "").replace("a ", "");
	}

	/** Gets the date of server. */
	public static String getDate() {
		return new SimpleDateFormat("EE MMM dd yyyy").format(new Date());
	}

	/** Gets the date of server. */
	public static String getSimpleDate() {
		return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	}

	/** Converts an integer into words. */
	public static String convertWord(int amount) {
		return Words.getInstance(amount).getNumberInWords();
	}

	/** Gets the current server time and formats it */
	public static String getTime() {
		return new SimpleDateFormat("hh:mm aa").format(new Date());
	}

	/** Gets the time based off a long. */
	public static String getTime(long period) {
		return new SimpleDateFormat("m:ss").format(period);
	}

	public static String bigDaddyTime(long period) {
		return new SimpleDateFormat("HH:mm:ss").format(period);
	}

	/** Gets the current uptime of server and formats it */
	public static String getUptime() {
		return getTime((int) (Server.UPTIME.elapsedTime() / 600));
	}

	/** Gets a basic time based off seconds. */
	public static String getTime(int ticks) {
		long secs = ticks * 3 / 5;

		if (secs < 60) {
			return "0:" + (secs < 10 ? "0" : "") + secs;
		}

		long mins = secs / 60;
		long remainderSecs = secs - (mins * 60);
		if (mins < 60) {
			return mins + ":" + (remainderSecs < 10 ? "0" : "") + remainderSecs + "";
		}

		long hours = mins / 60;
		long remainderMins = mins - (hours * 60);
		if (hours < 24) {
			return hours + "h " + (remainderMins < 10 ? "0" : "") + remainderMins + "m "
					+ (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
		}

		long days = hours / 24;
		long remainderHrs = hours - (days * 24);
		return days + "d " + (remainderHrs < 10 ? "0" : "") + remainderHrs + "h " + (remainderMins < 10 ? "0" : "")
				+ remainderMins + "m";
	}

	/** Converts the first 12 characters in a string of text to a hash. */
	public static long nameToLong(String text) {
		long hash = 0L;
		for (int index = 0; index < text.length() && index < 12; index++) {
			char key = text.charAt(index);
			hash *= 37L;
			if (key >= 'A' && key <= 'Z')
				hash += (1 + key) - 65;
			else if (key >= 'a' && key <= 'z')
				hash += (1 + key) - 97;
			else if (key >= '0' && key <= '9')
				hash += (27 + key) - 48;
		}
		while (hash % 37L == 0L && hash != 0L)
			hash /= 37L;
		return hash;
	}

	public static String longToString(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return null;
		if (l % 37L == 0L)
			return null;
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static long hash(String input) {
		long hash = 0L;
		if (input == null) {
			input = "null";
		}
		for (int index = 0; index < input.length() && index < 12; index++) {
			char key = input.charAt(index);
			hash *= 37L;
			if (key >= 'A' && key <= 'Z') {
				hash += (1 + key) - 65;
			} else if (key >= 'a' && key <= 'z') {
				hash += (1 + key) - 97;
			} else if (key >= '0' && key <= '9') {
				hash += (27 + key) - 48;
			}
		}
		while (hash % 37L == 0L && hash != 0L) {
			hash /= 37L;
		}
		return hash;
	}

	public static int random(int bound) {
		return random(0, bound, false);
	}

	public static int random(int lowerBound, int upperBound) {
		if (upperBound == lowerBound)
			upperBound++;
		return random(lowerBound, upperBound, false);
	}

	/** Picks a random element out of any array type. */
	public static <T> T randomElement(Collection<T> collection) {
		return new ArrayList<T>(collection).get((int) (RANDOM.nextDouble() * collection.size()));
	}

	/** Picks a random element out of any list type. */
	public static <T> T randomElement(List<T> list) {
		return list.get((int) (RANDOM.nextDouble() * list.size()));
	}

	/** Picks a random element out of any array type. */
	public static <T> T randomElement(T[] array) {
		return array[(int) (RANDOM.nextDouble() * array.length)];
	}

	/** Picks a random element out of any array type. */
	public static int randomElement(int[] array) {
		return array[(int) (RANDOM.nextDouble() * array.length)];
	}

	public static int random(int lowerBound, int upperBound, boolean inclusive) {
		if (lowerBound >= upperBound) {
			throw new IllegalArgumentException("The lower bound cannot be larger than or equal to the upper bound!");
		}

		return lowerBound + RANDOM.nextInt(upperBound - lowerBound) + (inclusive ? 1 : 0);
	}

	/** Gets all of the classes in a directory */
	public static List<Object> getClassesInDirectory(String directory) {
		List<Object> classes = new LinkedList<>();
		File dir = new File(directory);
		if (!dir.exists() || !dir.isDirectory()) {
			return classes;
		}
		try {
			File[] files = dir.listFiles();
			if (files == null) {
				return classes;
			}
			for (File f : files) {
				if (f.isDirectory() || f.getName().contains("$")) {
					continue;
				}
				String domainPath = Utility.class.getProtectionDomain().getCodeSource().getLocation().getPath()
						.replace("/", "\\");
				String filePath = "\\" + f.getPath();
				String clazzName = filePath.replace(domainPath, "");
				clazzName = clazzName.replace("\\", ".");
				clazzName = clazzName.replace(".class", "");
				Class<?> clazz = Class.forName(clazzName);
				Object o = clazz.newInstance();
				classes.add(o);
			}
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			// e.printStackTrace();
		}
		return classes;
	}

	/** Gets all of the sub directories of a folder */
	public static List<String> getSubDirectories(Class<?> clazz) {
		String filePath = clazz.getResource("/" + clazz.getName().replace(".", "/") + ".class").getFile();
		File file = new File(filePath);
		File directory = file.getParentFile();
		List<String> list = new ArrayList<>();

		File[] files = directory.listFiles();

		if (files == null) {
			return Collections.emptyList();
		}

		for (File f : files) {
			if (f.isDirectory()) {
				list.add(f.getPath());
			}
		}

		String[] directories = list.toArray(new String[0]);
		return Arrays.asList(directories);
	}

	/*
	 * Check for a map region change, and if the map region has changed, set the
	 * appropriate flag so the new map region packet is sent.
	 */
	public static boolean isRegionChange(Position position, Position region) {
		int diffX = position.getX() - region.getChunkX() * 8;
		int diffY = position.getY() - region.getChunkY() * 8;
		boolean changed = false;

		if (diffX < 16) {
			changed = true;
		} else if (diffX >= 88) {
			changed = true;
		}

		if (diffY < 16) {
			changed = true;
		} else if (diffY >= 88) {
			changed = true;
		}

		return changed;
	}

	public static int getDistance(Interactable source, Position target) {
		Position sourceTopRight = source.getPosition().transform(source.width() - 1, source.length() - 1);

		int dx, dy;

		if (sourceTopRight.getX() < target.getX()) {
			dx = target.getX() - sourceTopRight.getX();
		} else if (source.getX() > target.getX()) {
			dx = source.getX() - target.getX();
		} else {
			dx = 0;
		}

		if (sourceTopRight.getY() < target.getY()) {
			dy = target.getY() - sourceTopRight.getY();
		} else if (source.getY() > target.getY()) {
			dy = source.getY() - target.getY();
		} else {
			dy = 0;
		}

		return dx + dy;
	}



	public static boolean elapsed(long unitToTest, long timeElapsed) {
		return System.currentTimeMillis() - unitToTest > timeElapsed;
	}
	
	public static boolean elapsedTicks(long unitToTest, long ticksElapsed) {
		return System.currentTimeMillis() - unitToTest > ticksElapsed * 600;
	}
	
	public static int getDistance(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth,
			int targetLength) {
		if (source.getHeight() != target.getHeight()) {
			return Integer.MAX_VALUE;
		}

		if (sourceWidth <= 0)
			sourceWidth = 1;
		if (sourceLength <= 0)
			sourceLength = 1;
		if (targetWidth <= 0)
			targetWidth = 1;
		if (targetLength <= 0)
			targetLength = 1;

		Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1, 0);
		Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1, 0);

		int dx, dy;

		if (sourceTopRight.getX() < target.getX()) {
			dx = target.getX() - sourceTopRight.getX();
		} else if (source.getX() > targetTopRight.getX()) {
			dx = source.getX() - targetTopRight.getX();
		} else {
			dx = 0;
		}

		if (sourceTopRight.getY() < target.getY()) {
			dy = target.getY() - sourceTopRight.getY();
		} else if (source.getY() > targetTopRight.getY()) {
			dy = source.getY() - targetTopRight.getY();
		} else {
			dy = 0;
		}

		return dx + dy;
	}

	public static Position getDelta(Position source, int sourceWidth, int sourceLength, Position target,
			int targetWidth, int targetLength) {
		if (source.getHeight() != target.getHeight()) {
			return Position.create(Integer.MAX_VALUE, Integer.MAX_VALUE);
		}

		if (sourceWidth <= 0)
			sourceWidth = 1;
		if (sourceLength <= 0)
			sourceLength = 1;
		if (targetWidth <= 0)
			targetWidth = 1;
		if (targetLength <= 0)
			targetLength = 1;

		Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1, 0);
		Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1, 0);

		int dx, dy;

		if (sourceTopRight.getX() < target.getX()) {
			dx = target.getX() - sourceTopRight.getX();
		} else if (source.getX() > targetTopRight.getX()) {
			dx = source.getX() - targetTopRight.getX();
		} else {
			dx = 0;
		}

		if (sourceTopRight.getY() < target.getY()) {
			dy = target.getY() - sourceTopRight.getY();
		} else if (source.getY() > targetTopRight.getY()) {
			dy = source.getY() - targetTopRight.getY();
		} else {
			dy = 0;
		}

		return Position.create(dx, dy);
	}

	public static int getDistance(Interactable source, Interactable target) {
		return getDistance(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(),
				target.length());
	}

	public static Position getDelta(Interactable source, Interactable target) {
		return getDelta(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(),
				target.length());
	}

	public static Position getDelta(Position source, Position target) {
		int dx = target.getX() - source.getX();
		int dy = target.getY() - source.getY();
		return Position.create(dx, dy);
	}

	public static boolean withinDistance(Interactable source, Interactable target, int radius) {
		return within(source, target, radius);
	}

	public static boolean withinDistance(Interactable source, Position target, int radius) {
		return within(source.getPosition(), source.width(), source.length(), target, 1, 1, radius);
	}

	public static Position findAccessableTile(Interactable source) {
		Position found = null;
		Position[] positions = getBoundaries(source);

		for (Position next : positions) {
			Direction direction = Direction.getDirection(source.getPosition(), next);

			if (inside(next, 0, 0, source.getPosition(), source.width(), source.length())) {
				continue;
			}

			if (TraversalMap.isTraversable(next, direction, false)) {
				found = next;
				break;
			}
		}

		return found;
	}

	public static Position findBestInside(Interactable source, Interactable target) {
		if (target.width() <= 1 || target.length() <= 1) {
			return target.getPosition();
		}

		int dx, dy, dist = Integer.MAX_VALUE;
		Position best = source.getPosition();

		for (int x = 0; x < target.width(); x++) {
			Position boundary = target.getPosition().transform(x, 0);
			int distance = getDistance(source, boundary);

			if (dist > distance) {
				dist = distance;
				best = boundary;
			}

			boundary = target.getPosition().transform(x, target.length() - 1);
			distance = getDistance(source, boundary);

			if (dist > distance) {
				dist = distance;
				best = boundary;
			}
		}

		for (int y = 0; y < target.length(); y++) {
			Position boundary = target.getPosition().transform(0, y);
			int distance = getDistance(source, boundary);

			if (dist > distance) {
				dist = distance;
				best = boundary;
			}

			boundary = target.getPosition().transform(target.width() - 1, y);
			distance = getDistance(source, boundary);

			if (dist > distance) {
				dist = distance;
				best = boundary;
			}
		}

		if (best.equals(source.getPosition())) {
			return source.getPosition();
		}

		Direction direction = Direction.getDirection(source.getPosition(), best);
		Position sourceTopRight = source.getPosition().transform(source.width() - 1, source.length() - 1, 0);

		if (source.getX() > best.getX()) {
			dx = best.getX() - source.getX();
		} else if (sourceTopRight.getX() < best.getX()) {
			dx = best.getX() - sourceTopRight.getX();
		} else {
			dx = direction.getDirectionX();
		}
		if (source.getY() > best.getY()) {
			dy = best.getY() - source.getY();
		} else if (sourceTopRight.getY() < best.getY()) {
			dy = best.getY() - sourceTopRight.getY();
		} else {
			dy = direction.getDirectionY();
		}

		return source.getPosition().transform(dx, dy);
	}

	public static void fixInsidePosition(Mob source, Interactable target) {
		List<Position> boundaries = new LinkedList<>();
		Map<Position, LinkedList<Position>> paths = new HashMap<>();
		Position cur = source.getPosition();

		for (Position position : getBoundaries(target)) {
			Position delta = getDelta(target.getPosition(), position);
			int dx = Integer.signum(delta.getX()), dy = Integer.signum(delta.getY());
			Direction direction = Direction.getDirection(dx, dy);

			if (direction == Direction.NONE)
				continue;

			while (inside(cur, source.width(), source.length(), target.getPosition(), target.width(),
					target.length())) {
				if (!TraversalMap.isTraversable(cur, direction, source.width())) {
					break;
				}

				cur = cur.transform(direction.getFaceLocation());
				LinkedList<Position> list = paths.computeIfAbsent(position, pos -> {
					boundaries.add(position);
					return new LinkedList<>();
				});

				list.add(cur);
				paths.put(position, list);
			}
		}

		if (boundaries.isEmpty()) {
			return;
		}

		Position random = RandomUtils.random(boundaries);
		if (!source.movement.needsPlacement()) {
			source.movement.addPath(new Path(paths.get(random)));
		}
	}

	private static Position[] getBoundaries(Interactable interactable) {
		int nextSlot = 0;
		int width = interactable.width();
		int length = interactable.length();
		Position[] boundaries = new Position[2 * (width + length)];
		for (int y = 0; y < length + 2; y++) {
			for (int x = 0; x < width + 2; x++) {
				int xx = x % (width + 1);
				int yy = y % (length + 1);
				if (xx == 0 && yy == 0 || xx != 0 && yy != 0)
					continue;
				boundaries[nextSlot++] = interactable.getPosition().transform(x - 1, y - 1, 0);
			}
		}
		return boundaries;
	} //hi

	public static Position[] getInnerBoundaries(Position position, int width, int length) {
		int nextSlot = 0;
		Position[] boundaries = new Position[width * length];
		for (int y = 0; y < length; y++) {
			for (int x = 0; x < width; x++) {
				boundaries[nextSlot++] = position.transform(x, y, 0);
			}
		}
		return boundaries;
	}

	public static Position[] getInnerBoundaries(Interactable interactable) {
		int nextSlot = 0;
		int width = interactable.width();
		int length = interactable.length();
		Position[] boundaries = new Position[width * length];
		for (int y = 0; y < length; y++) {
			for (int x = 0; x < width; x++) {
				boundaries[nextSlot++] = interactable.getPosition().transform(x, y, 0);
			}
		}
		return boundaries;
	}

	public static String formatName(final String string) {
		return Stream.of(string.trim().split("\\s")).filter(word -> word.length() > 0)
				.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
	}
	
	public static String formatUsername(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

	public static boolean within(Position source, Position target, int distance) {
		Interactable interactableSource = Interactable.create(source);
		Interactable interactableTarget = Interactable.create(target);
		return within(interactableSource, interactableTarget, distance);
	}

	public static String formatText(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s.replace("_", " ");
	}

	public static boolean within(Interactable source, Interactable target, int distance) {
		return within(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(),
				target.length(), distance);
	}

	public static boolean withinOctal(Interactable source, Interactable target, int distance) {
		return withinOctal(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(),
				target.length(), distance);
	}

	public static boolean withinOctal(Position source, int sourceWidth, int sourceLength, Position target,
			int targetWidth, int targetLength, int distance) {
		if (target.getHeight() != source.getHeight()) {
			return false;
		}
		Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1);
		Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1);
		int dx, dy;
		if (sourceTopRight.getX() < target.getX()) {
			dx = Math.abs(target.getX() - sourceTopRight.getX());
		} else if (source.getX() > targetTopRight.getX()) {
			dx = Math.abs(targetTopRight.getX() - source.getX());
		} else {
			dx = 0;
		}
		if (sourceTopRight.getY() < target.getY()) {
			dy = Math.abs(target.getY() - sourceTopRight.getY());
		} else if (source.getY() > targetTopRight.getY()) {
			dy = Math.abs(targetTopRight.getY() - source.getY());
		} else {
			dy = 0;
		}
		return dx <= distance && dy <= distance;
	}

	public static boolean within(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth,
			int targetLength, int distance) {
		if (target.getHeight() != source.getHeight()) {
			return false;
		}
		Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1);
		Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1);
		int dx, dy;
		if (sourceTopRight.getX() < target.getX()) {
			dx = Math.abs(target.getX() - sourceTopRight.getX());
		} else if (source.getX() > targetTopRight.getX()) {
			dx = Math.abs(targetTopRight.getX() - source.getX());
		} else {
			dx = 0;
		}
		if (sourceTopRight.getY() < target.getY()) {
			dy = Math.abs(target.getY() - sourceTopRight.getY());
		} else if (source.getY() > targetTopRight.getY()) {
			dy = Math.abs(targetTopRight.getY() - source.getY());
		} else {
			dy = 0;
		}
		return dx + dy <= distance;
	}

	public static boolean withinViewingDistance(Interactable source, Interactable target, int radius) {
		if (source == null || target == null) {
			return false;
		}

		if (target.getHeight() != source.getHeight()) {
			return false;
		}
		Position sourceTopRight = source.getPosition().transform(source.width() - 1, source.length() - 1);
		Position targetTopRight = target.getPosition().transform(target.width() - 1, target.length() - 1);
		int dx, dy;
		if (sourceTopRight.getX() < target.getX()) {
			dx = Math.abs(target.getX() - sourceTopRight.getX());
		} else if (source.getX() > targetTopRight.getX()) {
			dx = Math.abs(targetTopRight.getX() - source.getX());
		} else {
			dx = 0;
		}
		if (sourceTopRight.getY() < target.getY()) {
			dy = Math.abs(target.getY() - sourceTopRight.getY());
		} else if (source.getY() > targetTopRight.getY()) {
			dy = Math.abs(targetTopRight.getY() - source.getY());
		} else {
			dy = 0;
		}
		return dx <= radius && dy <= radius;
	}

	public static boolean inside(Interactable source, Interactable target) {
		return inside(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(),
				target.length());
	}

	public static boolean inside(Interactable source, Position target) {
		Position sourceTopRight = source.getPosition().transform(source.width() - 1, source.length() - 1, 0);
		if (source.getX() >= target.getX() || sourceTopRight.getX() <= target.getX()) {
			return false;
		}
		return source.getY() < target.getY() && sourceTopRight.getY() > target.getY();
	}

	public static boolean inside(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth,
			int targetLength) {
		if (sourceWidth <= 0)
			sourceWidth = 1;
		if (sourceLength <= 0)
			sourceLength = 1;
		if (targetWidth <= 0)
			targetWidth = 1;
		if (targetLength <= 0)
			targetLength = 1;
		Position sourceTopRight = source.transform(sourceWidth - 1, sourceLength - 1, 0);
		Position targetTopRight = target.transform(targetWidth - 1, targetLength - 1, 0);
		if (source.equals(target) || sourceTopRight.equals(targetTopRight)) {
			return true;
		}
		if (source.getX() > targetTopRight.getX() || sourceTopRight.getX() < target.getX()) {
			return false;
		}
		return source.getY() <= targetTopRight.getY() && sourceTopRight.getY() >= target.getY();
	}

	public static boolean checkRequirements(Player player, int[] requirements, String action) {
		boolean can = true;
		for (int index = 0; index < requirements.length; index++) {
			int level = player.skills.getMaxLevel(index);
			int required = requirements[index];

			if (level < required) {
				player.send(new SendMessage(
						"You need a level of " + required + " " + Skill.getName(index) + " to " + action));
				can = false;
			}
		}
		return can;
	}

	public static boolean isLarger(Interactable source, Interactable other) {
		return source.width() * source.length() > other.width() * other.length();
	}

	public static int getStarters(String host) {
		int amount = 0;
		try {
			File file = new File("./data/starters/" + host + ".txt");
			if (!file.exists()) {
				return 0;
			}
			BufferedReader in = new BufferedReader(new FileReader(file));

			String whatever = in.readLine();

			long max = Long.parseLong(whatever);

			if (max > Integer.MAX_VALUE) {
				amount = 2;
			} else {
				amount = (int) max;
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amount;
	}

	public static boolean setStarter(Player player) {
		String host = player.lastHost;

		int amount = getStarters(host);

		if (amount >= 2) {
			return false;
		}

		if (amount == 0) {
			amount = 1;
		} else if (amount == 1) {
			amount = 2;
		}

		try {
			File file = new File("./data/starters/" + host + ".txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
			out.write(String.valueOf(amount));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

    public static String getFormattedSeconds(long seconds) {
        final long h = seconds / 3600, m = seconds / 60 % 60, s = seconds % 60;
        return (h < 10 ? "0" + h : h) + " hours : " + (m < 10 ? "0" + m : m) + " minutes : " + (s < 10 ? "0" + s : s) + " seconds.";
    }
    
    public static String getShortenedFormattedSeconds(long seconds) {
        final long h = seconds / 3600, m = seconds / 60 % 60, s = seconds % 60;
        return (h < 10 ? "0" + h : h) + " H : " + (m < 10 ? "0" + m : m) + " M : " + (s < 10 ? "0" + s : s) + " S.";
    }

	 public static String convertMsToTime(long ms) {
    	if (ms >= 600000)
    		return String.format("%02d:%02d:%02d", TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)), TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)), TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
    	return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)), TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
    }

	 public static String getColoredName(Player player) {
		 return "<col=" + player.right.getColor() + ">" + player.getName() + "@bla@";
	 }
	 
	 public static String highlightText(String input) {
		 return "@red@"+input+"@bla@";
	 }
	 public static String highlightText(int i) {
		 return "@red@"+i+"@bla@";
	 }
	 
	 public static String highlightGreenText(String input) {
		 return "@gre@" + input + "@bla@";
	 }
	 
	 public static String highlightBlueText(String input) {
		 return "@blu@" + input + "@bla@";
	 }
	 
	/**
	 * Created by Austin X (Rune-Server)
	 * @param input must contain a String similar to [Header]
	 * @return
	 */
	public static String formatHeader(String input) {
		return input.replace("[", "<img=15>[@blu@").replace("]", "@bla@]");
	}

	public static Date getDateFromString(String date) {
		try {
			Calendar calendar = new GregorianCalendar();
			String[] parts = date.toString().split(" ");
			String[] hms = parts[3].split(":");
			String mon = Integer.toString(calendar.get(Calendar.MONTH)) + 1;
			String newDateAsString = mon + "/" + parts[2] + "/" + hms[0] + "/" + hms[1] + "/" ;
			Date newDate = new SimpleDateFormat("MM/dd/HH/mm").parse(newDateAsString);
			return newDate;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getLastDayOfMonth() {
		Calendar calendar = new GregorianCalendar();
		switch (calendar.get(Calendar.MONTH)) {
		case 0: return 31;
		case 1: return 28;
		case 2: return 31;
		case 3: return 30;
		case 4: return 31;
		case 5: return 30;
		case 6: return 31;
		case 7: return 31;
		case 8: return 30;
		case 9: return 31;
		case 10: return 30;
		case 11: return 31;
		}
		return 0;
	}
	
	public static String salt(String data) {
        return "@pp13" + data + "p3n!$";
    }
	
	public static Date getModifiedDate(Date date, int days, int hours) {
		try {
			Calendar calendar = new GregorianCalendar();
			// dow mon dd hh:mm:ss zzz yyyy
			String[] parts = date.toString().split(" ");

			String dow = parts[0];

			String[] hms = parts[3].split(":");

			String hh = hms[0];
			String mm = hms[1];
			String ss = hms[2];
			
			String zzz = parts[4];
			String yyyy= parts[5];
			
			String mon = Integer.toString(calendar.get(Calendar.MONTH) + 1);
			
//			System.out.println("Month: " + calendar.get(Calendar.MONTH) + 1);
			
			String dd = parts[2];

			
			int month = Integer.parseInt(mon);
			int day = Integer.parseInt(dd);
			int hour = Integer.parseInt(hh);
			
			hour += hours;
			if (hour > 24) {
				day += hour / 24;
				hour %= 24;
			}
			day += days;
			
			if (day >= getLastDayOfMonth()) {
				if (month == 11)
					month %= 11;
				else
					month++;
				day -= getLastDayOfMonth();
			} 

			mon = month - 1 > 10 ? Integer.toString(month - 1) : "0" + Integer.toString(month - 1);
			hh = hour > 10 ? Integer.toString(hour) : "0" + Integer.toString(hour);
			dd = day > 10 ? Integer.toString(day) : "0" + Integer.toString(day);
			
			Calendar c = new GregorianCalendar();
			
//			System.out.println("Month B: " + month);
//			
//			System.out.println("Before: " + c.getTime().toString());
			// Tue/32/00/17/29/06/CDT/2019
			// DOW: tue, MON: 32, DD: 00, HH: 17, MM: 29, SS: 06, Year: 2019
//			System.out.println("Got: " + dow + "/" + mon + "/" + dd + "/"+hh+"/"+mm+"/"+ss+"/"+zzz+"/"+yyyy);
			
			c.set(Integer.parseInt(yyyy), Integer.parseInt(mon), Integer.parseInt(dd), Integer.parseInt(hh), Integer.parseInt(mm), Integer.parseInt(ss));
			
//			System.out.println("After: " + c.getTime().toString());
			
			return c.getTime();
			
//			String newDateAsString = dow + "/" + mon + "/" + dd + "/"+hh+"/"+mm+"/"+ss+"/"+zzz+"/"+yyyy;
			//this will just disable events, some reason it doesn't work on yours but works on mine, para's, and the host's lol
		    //Date newDate = new SimpleDateFormat("EEE/MM/dd/HH/mm/ss/zzz/yyyy").parse(newDateAsString);
		    /*System.out.println(date.toString());
		    System.out.println(newDateAsString);
		    System.out.println(newDate.toString());*/
//			return date;
		} catch (Exception e) {
			e.printStackTrace();
			return date;
		}
	}

}
