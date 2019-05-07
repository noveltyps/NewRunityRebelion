package io.server.util.parser.old;

import com.google.gson.JsonObject;

import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.parser.GsonParser;
import io.server.util.parser.old.defs.NpcDefinition;

/**
 * Parses through the npc spawn file and creates {@link Npc}s on startup.
 * 
 * @author Daniel | Obey
 */
public class NpcDefinitionParser extends GsonParser {

	public NpcDefinitionParser() {
		super("wiki/client_npc_definitions");
	}

	@Override
	protected void parse(JsonObject data) {
		int id = data.get("id").getAsInt();
		String name = data.get("name").getAsString();

		NpcDefinition definition = NpcDefinition.DEFINITIONS[id] = new NpcDefinition(id, name);

		definition.size = 1;
		if (data.has("size"))
			definition.size = data.get("size").getAsInt();

		definition.combatLevel = 1;
		if (data.has("combat-level"))
			definition.combatLevel = data.get("combat-level").getAsInt();

		definition.attackable = false;
		if (data.has("attackable"))
			definition.attackable = data.get("attackable").getAsBoolean();

		definition.stand = data.get("stand").getAsInt();
		definition.walk = data.get("walk").getAsInt();

		definition.turn180 = definition.walk;
		if (data.has("turn180"))
			definition.turn180 = data.get("turn180").getAsInt();

		definition.turn90CW = definition.walk;
		if (data.has("turn90CW"))
			definition.turn90CW = data.get("turn90CW").getAsInt();

		definition.turn90CCW = definition.walk;
		if (data.has("turn90CCW"))
			definition.turn90CCW = data.get("turn90CCW").getAsInt();
	}

}
