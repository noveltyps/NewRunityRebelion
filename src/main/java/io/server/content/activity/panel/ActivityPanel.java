package io.server.content.activity.panel;

import io.server.Config;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendForceTab;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendProgressBar;
import io.server.net.packet.out.SendString;

/**
 * Handles the activity panel.
 *
 * @author Daniel.
 */
public class ActivityPanel {
	/** Sends all the information for the activity panel. */
	public static void update(Player player, int amount, String title, String footer, String... strings) {
		update(player, amount, title, footer, new Item[] {}, strings);
	}

	/** Sends all the information for the activity panel. */
	public static void update(Player player, int amount, String title, Item item, String... strings) {
		update(player, amount, title, "Activity Completion:", new Item[] { item }, strings);
	}

	/** Sends all the information for the activity panel. */
	public static void update(Player player, int amount, String header, String footer, Item container,
			String... strings) {
		update(player, amount, header, footer, new Item[] { container }, strings);
	}

	/** Sends all the information for the activity panel. */
	public static void update(Player player, int amount, String header, String footer, Item[] container,
			String... strings) {
		if (!player.interfaceManager.isSidebar(Config.ACTIVITY_TAB, 38000)) {
			player.send(new SendForceTab(Config.ACTIVITY_TAB));
		}
		player.interfaceManager.setSidebar(Config.ACTIVITY_TAB, 38000);
		for (int index = 0, string = 38005; index < strings.length; index++, string += 1) {
			player.send(new SendString(strings[index], string));
		}
		if (container != null) {
			player.send(new SendItemOnInterface(38016, container));
		}
		player.send(new SendString(header, 38003));
		player.send(new SendProgressBar(38015, amount));
		player.send(new SendString(amount == -1 ? "" : footer, 38004));
	}

	/** Clears the activity panel. */
	public static void clear(Player player) {
		player.send(new SendItemOnInterface(38016));
		player.send(new SendString("", 38003));
		player.send(new SendString("", 38004));
		player.send(new SendString("", 38005));
		player.send(new SendString("", 38006));
		player.send(new SendString("", 38007));
		player.send(new SendString("", 38008));
		player.send(new SendProgressBar(38008, -1));
		player.send(new SendForceTab(Config.INVENTORY_TAB));
		player.interfaceManager.setSidebar(Config.ACTIVITY_TAB, -1);
	}

	/** Clears the activity panel after a defined set of time. */
	public static void timedClear(Player player, int delay) {
		World.schedule(delay, () -> clear(player));
	}
}
