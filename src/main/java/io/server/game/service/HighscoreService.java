package io.server.game.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.util.Logger;

public final class HighscoreService {

	private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/runity";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";

	private final static String HIGHSCORES_GATHER = "SELECT prestige, skill, experience, entry_time FROM highscores WHERE account_name = ?";
	private final static String HIGHSCORES_INSERT = "INSERT INTO highscores (account_name, mode, prestige, skill, level, experience, entry_time) VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE prestige = VALUES(prestige), level = VALUES(level), experience = VALUES(experience), entry_time = VALUES(entry_time)";

	public static void saveHighscores(Player player) {
		if (player == null || !System.getProperty("user.name")
				.equalsIgnoreCase("runity")/*
											 * || !Config.LIVE_SERVER || !Config.FORUM_INTEGRATION ||
											 * PlayerRight.isPriviledged(player) || !Config.highscoresEnabled
											 */) {
			return;
		}

		Logger.log("Preparing highscores for " + player.getUsername() + "...");
		new Thread(() -> {

			Connection c = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;

			try {

				c = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);

				// Gather all ECO high-score entries of the player
				stmt = c.prepareStatement(HIGHSCORES_GATHER);
				stmt.setString(1, player.getUsername());
				rs = stmt.executeQuery();

				final Map<Integer, Pair<Integer, Timestamp>> entries = new HashMap<>();

				while (rs.next()) {
					entries.put(rs.getInt("skill"), Pair.of(rs.getInt("experience"), rs.getTimestamp("entry_time")));
				}

				// Insert/update the ECO entries
				stmt = c.prepareStatement(HIGHSCORES_INSERT);
				final Timestamp ts = new Timestamp(System.currentTimeMillis());

				for (int i = 0; i < Skill.SKILL_COUNT; i++) {

					final Pair<Integer, Timestamp> entry = entries.get(i);
					final Skill skill = player.skills.get(i);
					final int experience = (int) Math.ceil(skill.getRoundedExperience());

					stmt.setString(1, player.getUsername());
					stmt.setInt(2, getRank(player.right));
					stmt.setInt(3, player.prestige.prestige[i]);
					stmt.setInt(4, i);
					stmt.setInt(5, skill.getMaxLevel());
					stmt.setInt(6, experience);

					// Check if the player already reached the maximum experience so
					// it can keep the current position
					if (entry != null && entry.getLeft() >= 200_000_000) {
						stmt.setTimestamp(7, entry.getRight());
					} else {
						stmt.setTimestamp(7, ts);
					}

					stmt.addBatch();

				}

				stmt.executeBatch();

				// PreparedStatement dsta = connection.prepareStatement("DELETE FROM highscores
				// WHERE username = ?");
				// PreparedStatement ista = connection.prepareStatement(generateQuery());
				//
				// dsta.setString(1, player.getUsername());
				// dsta.execute();
				//
				// ista.setString(1, player.getUsername());
				// ista.setInt(2, getRank(player.right));
				// ista.setInt(3, getRank(player.right));
				// ista.setInt(4, player.prestige.totalPrestige);
				//
				// for (int x = 0; x < Skill.SKILL_COUNT; x++) {
				// ista.setInt(5 + x, player.prestige.prestige[x]);
				// }
				//
				// ista.setInt(28, player.skills.getTotalLevel());
				// ista.setLong(29, player.skills.getTotalXp());
				//
				// for (int i = 0; i < Skill.SKILL_COUNT; i++) {
				// ista.setInt(30 + i, player.skills.get(i).getRoundedExperience());
				// }
				//
				// ista.execute();

			} catch (SQLException ex) {
				Logger.error(String.format("Failed to save highscores for player=%s", player.getName()));
				Logger.parent(ex.getMessage());
			} finally {
				if (c != null) {
					try {
						c.close();
					} catch (Exception ignored) {
					}
				}
			}

		}).start();
	}

	private static int getRank(PlayerRight right) {
		if (right == PlayerRight.ULTIMATE_IRONMAN)
			return 3;
		if (right == PlayerRight.HARDCORE_IRONMAN)
			return 2;
		if (right == PlayerRight.IRONMAN)
			return 1;
		return 0;
	}
/*
	private static String generateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO highscores (");
		sb.append("\"username\", ");
		sb.append("\"rights\", ");
		sb.append("\"mode\", ");
		sb.append("\"total_prestiges\", ");
		sb.append("\"attack_prestiges\", ");
		sb.append("\"defence_prestiges\", ");
		sb.append("\"strength_prestiges\", ");
		sb.append("\"hitpoints_prestiges\", ");
		sb.append("\"ranged_prestiges\", ");
		sb.append("\"prayer_prestiges\", ");
		sb.append("\"magic_prestiges\", ");
		sb.append("\"cooking_prestiges\", ");
		sb.append("\"woodcutting_prestiges\", ");
		sb.append("\"fletching_prestiges\", ");
		sb.append("\"fishing_prestiges\", ");
		sb.append("\"firemaking_prestiges\", ");
		sb.append("\"crafting_prestiges\", ");
		sb.append("\"smithing_prestiges\", ");
		sb.append("\"mining_prestiges\", ");
		sb.append("\"herblore_prestiges\", ");
		sb.append("\"agility_prestiges\", ");
		sb.append("\"thieving_prestiges\", ");
		sb.append("\"slayer_prestiges\", ");
		sb.append("\"farming_prestiges\", ");
		sb.append("\"runecrafting_prestiges\", ");
		sb.append("\"hunter_prestiges\", ");
		sb.append("\"construction_prestiges\",");
		sb.append("\"total_level\", ");
		sb.append("\"overall_xp\", ");
		sb.append("\"attack_xp\", ");
		sb.append("\"defence_xp\", ");
		sb.append("\"strength_xp\", ");
		sb.append("\"hitpoints_xp\", ");
		sb.append("\"ranged_xp\", ");
		sb.append("\"prayer_xp\", ");
		sb.append("\"magic_xp\", ");
		sb.append("\"cooking_xp\", ");
		sb.append("\"woodcutting_xp\", ");
		sb.append("\"fletching_xp\", ");
		sb.append("\"fishing_xp\", ");
		sb.append("\"firemaking_xp\", ");
		sb.append("\"crafting_xp\", ");
		sb.append("\"smithing_xp\", ");
		sb.append("\"mining_xp\", ");
		sb.append("\"herblore_xp\", ");
		sb.append("\"agility_xp\", ");
		sb.append("\"thieving_xp\", ");
		sb.append("\"slayer_xp\", ");
		sb.append("\"farming_xp\", ");
		sb.append("\"runecrafting_xp\", ");
		sb.append("\"hunter_xp\", ");
		sb.append("\"construction_xp\")");
		sb.append("VALUES (" + "?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?)");
		return sb.toString();
	}*/

	private HighscoreService() {

	}

}