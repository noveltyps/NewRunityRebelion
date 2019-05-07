package io.server;

import java.io.File;
import java.math.BigInteger;

import com.google.common.collect.ImmutableList;
import com.moandjiezana.toml.Toml;

import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import io.server.game.world.entity.mob.player.appearance.Appearance;
import io.server.game.world.entity.mob.player.appearance.Gender;
import io.server.game.world.items.Item;
import io.server.game.world.position.Position;
import io.server.net.session.Session;

/**
 * The class that contains setting-related constants for the server.
 *
 * @author Daniel
 */

public final class Config {
	
	public static boolean DISCORD_MASS_MESSAGE = false;
	
	public static boolean DOUBLE_EXPERIENCE;
	
	public static boolean DOUBLE_DROPS;
	
	public static boolean TRIPLE_VOTE = true;
	
	public static boolean DR_15_BOOST;
	
	public static boolean DR_30_BOOST;
	
	public static boolean DOUBLE_AVO_POINTS;
	
	public static boolean DOUBLE_PK_POINTS;

	public static boolean X4_EXPERIENCE;

	/** The latest announcement thread link. */
	public static final String LATEST_ANNOUNCEMENT_THREAD = "https://RebelionX.org";

	/** The latest update thread link. */
	public static final String LATEST_UPDATE_THREAD = "https://RebelionX.org";

	/** The welcome marquee. */
	public static final String[] WELCOME_MARQUEE = { "There are currently #players players online!",
			"Make sure to vote daily for great rewards!", "Donating really helps us with keeping the server alive" };

	/** The welcome dialogue. */
	public static final String[] WELCOME_DIALOGUE = { "Update threads are released every friday",
			"The Owners are Parano1a and Dani", "The Developers are Parano1a and Dani" };

	/** The welcome announcement. */
	public static final String[] WELCOME_ANNOUNCEMENT = { "New Minigame!", "[ Nov 5th 2018 ]",
			"Hello RebelionX community. On", "05/11/18, we will be pushing", "more bug fixes" };

	public static final String[] WELCOME_UPDATE = { "Game Updates", "[ Nov 5th 2018 ]", "QoL Updates! & Double Threat,",
			"Numerous bug fixes", "& much more!" };

	/** The amount of client packets that can be handled by the server each tick. */
	public static final int CLIENT_PACKET_THRESHOLD;

	/** Displays packet information in the output stream. */
	public static boolean DISPLAY_PACKETS = Config.SERVER_DEBUG;

	/**
	 * The maximum amount of connections that can be active at a time, or in other
	 * words how many clients can be logged in at once per connection.
	 */
	public static final int CONNECTION_LIMIT;

	/** The number of seconds before a connection becomes idle. */
	public static final int IDLE_TIMEOUT;

	/**
	 * The resource leak detection level, should be {@code PARANOID} in a
	 * development environment and {@code DISABLED} in a production environment.
	 */
	public static final ResourceLeakDetector.Level RESOURCE_LEAK_DETECTION;

	/** The list of exceptions that are ignored and discarded by the */
	public static final ImmutableList<String> IGNORED_EXCEPTIONS = ImmutableList.of(
			"An existing connection was forcibly closed by the remote host",
			"An established connection was aborted by the software in your host machine");

	/** The session key. */
	public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session.key");

	/** The RSA modulus. */
	public static final BigInteger RSA_MODULUS;

	/** The RSA exponent. */
	public static final BigInteger RSA_EXPONENT;

	public static final String GLOBAL_DATABASE = "runity_global";

	public static final boolean LIVE_SERVER;

	public static final boolean LOG_PLAYER;

	/** This will use the parallel game game. */
	public static final boolean PARALLEL_GAME_ENGINE;

	/** The name of the server. */
	public static final String SERVER_NAME;

	/** This is used to connect to the VPS mongo server */
	public static final String VPS_IP;

	/**
	 * If forum integration is true, users can only login if they enter the same
	 * username and password that's on the forums also mongo must be running when
	 * this is true. Since all the players will be stored in the mongo database.
	 **/
	public static final boolean FORUM_INTEGRATION;

	/**
	 * This is used for mySQL to authenticate username and passwords from the forum.
	 */
	public static final String FORUM_DB_URL;

