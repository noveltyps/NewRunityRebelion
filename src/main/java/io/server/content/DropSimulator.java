package io.server.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.npc.drop.NpcDrop;
import io.server.game.world.entity.mob.npc.drop.NpcDropManager;
import io.server.game.world.entity.mob.npc.drop.NpcDropTable;
import io.server.game.world.entity.mob.npc.dropchance.DropChanceHandler;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.pricechecker.PriceType;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendScrollbar;
import io.server.net.packet.out.SendString;
import io.server.net.packet.out.SendTooltip;
import io.server.util.RandomGen;
import io.server.util.Stopwatch;
import io.server.util.Utility;

/**
 * This class simulates drops of an npc and places it on an itemcontainer.
 *
 * @author Daniel.
 */
public class DropSimulator {

	/** The default NPCs that will have their drops simulated. */
	private static final int[] DEFAULT = { 3080 };

	/** Handles opening the drop simulator itemcontainer. */
	public static void open(Player player) {
		int npc = Utility.randomElement(DEFAULT);
		String name = NpcDefinition.get(npc).getName();
		drawList(player, name);
		displaySimulation(player, npc, 100);
		player.send(new SendString(name, 26810, true));
		player.interfaceManager.open(26800);
	}

	/** Handles drawing the lsit of npcs based off the search context. */
	public static void drawList(Player player, String context) {
		List<String> npc = new ArrayList<>();
		List<Integer> button = new ArrayList<>();
		for (NpcDefinition definition : NpcDefinition.DEFINITIONS) {
			if (npc.size() >= 50)
				break;
			if (definition == null)
				continue;
			if (!NpcDropManager.NPC_DROPS.containsKey(definition.getId())) {
				continue;
			}
			if (!definition.getName().toLowerCase().contains(context.toLowerCase())) {
				continue;
			}
			if (npc.contains(definition.getName()))
				continue;
			npc.add(definition.getName());
			button.add(definition.getId());
		}
		int size = npc.size() < 14 ? 14 : npc.size();
		for (int index = 0, string = 26851; index < size; index++, string++) {
			String name = index >= npc.size() ? "" : npc.get(index);
			player.send(new SendTooltip(name.isEmpty() ? "" : "Open drop simulator for " + name, string));
			player.send(new SendString(name, string));
		}
		player.attributes.set("DROP_SIMULATOR_BUTTON_KEY", button);
		player.send(new SendScrollbar(26850, size * 15));
	}

	/** Handles displaying the simulated drops. */
	public static void displaySimulation(Player player, int id, int amount) {
		StopWatch timer = new StopWatch();
		timer.reset();
		timer.start();
		if (amount > 100_000) {
			amount = 100_000;
		}
		NpcDefinition npc = NpcDefinition.get(id);
		if (npc == null)
			return;
		NpcDropTable dropTable = NpcDropManager.NPC_DROPS.get(id);
		if (dropTable == null)
			return;
		Map<Integer, Item> items = new HashMap<>();
		long value = 0;
		int rate = DropChanceHandler.getRate(player);
		Map<Integer, Long> npc_drops = new HashMap<Integer, Long>();
		for (int index = 0; index < amount; index++) {
			dropTable.generate(player, npc_drops, rate);
		}
		System.out.println(timer.getTime(TimeUnit.MILLISECONDS) + " ms to simulate: " + amount);
		timer.reset();
		timer.start();
		for (Entry<Integer, Long> entry : npc_drops.entrySet()) {
			NpcDrop drop = dropTable.drops[entry.getKey()];
			Item item = drop.toItem(entry.getValue());
			value += item.getValue(PriceType.VALUE) * item.getAmount();
			items.compute(item.getId(), (key, val) -> val == null ? item
					: val.getId() == item.getId() ? val.createAndIncrement(item.getAmount()) : val);
		}
		System.out.println(timer.getTime(TimeUnit.MILLISECONDS) + " ms to simulate: " + amount);
		timer.reset();
		timer.start();
		TreeSet<Item> sorted = new TreeSet<>(
				(first, second) -> second.getValue() * second.getAmount() - first.getValue() * first.getAmount());
		sorted.addAll(items.values());
		player.attributes.set("DROP_SIMULATOR_SORTED_LIST", sorted.toArray(new Item[0]));
		int scroll = (items.size() / 6 + (items.size() % 6 > 0 ? 1 : 0)) * 44;
		player.send(new SendScrollbar(26815, scroll));
		player.send(new SendItemOnInterface(26816, sorted.toArray(new Item[0])));
		player.send(new SendString(amount, 26811, true));
		player.send(new SendString("<col=C1A875>" + npc.getName(), 26806));
		player.send(new SendString("Simulated <col=C1A875>" + Utility.formatDigits(amount) + "</col> drops", 26807));
		player.send(new SendString("Total value: <col=01FF80>" + Utility.formatDigits(value) + "</col>", 26808));
		player.attributes.set("DROP_SIMULATOR_KEY", id);
		System.out.println(timer.getTime(TimeUnit.MILLISECONDS) + " ms to simulate: " + amount);
	}
}
