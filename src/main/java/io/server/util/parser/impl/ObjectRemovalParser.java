package io.server.util.parser.impl;

import com.google.gson.JsonObject;

import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.util.parser.GsonParser;

/**
 * Handles parsing the removed object.
 *
 * @author Daniel
 */
public class ObjectRemovalParser extends GsonParser {

	public ObjectRemovalParser() {
		super("def/object/removed_objects", false);
	}

	@Override
	protected void parse(JsonObject data) {
		Position position = builder.fromJson(data.get("position"), Position.class);
		Region.SKIPPED_OBJECTS.add(position);

	}
}