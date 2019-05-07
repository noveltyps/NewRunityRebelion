package io.server.content.achievement;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import io.server.game.world.entity.skill.Skill;

/**
 * Holds all the achievements
 *
 * @author Nerik#8690
 */
public enum AchievementList {
	/* Easy achievements */
	KILL_A_MAN(100, AchievementKey.KILL_A_MAN, AchievementDifficulty.EASY, "Kill 100 Men"),
	BURY_BONES(100, AchievementKey.BURY_BONES, AchievementDifficulty.EASY, "Bury 100S bones (any)"),
	BURN_AN_OAK_LOG(50, AchievementKey.BURN_AN_OAK_LOG, AchievementDifficulty.EASY, "Burn 50 Oak Log"),
	CATCH_A_SALMON(100, AchievementKey.CATCH_A_SALMON, AchievementDifficulty.EASY, "Catch 100 Salmon Fish"),
	TRIVIABOT(25, AchievementKey.TRIVIABOT, AchievementDifficulty.EASY, "Answer 25 TriviaBot questions"),
	HIGH_ALCHEMY(100, AchievementKey.HIGH_ALCHEMY, AchievementDifficulty.EASY, "Cast high alchemy spell 100 times"),
	SKILL_MASTERY(3, AchievementKey.SKILL_MASTERY, AchievementDifficulty.EASY, "Achieve level 99 in 3 skill"),
	KILLER(10, AchievementKey.KILLER, AchievementDifficulty.EASY, "Kill 10 players"),
	CUT100TREES1(500, AchievementKey.CUT100TREES, AchievementDifficulty.EASY, "Chop down 500 trees"),
	KILL_KRAKEN_I(25, AchievementKey.KILL_KRAKEN, AchievementDifficulty.EASY, "Kill Kraken 25 Times"),
	STEAL_FROM_STALL(1, AchievementKey.STEAL_FROM_STALL, AchievementDifficulty.EASY, "Steal from a stall"),

	/* Medium achievements */
	CUT100TREES(500, AchievementKey.CUT100TREES, AchievementDifficulty.MEDIUM, "Cut 500 Trees (any)"),
	BURN100ANY(500, AchievementKey.BURN100ANY, AchievementDifficulty.MEDIUM, "Burn 500 logs (any)"),
	MINE_100_ORE(500, AchievementKey.MINE_100_ORE, AchievementDifficulty.MEDIUM, "Mine 500 ore (any)"),
	// FLETCH250ADAMANET(500, AchievementKey.FLETCH250ADAMANET,
	// AchievementDifficulty.MEDIUM, "Fletch 500 Adamanet Cbows"),
	SKILL_MASTERY_I(6, AchievementKey.SKILL_MASTERY, AchievementDifficulty.MEDIUM, "Achieve level 99 in 6 skills"),
	TRIVIABOT_I(100, AchievementKey.TRIVIABOT, AchievementDifficulty.MEDIUM, "Answer 100 TriviaBot questions"),
	KILLER_I(50, AchievementKey.KILLER, AchievementDifficulty.MEDIUM, "Kill 50 players"),
	WOODCUTTING_I(1000, AchievementKey.CUT100TREES, AchievementDifficulty.MEDIUM, "Chop down 1000 trees"),
	CRYSTAL_CHEST_I(50, AchievementKey.CRYSTAL_CHEST, AchievementDifficulty.MEDIUM, "Open 50 crystal chests"),
	KILL_GLOD(100, AchievementKey.KILL_GLOD, AchievementDifficulty.MEDIUM, "Kill the glod 25 Times"),
	CRAFT_BLOODRUNE(500, AchievementKey.CRAFT_BLOODRUNE, AchievementDifficulty.MEDIUM, "Craft 500 Blood runes"),
	HIGH_ALCHEMY_I(500, AchievementKey.HIGH_ALCHEMY, AchievementDifficulty.MEDIUM, "Cast high alchemy spell 500 times"),
	STEAL_FROM_STALL1(250, AchievementKey.STEAL_FROM_STALL, AchievementDifficulty.MEDIUM, "Steal from 250 stall"),

