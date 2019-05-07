package io.server.game.world.entity.mob.player.persist;

import java.sql.SQLException;

import com.jcabi.jdbc.JdbcSession;

import io.server.game.world.entity.mob.player.Player;

public abstract class PlayerDBProperty {

	private final String name;

	public PlayerDBProperty(String name) {
		this.name = name;
	}

	abstract void read(Player player, JdbcSession session) throws SQLException;

	abstract void write(Player player, JdbcSession session) throws SQLException;

	public String getName() {
		return name;
	}

}
