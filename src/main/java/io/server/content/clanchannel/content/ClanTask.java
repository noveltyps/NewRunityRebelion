package io.server.content.clanchannel.content;

import static io.server.content.clanchannel.content.ClanTaskKey.ABYSSAL_DEMON;
import static io.server.content.clanchannel.content.ClanTaskKey.AFK_LOG;
import static io.server.content.clanchannel.content.ClanTaskKey.AGILITY_COURSE;
import static io.server.content.clanchannel.content.ClanTaskKey.BLACK_DEMON;
import static io.server.content.clanchannel.content.ClanTaskKey.BLACK_DRAGON;
import static io.server.content.clanchannel.content.ClanTaskKey.BLOOD_RUNE;
import static io.server.content.clanchannel.content.ClanTaskKey.BLUE_DRAGON;
import static io.server.content.clanchannel.content.ClanTaskKey.BOT_KILLING;
import static io.server.content.clanchannel.content.ClanTaskKey.BURN_WILLOW_LOG;
import static io.server.content.clanchannel.content.ClanTaskKey.CALLISTO;
import static io.server.content.clanchannel.content.ClanTaskKey.CHAOS_ELEMENTAL;
import static io.server.content.clanchannel.content.ClanTaskKey.CHAOS_FANATIC;
import static io.server.content.clanchannel.content.ClanTaskKey.CHOP_ANY_LOG;
import static io.server.content.clanchannel.content.ClanTaskKey.CHOP_WILLOW_LOG;
import static io.server.content.clanchannel.content.ClanTaskKey.CRAZY_ARCHAEOLOGIST;
import static io.server.content.clanchannel.content.ClanTaskKey.DARK_CRAB;
import static io.server.content.clanchannel.content.ClanTaskKey.DEATH_RUNE;
import static io.server.content.clanchannel.content.ClanTaskKey.GREATER_DEMON;
import static io.server.content.clanchannel.content.ClanTaskKey.GREEN_DRAGON;
import static io.server.content.clanchannel.content.ClanTaskKey.HILL_GIANT;
import static io.server.content.clanchannel.content.ClanTaskKey.KING_BLACK_DRAGON;
import static io.server.content.clanchannel.content.ClanTaskKey.MAGIC_LOG;
import static io.server.content.clanchannel.content.ClanTaskKey.MAGIC_SHORTBOW;
import static io.server.content.clanchannel.content.ClanTaskKey.PLAYER_KILLING;
import static io.server.content.clanchannel.content.ClanTaskKey.RED_DRAGON;
import static io.server.content.clanchannel.content.ClanTaskKey.ROCK_CRAB;
import static io.server.content.clanchannel.content.ClanTaskKey.RUNITE_ORES;
import static io.server.content.clanchannel.content.ClanTaskKey.SAND_CRAB;
import static io.server.content.clanchannel.content.ClanTaskKey.SCORPIA;
import static io.server.content.clanchannel.content.ClanTaskKey.SHARK;
import static io.server.content.clanchannel.content.ClanTaskKey.SKELETAL_WYVERN;
import static io.server.content.clanchannel.content.ClanTaskKey.SUPER_RESTORE_POTION;
import static io.server.content.clanchannel.content.ClanTaskKey.THIEVING_STALL;
import static io.server.content.clanchannel.content.ClanTaskKey.TRIVIA_ANSWER_KEY;
import static io.server.content.clanchannel.content.ClanTaskKey.VENNANTIS;
import static io.server.content.clanchannel.content.ClanTaskKey.YEW_LOG;
import static io.server.content.clanchannel.content.ClanTaskKey.YEW_SHORTBOW;

import java.util.HashSet;
import java.util.Set;

import io.server.content.clanchannel.ClanType;
import io.server.content.clanchannel.channel.ClanChannel;
import io.server.util.Difficulty;
import io.server.util.InclusiveRandom;
import io.server.util.Utility;

/**
 * Holds all the clan task data.
 *
 * @author Daniel
 */
public enum ClanTask {
	/** PvP */
	/* -> Easy <- */
	KILL_PLAYERS_I(ClanType.PVP, Difficulty.EASY, "Kill % players", PLAYER_KILLING, new InclusiveRandom(10, 20)),
	KILL_BOT_I(ClanType.PVP, Difficulty.EASY, "Kill % bots", BOT_KILLING, new InclusiveRandom(10, 25)),

	/* -> Medium <- */
	KILL_PLAYERS_II(ClanType.PVP, Difficulty.MEDIUM, "Kill % players", PLAYER_KILLING, new InclusiveRandom(25, 35)),
	KILL_BOT_II(ClanType.PVP, Difficulty.MEDIUM, "Kill % bots", BOT_KILLING, new InclusiveRandom(25, 50)),

	/* -> Hard <- */
	KILL_PLAYERS_III(ClanType.PVP, Difficulty.HARD, "Kill % players", PLAYER_KILLING, new InclusiveRandom(50, 125)),
	KILL_BOT_III(ClanType.PVP, Difficulty.HARD, "Kill % bots", BOT_KILLING, new InclusiveRandom(50, 125)),

