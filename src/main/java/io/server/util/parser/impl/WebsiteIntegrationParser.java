package io.server.util.parser.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.server.util.Logger;

/**
 * @author Ethan Kyle Millard <skype:pumpklins>
 * @since Mon, May 07, 2018 @ 8:10 PM
 */
public class WebsiteIntegrationParser {

	public WebsiteIntegrationParser() {

	}

	/**
	 * The method allows a user to modify the data as its being parsed.
	 *
	 * @param reader The {@code JsonObject} that contains all serialized
	 *               information.
	 */
	public void parse(JsonObject reader) {
		if (reader.has("username")) {
			Logger.log("Username " + reader.get("username").getAsString());
		}

		/*
		 * if (reader.has("total_level")) {
		 * OSBLoader.stat_total.setText(reader.get("total_level").getAsString()); }
		 * 
		 * if (reader.has("overall_xp")) { Client.totalExperience =
		 * Long.parseLong(reader.get("overall_xp").getAsString()); }
		 * 
		 * int attack =
		 * getLevelForExperience(Integer.parseInt(reader.get(SKILLS[0]).getAsString()));
		 * int defence =
		 * getLevelForExperience(Integer.parseInt(reader.get(SKILLS[1]).getAsString()));
		 * int strength =
		 * getLevelForExperience(Integer.parseInt(reader.get(SKILLS[2]).getAsString()));
		 * int hp =
		 * getLevelForExperience(Integer.parseInt(reader.get(SKILLS[3]).getAsString()));
		 * int ranged =
		 * getLevelForExperience(Integer.parseInt(reader.get(SKILLS[4]).getAsString()));
		 * int prayer =
		 * getLevelForExperience(Integer.parseInt(reader.get(SKILLS[5]).getAsString()));
		 * int magic =
		 * getLevelForExperience(Integer.parseInt(reader.get(SKILLS[6]).getAsString()));
		 * OSBLoader.stat_combat.setText(String.valueOf(calculateCombat(attack, defence,
		 * strength, hp, ranged, prayer, magic)));
		 * 
		 * for (int i = 1; i < SKILLS.length; i++) { if (reader.has(SKILLS[i])) {
		 * Client.experience[i] = Integer.parseInt(reader.get(SKILLS[i -
		 * 1]).getAsString());
		 * OSBLoader.stat[i].setText(String.valueOf(getLevelForExperience(Integer.
		 * parseInt(reader.get(SKILLS[i - 1]).getAsString())))); } } for (int i = 1; i <
		 * PRESTIGES.length; i++) { if (reader.has(PRESTIGES[i])) { Client.prestige[i] =
		 * Integer.parseInt(reader.get(PRESTIGES[i - 1]).getAsString()); } }
		 */
	}

	/**
	 * The method that deserializes the file information.
	 */
	public void deserialize(String username, String password) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new URL("http://runity.io/osbuddy/highscores.php?username=" + username + "&password=" + MD5(password))
						.openStream(),
				"UTF8"))) {
			final JsonParser parser = new JsonParser();
			final JsonElement element = parser.parse(reader);

			if (element.isJsonNull()) {
				return;
			}

			if (element.isJsonArray()) {
				final JsonArray array = element.getAsJsonArray();
				for (int index = 0; index < array.size(); index++) {
					JsonObject data = (JsonObject) array.get(index);
					parse(data);
				}
			} else if (element.isJsonObject()) {
				parse(element.getAsJsonObject());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String MD5(String md5) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte anArray : array) {
				sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ignored) {

		}
		return null;
	}
}