	/* Hard achievements */
	SKILL_MASTERY_II(Skill.SKILL_COUNT, AchievementKey.SKILL_MASTERY, AchievementDifficulty.HARD,
			"Achieve level 99 in all skills"),
	EXPERIENCE_MASTERY(5, AchievementKey.EXPERIENCE_MASTERY, AchievementDifficulty.HARD, "Earn 200M EXP in 5 skills"),
	TRIVIABOT_II(100, AchievementKey.TRIVIABOT_II, AchievementDifficulty.HARD, "Answer 100 TriviaBot questions"),
	KILLER_II(150, AchievementKey.KILLER, AchievementDifficulty.HARD, "Kill 150 players"),
	WOODCUTTING_II(1500, AchievementKey.WOODCUTTING, AchievementDifficulty.HARD, "Chop down 1,500 trees"),
	CRYSTAL_CHEST_II(150, AchievementKey.CRYSTAL_CHEST, AchievementDifficulty.HARD, "Open 150 crystal chests"),
	COMPLETE_RFD_MINIQUEST(1, AchievementKey.COMPLETE_RFD, AchievementDifficulty.HARD, "Complete RFD Miniquest"),
	KILL_SKOTIZO(250, AchievementKey.KILL_SKOTIZO, AchievementDifficulty.HARD, "Kill the Skotizo 100 Times"),

	/* Elite achievements */
	BOSSPOINT(5000, AchievementKey.BOSSPOINT, AchievementDifficulty.ELITE, "Gain 5000 Boss Points"),
	EXPERIENCE_MASTERY_II(Skill.SKILL_COUNT, AchievementKey.EXPERIENCE_MASTERY, AchievementDifficulty.ELITE,
			"Earn 200M EXP in all skills"),
	TRIVIABOT_III(1000, AchievementKey.TRIVIABOT, AchievementDifficulty.ELITE, "Answer 1000 TriviaBot questions"),
	KILLER_III(1000, AchievementKey.KILLER, AchievementDifficulty.ELITE, "Kill 1,000 players"),
	KILLER_IV(2500, AchievementKey.KILLER, AchievementDifficulty.ELITE, "Kill 2500 players"),
	WOODCUTTING_III(5000, AchievementKey.CUT100TREES, AchievementDifficulty.ELITE, "Chop down 5,000 trees"),
	KILL_GANO(1000, AchievementKey.KILL_GALVEK, AchievementDifficulty.ELITE, "Kill Galvek 1000 Times"),
	CUT100TREES2(5000, AchievementKey.CUT100TREES, AchievementDifficulty.ELITE, "Cut 5000 Trees (any)"),
	STEAL_FROM_STALL2(5000, AchievementKey.STEAL_FROM_STALL, AchievementDifficulty.ELITE, "Steal from 5000 stall"),
	HIGH_ALCHEMY_II(5000, AchievementKey.HIGH_ALCHEMY, AchievementDifficulty.ELITE,
			"Cast high alchemy spell 5000 times"),

	;
	/** Caches our enum values. */
	private static final ImmutableSet<AchievementList> VALUES = ImmutableSet.copyOf(values());

	/** The amount required to complete the achievement. */
	private final int amount;

	/** The key of this achievement */
	private final AchievementKey key;

	/** The achievement difficulty. */
	private final AchievementDifficulty difficulty;

	/* The achievement task string. */
	private final String task;

	/** Constructs a new <code>AchievementList<code>. */
	AchievementList(int amount, AchievementKey key, AchievementDifficulty difficulty, String task) {
		this.amount = amount;
		this.key = key;
		this.difficulty = difficulty;
		this.task = task;
	}

	/** Gets the amount required to complete the achievement. */
	public int getAmount() {
		return amount;
	}

	/** Gets the achievement key. */
	public AchievementKey getKey() {
		return key;
	}

	/** Gets the achievement difficulty */
	public AchievementDifficulty getDifficulty() {
		return difficulty;
	}

	/** Gets the achievement task. */
	public String getTask() {
		return task;
	}

	/** Gets the achievements as a list. */
	public static List<AchievementList> asList(AchievementDifficulty difficulty) {
		return VALUES.stream().filter(a -> a.getDifficulty() == difficulty).sorted(Comparator.comparing(Enum::name))
				.collect(Collectors.toList());
	}

	/** Gets the total amount of achievements. */
	public static int getTotal() {
		return values().length;
	}
}