	/** PvM */
	/* -> Easy <- */
	KILL_HILL_GIANTS(ClanType.PVM, Difficulty.EASY, "Kill % hill giants", HILL_GIANT, new InclusiveRandom(100, 200)),
	KILL_ROCK_CRABS(ClanType.PVM, Difficulty.EASY, "Kill % rock crabs", ROCK_CRAB, new InclusiveRandom(100, 200)),
	KILL_SAND_CRABS(ClanType.PVM, Difficulty.EASY, "Kill % sand crabs", SAND_CRAB, new InclusiveRandom(100, 200)),
	KILL_GREEN_DRAGONS_I(ClanType.PVM, Difficulty.MEDIUM, "Kill % green dragons", GREEN_DRAGON,
			new InclusiveRandom(50, 150)),

	/* -> Easy <- */
	/* -> Easy <- */
	ANY_TREE(ClanType.IRON_MAN, Difficulty.EASY, "Chop % Logs (Any Tree!)", CHOP_ANY_LOG,
			new InclusiveRandom(150, 350)),
	/*
	 * COMPLETE_MASTERMINER_TASK(ClanType.SOCIAL, Difficulty.EASY,
	 * "Complete %s MasterMiner Tasks!", COMPLETE_MASTERMINER_TASK_KEY, new
	 * InclusiveRandom(2, 6)),
	 */
	/* -> Hard <- */
	TRVIA_TASK(ClanType.SOCIAL, Difficulty.HARD, "Answer % Trivia Questions", TRIVIA_ANSWER_KEY,
			new InclusiveRandom(45, 70)),

	/* -> Medium <- */
	KILL_BLACK_DEMONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % black demons", BLACK_DEMON,
			new InclusiveRandom(150, 250)),
	KILL_GREATER_DEMONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % greater demons", GREATER_DEMON,
			new InclusiveRandom(150, 250)),
	KILL_BLUE_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % blue dragons", BLUE_DRAGON,
			new InclusiveRandom(151, 250)),
	KILL_BLACK_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % black dragons", BLACK_DRAGON,
			new InclusiveRandom(151, 250)),
	KILL_RED_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % red dragons", RED_DRAGON, new InclusiveRandom(151, 250)),
	KILL_GREEN_DRAGONS_II(ClanType.PVM, Difficulty.MEDIUM, "Kill % green dragons", GREEN_DRAGON,
			new InclusiveRandom(151, 250)),
	AFK_TREE(ClanType.SOCIAL, Difficulty.MEDIUM, "Harvest % AFK Logs", AFK_LOG, new InclusiveRandom(800, 1200)),

	/* -> Hard <- */
	KILL_ABYSSAL_DEMONS(ClanType.PVM, Difficulty.HARD, "Kill % abyssal demons", ABYSSAL_DEMON,
			new InclusiveRandom(250, 500)),
	KILL_SKELETAL_WYVERNS(ClanType.PVM, Difficulty.HARD, "Kill % skeletal wyverns", SKELETAL_WYVERN,
			new InclusiveRandom(250, 500)),
	KILL_KING_BLACK_DRAGONS(ClanType.PVM, Difficulty.HARD, "Kill % king black dragons", KING_BLACK_DRAGON,
			new InclusiveRandom(50, 200)),
	KILL_SCORPIA(ClanType.PVM, Difficulty.HARD, "Kill % scorpias", SCORPIA, new InclusiveRandom(25, 75)),
	KILL_VENENATIS(ClanType.PVM, Difficulty.HARD, "Kill % venenatis", VENNANTIS, new InclusiveRandom(25, 75)),
	KILL_CALLISTO(ClanType.PVM, Difficulty.HARD, "Kill % callistos", CALLISTO, new InclusiveRandom(25, 75)),
	KILL_CRAZY_ARCHAEOLOGIST(ClanType.PVM, Difficulty.HARD, "Kill % crazy archaeologists", CRAZY_ARCHAEOLOGIST,
			new InclusiveRandom(50, 200)),
	KILL_CHAOS_FANATIC(ClanType.PVM, Difficulty.HARD, "Kill % chaos fanatics", CHAOS_FANATIC,
			new InclusiveRandom(25, 75)),
	KILL_CHAOS_ELEMENTAL(ClanType.PVM, Difficulty.HARD, "Kill % chaos elementals", CHAOS_ELEMENTAL,
			new InclusiveRandom(25, 75)),

	/** Skilling */
	/* -> Easy <- */
	THIEVE_FROM_STALL_I(ClanType.SKILLING, Difficulty.EASY, "Thieve from stall % times", THIEVING_STALL,
			new InclusiveRandom(300, 500)),
	CHOP_WILLOW(ClanType.SKILLING, Difficulty.EASY, "Chop % willow logs", CHOP_WILLOW_LOG,
			new InclusiveRandom(300, 500)),
	COMPLETE_AGILITY_LAP_I(ClanType.SKILLING, Difficulty.EASY, "Complete % non-rooftop course", AGILITY_COURSE,
			new InclusiveRandom(300, 500)),
	BURN_YEW_LOG(ClanType.SKILLING, Difficulty.EASY, "Burn % willow logs", BURN_WILLOW_LOG,
			new InclusiveRandom(300, 500)),

	/* -> Medium <- */
	CATCH_SHARK(ClanType.SKILLING, Difficulty.MEDIUM, "Catch % sharks", SHARK, new InclusiveRandom(500, 750)),
	CHOP_YEW(ClanType.SKILLING, Difficulty.MEDIUM, "Chop % yew logs", YEW_LOG, new InclusiveRandom(500, 750)),
	FLETCH_YEW_SHORTBOW(ClanType.SKILLING, Difficulty.MEDIUM, "Fletch % yew shortbow", YEW_SHORTBOW,
			new InclusiveRandom(500, 750)),
	CRAFT_DEATH_RUNE(ClanType.SKILLING, Difficulty.MEDIUM, "Craft % death runes", DEATH_RUNE,
			new InclusiveRandom(500, 750)),
	CREATE_SUPER_RESTORE_POTION(ClanType.SKILLING, Difficulty.MEDIUM, "Create % super restore potions",
			SUPER_RESTORE_POTION, new InclusiveRandom(500, 750)),
	COMPLETE_AGILITY_LAP_II(ClanType.SKILLING, Difficulty.MEDIUM, "Complete % non-rooftop course", AGILITY_COURSE,
			new InclusiveRandom(500, 750)),
	THIEVE_FROM_STALL_II(ClanType.SKILLING, Difficulty.MEDIUM, "Thieve from stall % times", THIEVING_STALL,
			new InclusiveRandom(500, 750)),

	/* -> Hard <- */
	// SMITH_RUNITE_BAR(ClanType.SKILLING, Difficulty.HARD, "Smith % runite bars",
	// RUNITE_BAR, new InclusiveRandom(600, 900)),
	FLETCH_MAGIC_SHORTBOW(ClanType.SKILLING, Difficulty.HARD, "Fletch % magic shortbow", MAGIC_SHORTBOW,
			new InclusiveRandom(1250, 2000)),
	MINE_RUNITE_ORE(ClanType.SKILLING, Difficulty.HARD, "Mine % runite ores", RUNITE_ORES,
			new InclusiveRandom(650, 900)),