	/**
	 * This is used for mySQL to authenticate username and passwords from the forum.
	 */
	public static final String FORUM_DB_USER;

	/**
	 * This is used for mySQL to authenticate username and passwords from the forum.
	 */
	public static final String FORUM_DB_PASS;

	public static final String POSTGRE_URL;
	public static final String POSTGRE_USER;
	public static final String POSTGRE_PASS;

	public static final int FAILED_LOGIN_ATTEMPTS;
	public static final int FAILED_LOGIN_TIMEOUT;

	public static final int CACHE_VERSION;
	public static final int GAME_VERSION;

	public static boolean highscoresEnabled = false;
	
	/** The OS Royale community discord token. */
	public static final String DISCORD_TOKEN;

	/**
	 * The development state flag. (Always make sure you run the official server
	 * with this disabled!)
	 */
	public static boolean SERVER_DEBUG = false;

	/**
	 * Displays the time in milliseconds each tick the game takes to run through a
	 * game cycle.
	 */
	public static final boolean SERVER_CYCLE_DEBUG;

	/** The server port. */
	public static final int SERVER_PORT;

	/** The walking radius for Npc. */
	public static final int NPC_WALKING_RADIUS;

	/** The amount of players that can be logged in, in a single cycle **/
	public static final int LOGIN_THESHOLD;

	/** The amount of players that can logout in a single cycle **/
	public static final int LOGOUT_THESHOLD;

	/** The maximum amount of players that can be held within the game world. */
	public static final int MAX_PLAYERS;

	/** The maximum amount of npcs that can be held within the game world. */
	public static final int MAX_NPCS;

	/** The amount of bots that will spawn into the game world. */
	public static final int MAX_BOTS;

	/** The currency identification of the server. */
	public static final int CURRENCY = 995;

	/** The currency identification of the server. */
	public static final int BILL_CURRENCY = 5021;

	/** The limit of the npc identification. */
	public static final int NPC_DEFINITION_LIMIT;

	public static final boolean TEST_WORLD;

	/** The limit of the item identification. */
	public static final int ITEM_DEFINITION_LIMIT;

	/** The npc bits for the server which can handle 6755 npcs. */
	public static final int NPC_BITS;

	/**
	 * The time in ticks a player remains skulled for.
	 *
	 * 720 ticks ~20 minutes
	 */
	public static final int SKULL_TIME;

	/** All the tab identifications */
	public static final int ATTACK_TAB = 0, SKILL_TAB = 1, QUEST_TAB = 2, INVENTORY_TAB = 3, EQUIPMENT_TAB = 4,
			PRAYER_TAB = 5, MAGIC_TAB = 6, ACTIVITY_TAB = 7, FRIENDS_TAB = 8, IGNORE_TAB = 9, CLAN_TAB = 10,
			WRENCH_TAB = 11, EMOTE_TAB = 12, MUSIC_TAB = 13, LOGOUT_TAB = 14;

	/** The default, i.e. onSpawn, location. */
	public static final Position DEFAULT_POSITION = new Position(3086, 3501, 0);

	public static final Position RAIDS = new Position(1233, 3566, 0);
	public static final Position RAIDS2 = new Position(3670, 3220, 0);

	/** **/
	public static final Position STORES_POSITION = new Position(3164, 3470, 0);

	public static final Position LUMBRIDGE = new Position(3222, 3218, 0);

	/** The donator zone. */
	public static final Position DONATOR_ZONE = new Position(1811, 3545, 0);

	/** Tier 1 Zone. */
	public static final Position TIER_1_ZONE = new Position(3353, 3409, 0);

	/** Tier 2 Zone. */
	public static final Position TIER_2_ZONE = new Position(2835, 3270, 0);

	/** Tier 3 Zone. */
	public static final Position TIER_3_ZONE = new Position(2656, 4838, 0);

	/** Tier 4 Zone. */
	public static final Position TIER_4_ZONE = new Position(2584, 4840, 0);

	/** The DKS zone. */
	public static final Position DKS = new Position(1913, 4367, 0);

	/** ARENA ZONE. */
	public static final Position ARENA_ZONE = new Position(2264, 5341, 0);
	
	
	public static final Position PORAZDIR_ZONE = new Position(2981, 3822, 0);


