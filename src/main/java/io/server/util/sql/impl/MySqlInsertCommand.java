package io.server.util.sql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

import io.server.util.sql.MySqlCommandListener;
import io.server.util.sql.MySqlConnector;

/**
 * Inserts the log of the player into the database
 * 
 * @author Nerik#8690
 *
 */
public class MySqlInsertCommand implements MySqlCommandListener {

	@Override
	public void execute(String log) {
		Date date = new Date();
		try {
			Connection connection = DriverManager.getConnection(MySqlConnector.URL, MySqlConnector.USERNAME,
					MySqlConnector.PASSWORD);
			PreparedStatement insert = connection
					.prepareStatement("INSERT INTO logs (LOG, DATE) VALUES ('" + log + "','" + date + "')");
			insert.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("[MySql Manager] Finished inserting");
		}
	}

}
