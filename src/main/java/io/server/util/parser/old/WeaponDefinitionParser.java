package io.server.util.parser.old;

import com.google.gson.JsonObject;

import io.server.game.world.entity.combat.ranged.RangedWeaponDefinition;
import io.server.util.parser.GsonParser;
import io.server.util.parser.old.defs.WeaponDefinition;

public class WeaponDefinitionParser extends GsonParser {

	public WeaponDefinitionParser() {
		super("def/item/weapon_definitions");
	}

	@Override
	protected void parse(JsonObject data) {
		int id = data.get("id").getAsInt();
		String name = data.get("name").getAsString();
		boolean twoHanded = data.get("twoHanded").getAsBoolean();
		String weaponType = data.get("weaponType").getAsString();

		RangedWeaponDefinition rangedDefinition = null;
		if (data.has("rangedDefinition")) {
			rangedDefinition = builder.fromJson(data.get("rangedDefinition"), RangedWeaponDefinition.class);
		}

		int standAnimation = data.get("stand").getAsInt();
		int walkAnimation = data.get("walk").getAsInt();
		int runAnimation = data.get("run").getAsInt();

		WeaponDefinition.DEFINITIONS[id] = new WeaponDefinition(id, name, twoHanded, weaponType, rangedDefinition,
				standAnimation, walkAnimation, runAnimation);
	}

}