	/** The donator zone. */
	public static final Position PORTAL_ZONE = new Position(3363, 3318, 0);
	
	public static final Position EXTREME_ZONE = new Position(3329, 4797, 0);
	
	public static final Position EZ_ZONE = new Position(2905, 2727, 0);


	public static final Position AIR_ZONE = new Position(2841, 4829, 0);

	public static final Position BLOOD_ZONE = new Position(1732, 3827, 0);

	public static final Position TARN_ZONE = new Position(3037, 5346, 0);

	
	public static final Position NATURE_ZONE = new Position(2400, 4835, 0);

	public static final Position LAW_ALTAR = new Position(2464, 4818, 0);
	
	public static final Position STARTER_ZONE = new Position(2462, 4769, 0);
	
	public static final Position CHIMERA_ZONE = new Position(2410, 4678, 0);
	
	public static final Position ALIEN_ZONE = new Position(2851, 9637, 0);

	public static final Position MEDIUM_ZONE = new Position(1886, 5454, 0);
	
	public static final Position ADVANCED_ZONE = new Position(2337, 9805, 0);
	public static final Position DUOZOMBIE_ZONE = new Position(3075, 3502, 0);
	public static final Position HUNTER_ZONE = new Position(2524, 2914, 0);


	/** Wests. */
	public static final Position WESTS = new Position(2980, 3594, 0);

	/** Easts. */
	public static final Position EASTS = new Position(3333, 3666, 0);

	/** Easts. */
	public static final Position BARROWS_CHEST = new Position(3551, 9691, 0);
	
	public static final Position AFK_FISHING = new Position(3094, 3468, 0);

	public static final Position AFK_FIREMAKING = new Position(3093, 3464, 0);

	public static final Position AFK_WOODCUTTING = new Position(3095, 3466, 0);

	/** KARAMJA. */
	public static final Position KARAMJA = new Position(2918, 3176, 0);

	public static final Position REV_CAVES = new Position(3126, 3784, 0);

	public static final Position PORAZDIR = new Position(3230, 10198, 0);

	
	/** KARAMJA. 3126, 3784*/
	public static final Position DRAYNOR = new Position(3105, 3251, 0);

	public static final Position AL_KHARID = new Position(3293, 3163, 0);
	/** DUSTIES. */
	public static final Position DUSTIES = new Position(3208, 9379, 0);

	/** **/
	public static final Position CHIMERA = new Position(2310, 5240, 0);

	/** Mb. */
	public static final Position MB = new Position(2540, 4717, 0);

	/** Multi. */
	public static final Position GDZ = new Position(3288, 3886, 0);

	/** The Train zone. */
	public static final Position TRAIN_ZONE = new Position(2525, 4776, 0);

	/** The Gamble zone. */
	public static final Position DICE_ZONE = new Position(3317, 3236, 0);

	/** The KBD. */
	public static final Position KBD = new Position(2997, 3849, 0);
	/** BOTSPAWN (DO NOT TOUCH **/
	public static final Position BOTSPAWN = new Position(3086, 3489, 0);

	/** Edgeville PK. */
	public static final Position EDGEVILLE = new Position(3087, 3517, 0);

	/** Bandos. */
	public static final Position BANDOS = new Position(2860, 5355, 2);

	/** Saradomin. */
	public static final Position SARADOMIN = new Position(2911, 5265, 0);

	/** Armadyl. */
	public static final Position ARMADYL = new Position(2839, 5292, 2);

	/** Zamorak. */
	public static final Position ZAMORAK = new Position(2925, 5337, 2);

	/** Barrows. */
	public static final Position BARROWS = new Position(3565, 3315, 0);

	/** Duel Arena. */
	public static final Position DUEL = new Position(3365, 3265, 0);

	/** TAV. */
	public static final Position TAV = new Position(2884, 9800, 0);

	/** The staff zone. */
	public static final Position STAFF_ZONE = new Position(2602, 3874, 0);

	/** The Jail zone. */
	public static final Position JAIL_ZONE = new Position(2713, 2564, 0);

	/** The Jail zone. */
	public static final Position SKILL_ZONE = new Position(3090, 3483, 0);
	
