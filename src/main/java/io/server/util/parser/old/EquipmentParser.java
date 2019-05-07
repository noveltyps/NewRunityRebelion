package io.server.util.parser.old;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import io.server.game.world.items.SkillRequirement;
import io.server.util.parser.GsonParser;

public class EquipmentParser extends GsonParser {

	public static Map<Integer, Loader> LOADED = new HashMap<>();

	public EquipmentParser() {
		super("equip");
	}

	@Override
	protected void parse(JsonObject data) {
		final int id = data.get("id").getAsInt();
		SkillRequirement[] requirement = new SkillRequirement[] {};

		if (data.has("requirements")) {
			requirement = builder.fromJson(data.get("requirements"), SkillRequirement[].class);
		}

		LOADED.put(id, new Loader(id, requirement));
	}

	static class Loader {
		private final int id;
		private final SkillRequirement[] reqs;

		public Loader(int id, SkillRequirement[] req) {
			this.id = id;
			this.reqs = req;
		}

		public int getId() {
			return id;
		}

		public SkillRequirement[] getReqs() {
			return reqs;
		}
	}
}
