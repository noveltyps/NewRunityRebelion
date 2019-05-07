package io.server.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Logger {

	public static void handle(Throwable throwable) {
		System.out.println("ERROR! THREAD NAME: " + Thread.currentThread().getName());
		throwable.printStackTrace();
	}

	public static void log(Object message) {
		Date time = Calendar.getInstance().getTime();
		String text = "[" + time.toString() + "]" + " " + message.toString();
		System.err.println(text);
	}

	public static void log(String message) {
		System.out.println("[" + date() + "] -> " + message);
	}

	public static void log(String message, String... parents) {

		System.out.println("[" + date() + "] -> " + message);

		for (String parent : parents)
			parent(parent);

	}

	public static void parent(String message) {
		System.out.println("\t\t- " + message);
	}

	public static void parent(String message, int tabs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tabs; i++) {
			sb.append("\t");
		}
		System.out.println(sb.toString() + "- " + message);
	}

	public static void error(String message) {
		System.err.println("[" + date() + "] -> " + message);
	}

	public static void error(Exception message) {
		System.err.println("[" + date() + "] -> (" + message.getStackTrace()[0].getClassName() + ") Reason: "
				+ message.getMessage() + " at line: " + message.getStackTrace()[0].getLineNumber());
		message.printStackTrace();
	}

	public static void error(Throwable message) {
		System.err.println("[" + date() + "] -> (" + message.getStackTrace()[0].getClassName() + ") Reason: "
				+ message.getMessage() + " at line: " + message.getStackTrace()[0].getLineNumber());
		message.printStackTrace();
	}

	public static void tag() {
		System.out.println(" ");
		System.out.println();
		System.out.println("                                   Brutal Perfecting OSRS.  ");
		System.out.println();
		System.out.println("                             Parano1a and Dani.");
		System.out.println();
		System.out.println("--------------------------------------------------------------------------------");
	}

	public static String date() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("h:mm:ss a");
			Date now = new Date();
			String parsed = format.format(now);
			return parsed;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String f_date() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy' - 'HH-mm-ss");
			Date now = new Date();
			String parsed = format.format(now);
			return parsed;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private Logger() {

	}

}