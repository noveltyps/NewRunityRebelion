package io.server.util.sql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import io.server.util.sql.MySqlCommandListener;
import io.server.util.sql.MySqlConnector;

/**
 * Inserts the log of the player into the database
 * 
 * @author Nerik#8690
 *
 */
public class MySqlCreateTableCommand implements MySqlCommandListener {

	@Override
	public void execute(String log) {
		try {
			Connection connection = DriverManager.getConnection(MySqlConnector.URL, MySqlConnector.USERNAME,
					MySqlConnector.PASSWORD);
			PreparedStatement table = connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS logs(id int NOT NULL AUTO_INCREMENT, LOG varchar(255), DATE varchar(255), PRIMARY KEY(id))");
			table.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("[MySql Manager] Finished creating table!");
		}
	}

}
