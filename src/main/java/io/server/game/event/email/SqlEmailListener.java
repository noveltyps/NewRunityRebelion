package io.server.game.event.email;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlEmailListener {

	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USERNAME = "nerik";
	public static final String PASSWORD = "5DxbCzinL4LEXGlK";
	public static final String URL = "jdbc:mysql://localhost/data?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	private String username, email;

	public SqlEmailListener(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public void connect() throws ClassNotFoundException, SQLException {

		Class.forName(DRIVER);

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			insertEvent(username, email);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private void insertEvent(String username, String email) {
		try {
			Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO email_data (username, email) VALUES ('" + username + "', '" + email + "')");
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