	public static final Position KURASK_ZONE = new Position(3061, 5485, 0);


	/** The default appearance of a player. */
	public static final Appearance DEFAULT_APPEARANCE = new Appearance(Gender.MALE, 0, 10, 18, 26, 33, 36, 42, 0, 0, 0,
			0, 0);

	/** Strings that are classified as bad. */
	public static final String[] BAD_STRINGS = { "fag", "f4g", "faggot", "nigger", "fuck", "bitch", "whore", "slut",
			"gay", "lesbian", "scape", ".net", ".org", "vagina", "dick", "cock", "penis", "hoe", "soulsplit", "ikov",
			"retard", "cunt", "g ay", "ga y", "g a y", "h o e", "o r g", "osroyale", "OS royale", "osr", "niger",
			"nardarh", "dreamscape", "imagineps", "vision317" };

	/** Messages that are sent periodically to all players. */
	public static final String[] MESSAGES = { "The Owners are both Parano1a and Dani.",
			"Add more security to your account by setting a bank pin.", "Tired of constantly re-gearing? Set a preset!",
			"Have any ideas on how we could improve our gameplay? Post on forums!.",
			"Voting daily can be very beneficial & it supports the server!",
			"Found a bug? Let a moderator know or post it on the forums!",
			"Did you know you can change your combat level by clicking on the skill?",
			"You can do ::commands for a list of commands!",
             "Do ::store to purchase bonds to make purchases",
             "Did you know you can do CTRL + DRAG to re-arrange prayer slots?",
             "Did you know we have duo Slayer!!",
             "The vote store has been updated! make sure to ::vote",
             "Did you know we have fully functional pest control!",
             "Did you know can play RebelionX in HD! Go to settings -> Advanced Settings!",
             "We have more settings you can change by going to extra settings",
         	"Donate OSRS By doing ::osrs in return for Bonds IN-Game!",
         	"Donate OSRS By doing ::osrs in return for Bonds IN-Game!",
         	"Donate OSRS By doing ::osrs in return for Bonds IN-Game!",
         	"Donate OSRS By doing ::osrs in return for Bonds IN-Game!",
             "Did you know you can access your bank vault on the go? simply do ::pouch",
             };

	/**
	 * Holds the array of all the side-bar identification and their corresponding
	 * itemcontainer identification.
	 */
	public static final int[][] SIDEBAR_INTERFACE = { { Config.ATTACK_TAB, 5855 }, { Config.SKILL_TAB, 3917 },
			{ Config.INVENTORY_TAB, 3213 }, { Config.QUEST_TAB, 29400 }, { Config.EQUIPMENT_TAB, 1644 },
			{ Config.PRAYER_TAB, 5608 }, { Config.CLAN_TAB, 33500 }, { Config.FRIENDS_TAB, 5065 },
			{ Config.IGNORE_TAB, 5715 }, { Config.WRENCH_TAB, 50020 }, { Config.EMOTE_TAB, 41000 },
			{ Config.MUSIC_TAB, 51200 }, { Config.ACTIVITY_TAB, -1 }, { Config.LOGOUT_TAB, 2449 } };
	public static final String WEBSITE_URL;

	/** The experience modification for combat. */
	public static final double COMBAT_MODIFICATION;

	/** The experience modification for agility. */
	public static final double AGILITY_MODIFICATION;

	/** The experience modification for cooking. */
	public static final double COOKING_MODIFICATION;

	/** The experience modification for crafting. */
	public static final double CRAFTING_MODIFICATION;

	/** The experience modification for firemaking. */
	public static final double FIREMAKING_MODIFICATION;

	/** The experience modification for fletching. */
	public static final double FLETCHING_MODIFICATION;

	/** The experience modification for herblore. */
	public static final double HERBLORE_MODIFICATION;

	/** The experience modification for hunter. */
	public static final double HUNTER_MODIFICATION;

	/** The experience modification for magic. */
	public static final double MAGIC_MODIFICATION;

	/** The experience modification for mining. */
	public static final double MINING_MODIFICATION;

	/** The experience modification for prayer. */
	public static final double PRAYER_MODIFICATION;

	/** The experience modification for runecrafting. */
	public static final double RUNECRAFTING_MODIFICATION;

