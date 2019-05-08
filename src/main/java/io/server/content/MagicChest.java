package io.server.content;

import java.util.Arrays;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.util.chance.Chance;
import io.server.util.chance.WeightedChance;

/**
 * Handles opening the crystal chest.
 *
 * @author Daniel
 */
public class MagicChest {

	/** The two item key halves. */
	public static final Item[] MKEY_HALVES = { new Item(1547), new Item(1547) };
	/** The item key to enter the crystal chest. */
	public static final Item MKEY = new Item(1547);

	/** Handles creating a key. */
	
	public static void createMKey(Player player) {
		if (player.inventory.containsAll(MKEY_HALVES)) {
			player.inventory.remove(MKEY_HALVES[0]);
			player.inventory.remove(MKEY_HALVES[1]);
			player.inventory.add(MKEY);
			player.dialogueFactory
					.sendItem("Magic Key", "You have combined the two parts to form a Magic key.", MKEY.getId()).execute();
		}
		
	}

	/** Handles getting an item reward from the chest. */
	public static Item getReward() {
		return ITEMS.next();
	}

	/** Holds all the crystal chest rewards. */
	private static final Chance<Item> ITEMS = new Chance<>(Arrays.asList(new WeightedChance<>(8, new Item(1163, 1)), // RUNE_FULL_HELM
			new WeightedChance<>(6, new Item(2453, 100)), // antifire pot
					new WeightedChance<>(6,new Item(2447, 100)), // antiposion
							new WeightedChance<>(6,new Item(12906, 100)), // anti-venom
									new WeightedChance<>(6,new Item(12912, 100)), // anti-venom
											new WeightedChance<>(6,new Item(2445, 100)), // ranging pot
													new WeightedChance<>(6,new Item(3041, 100)), // magic pot
															new WeightedChance<>(6,new Item(12696, 100)), // super combat
																	new WeightedChance<>(6,new Item(1713, 35)), // amulet of glory
																			new WeightedChance<>(6,new Item(11959, 100)), // black chin
																					new WeightedChance<>(6,new Item(10034, 100)), // red chin
																							new WeightedChance<>(6,new Item(537, 100)), // dragon bones
																									new WeightedChance<>(6,new Item(11944, 100)), // lava dragon bones
																											new WeightedChance<>(6,new Item(6816, 100)), // wyvern bones

																													new WeightedChance<>(6,new Item(9342, 100)), // onyx bolts
																							new WeightedChance<>(6,new Item(10280, 1)), // willow comp bow
																									new WeightedChance<>(6,new Item(10282, 1)), // yew comp bow
																											new WeightedChance<>(6,new Item(10284, 1)), // magic comp bow
																													new WeightedChance<>(6,new Item(1780, 100)), // flax


	
			new WeightedChance<>(5, new Item(10828, 1)), // HELM_OF_NEITZ
			new WeightedChance<>(5, new Item(1632, 1)), // UNCUT_DAGONSTONE
			new WeightedChance<>(5, new Item(4099, 1)), // MYSTIC_HAT
			new WeightedChance<>(5, new Item(4089, 1)), // MYSTIC_HAT
			new WeightedChance<>(5, new Item(4109, 1)), // MYSTIC_HAT
			new WeightedChance<>(5, new Item(4101, 1)), // MYSTIC_TOP
			new WeightedChance<>(5, new Item(4091, 1)), // MYSTIC_TOP
			new WeightedChance<>(5, new Item(4111, 1)), // MYSTIC_TOP
			new WeightedChance<>(5, new Item(4103, 1)), // MYSTIC_BOTTOM
			new WeightedChance<>(5, new Item(4093, 1)), // MYSTIC_BOTTOM
			new WeightedChance<>(5, new Item(4113, 1)), // MYSTIC_BOTTOM
			new WeightedChance<>(4, new Item(995, 35000)), // COINS
			new WeightedChance<>(1.5, new Item(6199, 1)), // BRONZE_MYSTERY_BOX
			new WeightedChance<>(1.5, new Item(12955, 1)), // SILVER_MYSTERY_BOX
			new WeightedChance<>(1.5, new Item(2572, 1)), // ROW
			new WeightedChance<>(1.5, new Item(2677, 1)), // ROW
			new WeightedChance<>(1.5, new Item(2722, 1)), // ROW
			new WeightedChance<>(1.5, new Item(12073, 1)), // ROW
			new WeightedChance<>(2.5, new Item(12785, 1)), // SILVER_MYSTERY_BOX
			new WeightedChance<>(1, new Item(11840, 1)), // DRAGON_BOOTS
			new WeightedChance<>(1, new Item(1436, 1000)), // Rune Essence
			new WeightedChance<>(1, new Item(11812, 1)), // Bandos Hilt
			new WeightedChance<>(1, new Item(22114, 1)), // DRAGON_BOOTS
			new WeightedChance<>(1, new Item(2577, 1)), // DRAGON_BOOTS
			new WeightedChance<>(1, new Item(13116, 1)), // DRAGON_BOOTS
			new WeightedChance<>(2, new Item(21892, 1)), // DRAGON_BOOTS
			new WeightedChance<>(1, new Item(13116, 1)), // DRAGON_BOOTS
			new WeightedChance<>(2, new Item(533, 25)), // DRAGON_BOOTS
			new WeightedChance<>(2, new Item(200, 50)), // DRAGON_BOOTS
			new WeightedChance<>(2, new Item(202, 50)), // DRAGON_BOOTS
			new WeightedChance<>(2, new Item(204, 50)), // DRAGON_BOOTS
			new WeightedChance<>(2, new Item(205, 50)), // DRAGON_BOOTS
			new WeightedChance<>(2, new Item(208, 50)), // DRAGON_BOOTS
			new WeightedChance<>(0.5, new Item(6571, 1)) // UNCUT_ONYX
	));

}
