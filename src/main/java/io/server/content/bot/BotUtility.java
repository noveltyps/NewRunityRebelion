package io.server.content.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.server.content.tittle.PlayerTitle;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.appearance.Appearance;
import io.server.game.world.entity.mob.player.appearance.Gender;
import io.server.game.world.items.Item;
import io.server.util.MutableNumber;
import io.server.util.Utility;

/**
 * Holds all the constants used by bot.
 *
 * @author Daniel
 */
public class BotUtility {

	public static Map<Integer, MutableNumber> BOOT_LOOT = new HashMap<>();

	public static void logLoot(Item item) {
		MutableNumber amount = BOOT_LOOT.getOrDefault(item.getId(), new MutableNumber());
		amount.incrementAndGet(item.getAmount());
		BOOT_LOOT.put(item.getId(), amount);
	}

	/** List of all available bot names. */
	private static final String[] BOT_NAMES = { "vapster011", "Bangbros101", "Babylover234", "pillekesbv", "Beta124", "Menzo657",
			"Randykul", "ThisIsGod", "JesusTakeTheWheel", "BIOS",};

	/** The default bot title. */
	static final PlayerTitle TITLE = PlayerTitle.create("[BOT]", 0xC74C1C);

	/** The default bot appearance. */
	public static final Appearance APPEARANCE = new Appearance(Gender.MALE, Utility.random(0, 8),
			Utility.random(10, 17), Utility.random(18, 25), Utility.random(26, 31), Utility.random(33, 34),
			Utility.random(36, 40), Utility.random(42, 43), 7, 8, 9, 5, 0);

	/** Array of all the possible fight start message. */
	public static final String[] GEAR_UP_MESSAGES = { "It´s time to take a soul from someone on my list",
			"I am one with the demon of death, it´s time to kill all of you!", "Time to make my daddy, Parano1a proud!" };

	/** Array of all the possible fight start message. */
	public static final String[] FIGHT_START_MESSAGES = { "leggo", "gl", "gl nub" };

	/** Array of all the possible fight end message. */
	public static final String[] FIGHT_END_MESSAGES = { "gf", "out", "ye...shet" };

	/** Array of all the possible fight start message. */
	public static final String[] KILLED_MESSAGES = { "cya", "gf", "sit", "shitted on", "Cya", "gg", "bye" };

	/** Array of all the possible death message. */
	static final String[] DEATH_MESSAGES = { "fgs", "Gf", "Gg", "damn son", "bruh" };

	/** Generates a random bot named based on the available names. */
	static String nameGenerator() {
		return Utility.randomElement(getAvailableBotNames());
	}

	/** Generates a list of all available names. */
	private static List<String> getAvailableBotNames() {
		List<String> names = new ArrayList<>(BOT_NAMES.length);
		names.addAll(Arrays.asList(BOT_NAMES));
		for (Player bot : World.getPlayers()) {
			if (!bot.isBot) {
				continue;
			}
			for (String nameList : BOT_NAMES) {
				if (bot.getName().equalsIgnoreCase(nameList)) {
					names.remove(nameList);
				}
			}
		}
		return names;
	}

	/** Generates a random bot type. */
//    static BotClass classGenerator() {
//        return Utility.randomElement(BotClass.values());
//    }
}