	/** The experience modification for Slayer. */
	public static final double SLAYER_MODIFICATION;

	/** The experience modification for woodcutting. */
	public static final double WOODCUTTING_MODIFICATION;

	/** The experience modification for thieving. */
	public static final double THIEVING_MODIFICATION;

	/** The experience modification for smithing. */
	public static final double SMITHING_MODIFICATION;

	/** The experience modification for fishing. */
	public static final double FISHING_MODIFICATION;

	/** The experience modification for farming. */
	public static final double FARMING_MODIFICATION;

	/** TEMP VARS - WILL BE DELETED AFTER BETA IS FINISHED */
	public final static int[] TAB_AMOUNT = { 7, 46, 19, 42, 41, 15, 21, 4, 0, 0, };
	static int amount = 10000;
	
	public static int TOTAL_SERVER_VOTE = 0;

	public final static Item[] SPEARS = { new Item(4726), new Item(11824), new Item(20161), new Item(7809),
			new Item(11889)

	};

	public final static Item[] CUSTOM_ITEMS = { new Item(13738, 2), new Item(13739, 2), new Item(13740, 2), new Item(13741, 2),
			new Item(13742), new Item(13743), new Item(13744), new Item(13745), new Item(13746), new Item(13747),
			new Item(13748), new Item(13749), new Item(16628), new Item(16629), new Item(16630), new Item(16631),
			new Item(16647), new Item(16648), new Item(16649), new Item(16650), new Item(16651), new Item(16653),
			new Item(16654), new Item(16655), new Item(16656), new Item(19923), new Item(16657), new Item(16658),
			new Item(16659), new Item(16660), new Item(16661), new Item(16662), new Item(16663), new Item(16664),
			new Item(16665), new Item(16666), new Item(16667), new Item(16668), new Item(16669), new Item(13717),
			new Item(13718), new Item(13719), new Item(13720), new Item(13721), new Item(13722), new Item(13723),
			new Item(13724), new Item(13725), new Item(13726), new Item(13727), new Item(13728), new Item(13729),
			new Item(4062), new Item(17161), new Item(11642), new Item(17153), new Item(17154), new Item(17155),
			new Item(17156), new Item(17157), new Item(17158), new Item(17159), new Item(17160), new Item(17162),
			new Item(17163), new Item(17164), new Item(17165), new Item(17166), new Item(17167), new Item(17168),
			new Item(17172), new Item(17173), new Item(17174), new Item(17175), new Item(17176), new Item(17177),
			new Item(17178), new Item(17179), new Item(17180), new Item(17181), new Item(19914), new Item(19917),
			new Item(19920), new Item(13689), new Item(20251), new Item(20252), new Item(13690), new Item(13686),
			new Item(13687), new Item(11609), new Item(20690), new Item(14581), new Item(14582), new Item(14583),
			new Item(14584), new Item(14585), new Item(14586), new Item(14589), new Item(13207), new Item(13209),
			new Item(13210), new Item(13212), new Item(13213), new Item(13214), new Item(13661), new Item(13662),
			new Item(13189), new Item(10028), new Item(21777), new Item(13208), new Item(22123), new Item(21954),
			new Item(22099), new Item(22078), new Item(22301), new Item(22304), new Item(22307), new Item(15011),
			new Item(3273), new Item(3274), new Item(3275), new Item(15300), new Item(15307), new Item(10860),
			new Item(10861), new Item(15301), new Item(15302), new Item(15303), new Item(15304), new Item(3929),
			new Item(15308), new Item(15309), new Item(15310), new Item(13703), new Item(13704), new Item(13705),
			new Item(13695), new Item(13692), new Item(13693), new Item(13696), new Item(13697), new Item(13698),
			new Item(13699), new Item(13700), new Item(13701), new Item(13702), new Item(22317), new Item(22280),
			new Item(21225, 1), new Item(20035), new Item(20047), new Item(20044), new Item(20035), new Item(20038), new Item(13824), new Item(13825)
			, new Item(13826), new Item(13808), new Item(13809), new Item(13810), new Item(13811), new Item(13812),
			new Item(13832, 1), new Item(13710, 1), new Item(13711, 1), new Item(13712, 1),
			new Item(13713, 1), new Item(13714, 1), new Item(13715, 1), new Item(13805, 1), new Item(13832, 1), new Item(13816, 1),
			new Item(13814, 1), new Item(10075, 1), new Item(13749, 1), new Item(13831, 1), new Item(13833, 1),
			new Item(20031, 1),
			new Item(20043, 1),
			new Item(20052, 1),
			new Item(20151, 1), new Item(20064, 1), new Item(20043, 1), new Item(20032, 1), new Item(21269, 1), new Item(21290, 1), new Item(22128, 1),
			new Item(34, 1), new Item(22129, 1),	
			new Item(79, 1),
			new Item(80, 1),
			new Item(20157, 1),
			new Item(22325, 1),
			new Item(3078 , 1),
			new Item(3074 , 1),
			new Item(3075 , 1),
			new Item(13708 , 1),
			new Item(3073, 1),
			new Item(22325, 1),
			new Item(21292, 1),
			new Item(22130, 1), new Item(22131, 1), new Item(22132, 1), new Item(22133, 1), new Item(22134, 1), new Item(22135, 1), new Item(22136, 1),new Item(22137, 1),  

			};
	
	
	