//    CREATE_SUPER_COMBAT_POTION(ClanType.SKILLING, Difficulty.HARD, "Create % super combat potions", SUPER_COMBAT_POTION, new InclusiveRandom(, 2000)),
	COMPLETE_AGILITY_LAP_III(ClanType.SKILLING, Difficulty.HARD, "Complete % non-rooftop course", AGILITY_COURSE,
			new InclusiveRandom(750, 1000)),
	CATCH_ANGLERFISH(ClanType.SKILLING, Difficulty.HARD, "Catch % dark crabs", DARK_CRAB,
			new InclusiveRandom(750, 1000)),
	CRAFT_BLOOD_RUNE(ClanType.SKILLING, Difficulty.HARD, "Craft % blood runes", BLOOD_RUNE,
			new InclusiveRandom(750, 1000)),
	CHOP_MAGIC(ClanType.SKILLING, Difficulty.HARD, "Chop % magic logs", MAGIC_LOG, new InclusiveRandom(600, 800));

	public final String task;
	public final ClanTaskKey key;
	public final InclusiveRandom amount;
	public final Difficulty difficulty;
	public final ClanType type;

	ClanTask(ClanType type, Difficulty difficulty, String task, ClanTaskKey key, InclusiveRandom amount) {
		this.task = task;
		this.key = key;
		this.amount = amount;
		this.difficulty = difficulty;
		this.type = type;
	}

	public double getProgressExperience() {
		switch (type) {
		case PVP:
			return 1250;
		case PVM:
			return difficulty == Difficulty.HARD ? 350 : 50;
		case SKILLING:
			return 5;
		default:
			break;
		}
		return 0;
	}

	public String getName(ClanChannel channel) {
		return task.replace("%", "" + channel.getDetails().taskAmount);
	}

	public int getAmount() {
		return Utility.random(amount.minimum, amount.maximum);
	}

	public static Set<ClanTask> getTasks(ClanType type, Difficulty difficulty) {
		Set<ClanTask> tasks = new HashSet<>();
		for (ClanTask task : values()) {
			if (task.type == type && task.difficulty == difficulty)
				tasks.add(task);
		}
		return tasks;
	}

	public static ClanTask getAssignment(ClanType type, Difficulty difficulty) {
		Set<ClanTask> tasks = getTasks(type, difficulty);
		return Utility.randomElement(tasks);
	}

	public int getReward() {
		return (difficulty == Difficulty.EASY) ? 2 : (difficulty == Difficulty.MEDIUM) ? 3 : 5;
	}
}
