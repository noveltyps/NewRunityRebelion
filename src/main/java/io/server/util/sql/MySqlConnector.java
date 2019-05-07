package io.server.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector {

	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USERNAME = "nerik";
	public static final String PASSWORD = "5DxbCzinL4LEXGlK";
	public static final String URL = "jdbc:mysql://localhost/logs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public static void run() throws ClassNotFoundException, SQLException {

		Class.forName(DRIVER);
		MySqlCommandManager.load();

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			MySqlLogHandler.run(MySqlCommands.CREATE_TABLE, "");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
