package io.server.util.sql;

import java.util.HashMap;
import java.util.Map;

import io.server.util.sql.impl.MySqlCreateTableCommand;
import io.server.util.sql.impl.MySqlInsertCommand;

public class MySqlCommandManager {

	public static Map<MySqlCommands, MySqlCommandListener> commands = new HashMap<>();

	public static void load() {

		commands.put(MySqlCommands.INSERT, new MySqlInsertCommand());
		commands.put(MySqlCommands.CREATE_TABLE, new MySqlCreateTableCommand());
	}
}
