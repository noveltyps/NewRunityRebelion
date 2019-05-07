package io.server.content.masterminer;

public class Util {
	public static String toNiceString(long value) {
		// Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
		if (value == Long.MIN_VALUE)
			return toNiceString(Long.MIN_VALUE + 1);
		if (value < 0)
			return "-" + toNiceString(-value);
		if (value < 100000)
			return Long.toString(value); // deal with easy case
		if (value < 10000000)
			return value / 1000 + "k";
		if (value < 10000000000L)
			return value / 1000000 + "m";
		return value / 1000000000 + "b";
	}

	public static String toTime(long value) {
		value /= 1000;
		int seconds = (int) (value % 60);
		int minutes = (int) (value / 60 % 60);
		int hours = (int) (value / 3_600);
		return (hours > 0 ? hours + ":" : "") + String.format("%02d:%02d", minutes, seconds);
	}

	public static String toTimeLong(long value) {
		value /= 1000;
		return (value / 3_600) + " hours " + value / 60 % 60 + " minutes and " + value % 60 + " seconds";
	}
}