	public final static Item[] BANK_ITEMS = { new Item(995, 10000), new Item(6199, 10000), new Item(12955, 10000),
			new Item(11739, 10000), new Item(299, 10000), new Item(455, 10000), new Item(11665, 10000),
			new Item(11663, 10000), new Item(11664, 10000), new Item(8840, 10000), new Item(8839, 10000),
			new Item(8842, 10000), new Item(4151, 10000), new Item(11802, 10000), new Item(11804, 10000),
			new Item(11806, 10000), new Item(11808, 10000), new Item(13652, 10000), new Item(13576, 10000),
			new Item(4587, 10000), new Item(1305, 10000), new Item(7158, 10000), new Item(1434, 10000),
			new Item(5698, 10000), new Item(1215, 10000), new Item(11840, 10000), new Item(6585, 10000),
			new Item(12954, 10000), new Item(11283, 10000), new Item(10551, 10000), new Item(4153, 10000),
			new Item(10828, 10000), new Item(3751, 10000), new Item(2550, 10000), new Item(3105, 10000),
			new Item(1725, 10000), new Item(1704, 10000), new Item(1731, 10000), new Item(1163, 10000),
			new Item(1127, 10000), new Item(1079, 10000), new Item(1201, 10000), new Item(7460, 10000),
			new Item(4131, 10000), new Item(11832, 10000), new Item(11834, 10000), new Item(11836, 10000),
			new Item(21015, 10000), new Item(6570, 10000), new Item(13239, 10000), new Item(11773, 10000),
			new Item(12931, 10000), new Item(13197, 10000), new Item(13199, 10000), new Item(19639, 10000),
			new Item(19643, 10000), new Item(19647, 10000), new Item(11772, 10000), new Item(861, 10000),
			new Item(892, 10000), new Item(9185, 10000), new Item(9244, 10000), new Item(868, 10000),
			new Item(811, 10000), new Item(10498, 10000), new Item(10499, 10000), new Item(3749, 10000),
			new Item(2503, 10000), new Item(2497, 10000), new Item(2491, 10000), new Item(19481, 10000),
			new Item(11785, 10000), new Item(11826, 10000), new Item(11828, 10000), new Item(11830, 10000),
			new Item(13237, 10000), new Item(11771, 10000), new Item(4675, 10000), new Item(12904, 10000),
			new Item(11791, 10000), new Item(11770, 10000), new Item(13235, 10000), new Item(3755, 10000),
			new Item(1540, 10000), new Item(6889, 10000), new Item(2412, 10000), new Item(3840, 10000),
			new Item(2413, 10000), new Item(3844, 10000), new Item(2414, 10000), new Item(3842, 10000),
			new Item(4089, 10000), new Item(4091, 10000), new Item(4093, 10000), new Item(4095, 10000),
			new Item(4097, 10000), new Item(4099, 10000), new Item(4101, 10000), new Item(4103, 10000),
			new Item(4105, 10000), new Item(4107, 10000), new Item(4109, 10000), new Item(4111, 10000),
			new Item(4113, 10000), new Item(4115, 10000), new Item(4117, 10000), new Item(554, 10000),
			new Item(555, 10000), new Item(556, 10000), new Item(557, 10000), new Item(558, 10000),
			new Item(559, 10000), new Item(560, 10000), new Item(561, 10000), new Item(562, 10000),
			new Item(563, 10000), new Item(564, 10000), new Item(565, 10000), new Item(9075, 10000),
			new Item(6685, 10000), new Item(3024, 10000), new Item(2436, 10000), new Item(2440, 10000),
			new Item(2442, 10000), new Item(2444, 10000), new Item(3040, 10000), new Item(2434, 10000),
			new Item(2448, 10000), new Item(6687, 10000), new Item(3026, 10000), new Item(145, 10000),
			new Item(157, 10000), new Item(163, 10000), new Item(169, 10000), new Item(3042, 10000),
			new Item(139, 10000), new Item(181, 10000), new Item(6689, 10000), new Item(3028, 10000),
			new Item(147, 10000), new Item(159, 10000), new Item(165, 10000), new Item(171, 10000),
			new Item(3044, 10000), new Item(141, 10000), new Item(183, 10000), new Item(6691, 10000),
			new Item(3030, 10000), new Item(149, 10000), new Item(161, 10000), new Item(167, 10000),
			new Item(173, 10000), new Item(3046, 10000), new Item(143, 10000), new Item(185, 10000),
			new Item(12695, 10000), new Item(3144, 10000), new Item(391, 10000), new Item(7060, 10000),
			new Item(385, 10000), new Item(4315, 10000), new Item(4317, 10000), new Item(4319, 10000),
			new Item(4335, 10000), new Item(4337, 10000), new Item(4339, 10000), new Item(4355, 10000),
			new Item(4357, 10000), new Item(4359, 10000), new Item(4375, 10000), new Item(4377, 10000),
			new Item(4379, 10000), new Item(4395, 10000), new Item(4397, 10000), new Item(4399, 10000),
			new Item(4716, 10000), new Item(4718, 10000), new Item(4720, 10000), new Item(4722, 10000),
			new Item(4745, 10000), new Item(4747, 10000), new Item(4749, 10000), new Item(4751, 10000),
			new Item(4732, 10000), new Item(4734, 10000), new Item(4736, 10000), new Item(4738, 10000),
			new Item(4740, 10000), new Item(4708, 10000), new Item(4710, 10000), new Item(4712, 10000),
			new Item(4714, 10000), new Item(4753, 10000), new Item(4755, 10000), new Item(4757, 10000),
			new Item(4759, 10000), new Item(12829, 10000), new Item(12821, 10000), new Item(12817, 10000),
			new Item(12825, 10000), new Item(21225, 10000), };

