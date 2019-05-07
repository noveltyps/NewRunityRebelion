package io.server.util.parser.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.crypto.spec.GCMParameterSpec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.server.game.world.entity.mob.npc.drop.NpcDrop;
import io.server.game.world.entity.mob.npc.drop.NpcDropChance;
import io.server.game.world.entity.mob.npc.drop.NpcDropManager;
import io.server.game.world.entity.mob.npc.drop.NpcDropTable;
import io.server.game.world.items.ItemDefinition;
import io.server.util.parser.GsonParser;

/**
 * Loads npc drops on startup.
 *
 * @author Daniel
 */
public class NpcDropParser extends GsonParser {

	public NpcDropParser() {
		super("def/npc/npc_drops", false);
	}

	@Override
	protected void parse(JsonObject data) {
		final int[] npcIds = builder.fromJson(data.get("id"), int[].class);
		final boolean rareDropTable = data.get("rare_table").getAsBoolean();
		NpcDrop[] npcDrops = builder.fromJson(data.get("drops"), NpcDrop[].class);

		for (NpcDrop drop : npcDrops) {
			if (drop == null) {
				System.out.println("Null drop in drops in npc_drops.json");
				continue;
			}
			ItemDefinition definition = ItemDefinition.get(drop.item);

			if (definition == null) {
				System.out.println("Null item in drop, item id: " + drop.item);
				continue;
			}
			
			if (!definition.isStackable() && drop.maximum > 1 && definition.isNoteable()) {
				drop.setItem(definition.getNotedId());
			}

			if (drop.item == 12073) {// elite clue
				drop.setType(NpcDropChance.VERY_RARE);
				continue;
			}

			if (drop.item == 2722) {// hard clue
				drop.setType(NpcDropChance.RARE);
				continue;
			}

			if (drop.item == 2801) {// medium clue
				drop.setType(NpcDropChance.UNCOMMON);
				continue;
			}

			if (drop.item == 2677) {// easy clue
				drop.setType(NpcDropChance.COMMON);
				continue;
			}
		}

		Arrays.sort(npcDrops, new Comparator<NpcDrop>() {

			@Override
			public int compare(NpcDrop o1, NpcDrop other) {
				return (o1.type.equals(NpcDropChance.ALWAYS) ? 100000000 : o1.chance) - (other.type.equals(NpcDropChance.ALWAYS) ? 100000000 : other.chance);
			}
		});
		
		int totalWeight = 0;
		for (NpcDrop drop : npcDrops) {
			totalWeight += drop.chance;
			drop.cumulativeWeight = totalWeight;
			//System.out.println(ItemDefinition.get(drop.item).getName() + " " + drop.chance + " " + drop.cumulativeWeight);
		}

		for (int id : npcIds) {
			NpcDropManager.NPC_DROPS.put(id, new NpcDropTable(npcIds, rareDropTable, npcDrops));
		}
	}
	
}
