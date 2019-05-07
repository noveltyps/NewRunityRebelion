package io.server.content;

import java.util.Arrays;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendString;

/**
 * Handles the starter kits.
 *
 * @author Daniel
 */
public class StarterKit {

	/** Handles opening the starter kit interface. */
	public static void open(Player player) {
		player.locking.lock();
		refresh(player, KitData.NORMAL);
		player.interfaceManager.open(57500);
	}

	/** Handles refreshing the starter kit interface. */
	public static void refresh(Player player, StarterKit.KitData kit) {
		player.attributes.set("STARTER_KEY", kit);
		for (int index = 0, string = 57504; index < 4; index++, string += 1) {
			String desc = index >= kit.getDescription().length ? "" : kit.getDescription()[index];
			player.send(new SendString(desc, string));
		}

		player.equipment.clear();
		Arrays.stream(kit.getEquipment()).forEach(player.equipment::manualWear);
		player.send(new SendConfig(1085, kit.ordinal()));
		player.send(new SendItemOnInterface(57521, kit.getItems()));
		player.equipment.refresh();
		player.inventory.refresh();
	}

	/** Holds the starter kit data. */
	public enum KitData {

		NORMAL(PlayerRight.PLAYER, new String[] {
				"", "Play RebelionX as a normal player.", "As a normal player you will have no restrictions at all." },
				new Item[] { new Item(386, 300), new Item(1856, 1), new Item(565, 1000), new Item(555, 5000), new Item(560, 4000),
						new Item(557, 4000), new Item(3025, 75), new Item(6686, 20), new Item(10028, 1), new Item(995, 1000000) }),

		CLASSIC(PlayerRight.CLASSIC, new String[] {
				"", "Play RebelionX as a Classic player.", "As a normal player you will have no restrictions at all." },
				new Item[] { new Item(10828, 1), new Item(1127, 1), new Item(1856, 1), new Item(1079, 1), new Item(3105),
						new Item(386, 300), new Item(565, 1000), new Item(555, 5000), new Item(560, 4000),
						new Item(557, 4000), new Item(3025, 75), new Item(6686, 20), new Item(12696, 10), new Item(841),
						new Item(861), new Item(995, 850000), new Item(1321), new Item(1325), new Item(4587),
						new Item(1215), new Item(884, 1000), new Item(1704), new Item(13111) }),

		IRONMAN(PlayerRight.IRONMAN,
				new String[] { "Playing as an ironman will restrict you from trading, picking up drops from other",
						"player's kills including pvp or if another player has dealt any damage to a npc,",
						"access to certain shops, access from using the marketplace, and playing certain minigames",
						"which include duel arena. You will have access to ironman armour and a distinct rank. " },
				new Item[] { new Item(16657), new Item(16659), new Item(16659), new Item(16662),
						new Item(4119) }, 
				new Item[] { new Item(995, 1000000), new Item(1351, 1), new Item(590, 1), new Item(303, 1),
						new Item(315, 1), new Item(1856, 1), new Item(1925, 1), new Item(1931, 1), new Item(2309, 1), new Item(1265, 1),
						new Item(3273, 1), 
						new Item(556, 250), new Item(558, 150), new Item(555, 60), new Item(557, 40), new Item(559, 20) }),

		ULTIMATE_IRONMAN(PlayerRight.ULTIMATE_IRONMAN,
				new String[] { "In addition to all the regular ironman rules the following conditions apply as well:",
						"The use of banking (they are still able to use noted items on bank booths to unnote them)",
						"Keep any item on death nor use the Protect Item prayer ", },
				new Item[] { new Item(16657), new Item(16659), new Item(16659), new Item(16662),
						new Item(4119) }, 
				new Item[] { new Item(995, 1000000), new Item(1351, 1), new Item(590, 1), new Item(303, 1),
						new Item(315, 1), new Item(1856, 1), new Item(1925, 1), new Item(1931, 1), new Item(2309, 1), new Item(1265, 1),
						new Item(3273, 1), 
						new Item(556, 250), new Item(558, 150), new Item(555, 60), new Item(557, 40), new Item(559, 20) }),

		HARDCORE_IRONMAN(PlayerRight.HARDCORE_IRONMAN,
				new String[] {
						"Hardcore Ironman players will only have one life, in addition to the standard restrictions",
						"given to all ironmen. If a hardcore ironman dies, they will be converted to a Ironman",
						"chat badge standard ironman. Safe deaths, such as those in minigames, will not cause",
						"the player to become a standard ironman.", },
				new Item[] { new Item(20792), new Item(20794), new Item(20796), new Item(1277), new Item(1173),
						new Item(4119) },
				new Item[] { new Item(995, 1000000), new Item(1351, 1), new Item(590, 1), new Item(303, 1),
						new Item(315, 1), new Item(1925, 1), new Item(1856, 1), new Item(1931, 1), new Item(2309, 1), new Item(1265, 1),
						new Item(1205, 1), new Item(1277, 1), new Item(1171, 1), new Item(841, 1), new Item(882, 25),
						new Item(556, 25), new Item(558, 15), new Item(555, 6), new Item(557, 4), new Item(559, 2) });

		/** The player right of the starter kit. */
		private final PlayerRight right;

		/** The starter kit description. */
		private final String[] description;

		/** The starter kit equipment items. */
		private final Item[] equipment;

		/** The starter kit items. */
		private final Item[] items;

		/** Constructs a new <code>KitData</code>. */
		KitData(PlayerRight right, String[] description, Item[] equipment, Item[] items) {
			this.right = right;
			this.description = description;
			this.equipment = equipment;
			this.items = items;
		}

		/** Constructs a new <code>KitData</code>. */
		KitData(PlayerRight right, String[] description, Item[] items) {
			this.right = right;
			this.description = description;
			this.equipment = new Item[] {};
			this.items = items;
		}

		public PlayerRight getRight() {
			return right;
		}

		public String[] getDescription() {
			return description;
		}

		public Item[] getEquipment() {
			return equipment;
		}

		public Item[] getItems() {
			return items;
		}

	}

}