	public static final int EMAIL_MAX_CHARACTERS = 28;
	public static final int EMAIL_MIN_CHARACTERS = 7;
	public static final int USERNAME_MAX_CHARACTERS = 20;
	public static final int USERNAME_MIN_CHARACTERS = 3;
	public static final int PASSWORD_MAX_CHARACTERS = 20;

	public static final boolean MY_SQL = false;

	static {
		try {
			final Toml parser = new Toml().read(new File("./settings.toml"));
			PARALLEL_GAME_ENGINE = parser.getBoolean("server.parallel_game_engine");
			SERVER_NAME = parser.getString("server.server_name");
			SERVER_PORT = Math.toIntExact(parser.getLong("server.server_port"));
			LIVE_SERVER = parser.getBoolean("server.live_server");
			SERVER_DEBUG = parser.getBoolean("server.server_debug");
			SERVER_CYCLE_DEBUG = parser.getBoolean("server.server_cycle_debug");
			NPC_BITS = Math.toIntExact(parser.getLong("game.npc_bits"));
			NPC_WALKING_RADIUS = Math.toIntExact(parser.getLong("game.npc_walking_radius"));
			LOGIN_THESHOLD = Math.toIntExact(parser.getLong("network.login_threshold"));
			LOGOUT_THESHOLD = Math.toIntExact(parser.getLong("network.logout_threshold"));
			MAX_PLAYERS = Math.toIntExact(parser.getLong("game.max_players"));
			MAX_NPCS = Math.toIntExact(parser.getLong("game.max_npcs"));
			MAX_BOTS = Math.toIntExact(parser.getLong("game.max_bots"));
			NPC_DEFINITION_LIMIT = Math.toIntExact(parser.getLong("game.npc_definition_limit"));
			ITEM_DEFINITION_LIMIT = Math.toIntExact(parser.getLong("game.item_definition_limit"));
			WEBSITE_URL = parser.getString("website.website_url");
			FORUM_INTEGRATION = parser.getBoolean("website.website_integration");
			FORUM_DB_URL = parser.getString("website.forum_db_url");
			FORUM_DB_USER = parser.getString("website.forum_db_user");
			FORUM_DB_PASS = parser.getString("website.forum_db_pass");
			DISCORD_TOKEN = parser.getString("discord.token");
			COMBAT_MODIFICATION = parser.getDouble("game.combat_modifier");
			AGILITY_MODIFICATION = parser.getDouble("game.agility_modifier");
			COOKING_MODIFICATION = parser.getDouble("game.cooking_modifier");
			CRAFTING_MODIFICATION = parser.getDouble("game.crafting_modifier");
			FIREMAKING_MODIFICATION = parser.getDouble("game.firemaking_modifier");
			FLETCHING_MODIFICATION = parser.getDouble("game.fletching_modifier");
			HERBLORE_MODIFICATION = parser.getDouble("game.herblore_modifier");
			HUNTER_MODIFICATION = parser.getDouble("game.hunter_modifier");
			MAGIC_MODIFICATION = parser.getDouble("game.magic_modifier");
			MINING_MODIFICATION = parser.getDouble("game.mining_modifier");
			PRAYER_MODIFICATION = parser.getDouble("game.prayer_modifier");
			RUNECRAFTING_MODIFICATION = parser.getDouble("game.runecrafting_modifier");
			SLAYER_MODIFICATION = parser.getDouble("game.slayer_modifier");
			WOODCUTTING_MODIFICATION = parser.getDouble("game.woodcutting_modifier");
			THIEVING_MODIFICATION = parser.getDouble("game.thieving_modifier");
			SMITHING_MODIFICATION = parser.getDouble("game.smithing_modifier");
			FARMING_MODIFICATION = parser.getDouble("game.farming_modifier");
			FISHING_MODIFICATION = parser.getDouble("game.fishing_modifier");
			LOG_PLAYER = parser.getBoolean("game.log_player");
			SKULL_TIME = Math.toIntExact(parser.getLong("game.skull_time"));
			VPS_IP = parser.getString("vps.vps_ip");

			// network
			CONNECTION_LIMIT = Math.toIntExact(parser.getLong("network.connection_limit"));
			FAILED_LOGIN_ATTEMPTS = Math.toIntExact(parser.getLong("network.failed_login_attempts"));
			FAILED_LOGIN_TIMEOUT = Math.toIntExact(parser.getLong("network.failed_login_timeout"));
			IDLE_TIMEOUT = Math.toIntExact(parser.getLong("network.idle_timeout"));
			CLIENT_PACKET_THRESHOLD = Math.toIntExact(parser.getLong("network.client_packet_threshold"));
			DISPLAY_PACKETS = parser.getBoolean("network.display_packets");
			RESOURCE_LEAK_DETECTION = ResourceLeakDetector.Level
					.valueOf(parser.getString("network.resource_leak_detection").toUpperCase());
			RSA_MODULUS = new BigInteger(parser.getString("network.rsa_modulus"));
			RSA_EXPONENT = new BigInteger(parser.getString("network.rsa_exponent"));
			POSTGRE_URL = parser.getString("postgre.postgre_url");
			POSTGRE_USER = parser.getString("postgre.postgre_user");
			POSTGRE_PASS = parser.getString("postgre.postgre_pass");

			CACHE_VERSION = Math.toIntExact(parser.getLong("client.cache_version"));

			GAME_VERSION = CACHE_VERSION;

			TEST_WORLD = parser.getBoolean("server.test_world");
			highscoresEnabled = parser.getBoolean("services.highscores_enabled");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError("Failed to parse config file.");
		}
	}
}
