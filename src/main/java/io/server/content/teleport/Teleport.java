package io.server.content.teleport;

import io.server.content.skill.impl.magic.teleport.TeleportType;
import io.server.game.world.position.Position;

/**
 * The teleport data.
 *
 * @author Daniel/ Adam_#6723
 */
public enum Teleport {
	/* Minigames */
	ARENA("Mage Arena", TeleportType.MINIGAMES, new Position(2540, 4717, 0), false, new int[] { 2412, 2413, 2414 },
			true, "You will be teleported in a safe area", ""),
	GAMBLE("Gamble", TeleportType.MINIGAMES, new Position(3317, 3236, 0), false, new int[] { -1, 995 }, true,
			"The place to be if you want to lose bank", "Here comes the money!"),
	DUEl("Duel", TeleportType.MINIGAMES, new Position(3365, 3265, 0), false, new int[] { -1, 995 }, true,
			"The place to be if you want to lose bank", "Here comes the money!"),
	BARROWS("Barrows", TeleportType.MINIGAMES, new Position(3565, 3315, 0), true, new int[] { -1, 7462 }, true,
			"43 Prayer is highly recommended", "Don't forget your spade!"),
	FIGHT_CAVE("Fight Caves", TeleportType.MINIGAMES, new Position(2438, 5168, 0), false, new int[] { -1, 6570 }, true,
			"43 Prayer & High Range is recommended", ""),
	PEST_CONTROL("Pest Control", TeleportType.MINIGAMES, new Position(2662, 2655, 0), false,
			new int[] { 11663, 11664, 11665 }, true, "Having a group is highly recommended",
			"Make sure to pack your pesticides!"),
	WARRIOR_GUILD("Warrior Guild", TeleportType.MINIGAMES, new Position(2846, 3541, 0), true, new int[] { -1, 8850 },
			true, "A level of 130 (strength + attack) is needed", ""),
	KOLODIONS_ARENA("Kolodion's Arena", TeleportType.MINIGAMES, new Position(2540, 4717, 0), false,
			new int[] { 6918, 6916, 6924 }, true, "You will be teleported in a safe area", ""),
	All_Vs_One("All vs One", TeleportType.MINIGAMES, new Position(3169, 4958, 0), false,
			new int[] { 13833, 13713, 7775 }, true, "All Vs One! Taking on every boss ingame!", ""),
	ALL_VS_ONEV2("All vs One V2", TeleportType.MINIGAMES, new Position(2915, 4397, 0), false,
			new int[] { 21292, 13729, 7775 }, true, "All Vs One V2! Harder version of AllVOne", ""),
	ALL_VS_ONEV3("All vs One V3", TeleportType.MINIGAMES, new Position(2557, 4960, 0), false,
			new int[] { 21292, 13729, 7775 }, true, "All Vs One V3!", ""),
	//INFERO("Inferno", TeleportType.MINIGAMES, new Position(2540, 4717, 0), false,
		//	new int[] { 6918, 6916, 6924 }, true, "STILL UNDER CONSTRUCTION!", ""),
/*	FREE_FOR_ALL("Free For All", TeleportType.MINIGAMES, new Position(3296, 5004, 0), false,
			new int[] { -1, -1, -1 }, false, "You will be teleported to FFA", ""),*/

	/* Skilling */
	AGILITY("Agility", TeleportType.SKILLING, new Position(3293, 3182, 0), true, new int[] { 9773, 9771, 9772 }, true,
			"Want to be flexible and agile in bed?", "This is how you get there"),
	CRAFTING("Crafting", TeleportType.SKILLING, new Position(2747, 3444, 0), false, new int[] { 9782, 9780, 9781 },
			true, "Trying to make some sick crafts?", "Level up that crafting!"),
	FISHING("Fishing", TeleportType.SKILLING, new Position(2809, 3435, 0), false, new int[] { 9800, 9798, 9799 }, true,
			"Want to impress the girl next door?", "Get her some premium trout!"),
	HUNTER("Hunter", TeleportType.SKILLING, new Position(3039, 4836, 0), true, new int[] { 9950, 9948, 9949 }, true,
			"Looking to hunt the biggest game?", "Go pking, cause this shits too easy"),
	MINING("Mining", TeleportType.SKILLING, new Position(3039, 4836, 1), true, new int[] { 9794, 9792, 9793 }, true,
			"Let your natural slave roots take over...", "Mine those damn rocks!"),
	THIEVING("Thieving", TeleportType.SKILLING, new Position(2661, 3305, 0), false, new int[] { 9779, 9777, 9778 },
			true, "Working hard? What's that?", "Let's just steal our gold!"),
	RUNECRAFTING("Runecrafting", TeleportType.SKILLING, new Position(3039, 4836, 0), false,
			new int[] { 9767, 9765, 9766 }, true, "There's no feeling that can compare with",
			"crafting runes out of your ass!"),
	WOODCUTTING("Woodcutting", TeleportType.SKILLING, new Position(2727, 3484, 0), true, new int[] { 9809, 9807, 9808 },
			true, "HOW MUCH WOOD WOULD A WOODCHUCK CHUCK IF A", "WOODCHUCK COULD CHUCK WOOD?"),
	WILDERNESS_RESOURCE("Wilderness Resource", TeleportType.SKILLING, new Position(3184, 3947, 0), false,
			new int[] { 11934, 451, 1513 }, false, "This teleport is in the lvl 50+ wilderness!",
			"YOLO tho, am I right??"),

	/* Monster Killing */
	GOBLINS("Training Island", TeleportType.MONSTER_KILLING, new Position(2462, 4769, 0), false, new int[] { -1, 288 },
			true, "", ""),
	ROCK_CRABS("Rock Crabs", TeleportType.MONSTER_KILLING, new Position(2676, 3711, 0), false, new int[] { -1, 411 },
			true, "", ""),
	SAND_CRABS("Sand Crabs", TeleportType.MONSTER_KILLING, new Position(1726, 3463, 0), false, new int[] { -1, 411 },
			true, "", ""),
	AL_KHARID_WARRIORS("Al-Kharid Warriors", TeleportType.MONSTER_KILLING, new Position(3293, 3182, 0), false,
			new int[] {}, true, "", ""),
	HILL_GIANTS("Hill Giants", TeleportType.MONSTER_KILLING, new Position(3117, 9856, 0), false, new int[] {}, true, "",
			""),
	YAKS("Yaks", TeleportType.MONSTER_KILLING, new Position(2321, 3804, 0), false, new int[] {}, true, "", ""),
	ANKOUS("Ankous", TeleportType.MONSTER_KILLING, new Position(2359, 5236, 0), false, new int[] {}, true, "", ""),
	SLAYER_TOWER("Slayer Tower", TeleportType.MONSTER_KILLING, new Position(3429, 3538, 0), false, new int[] {}, true,
			"", ""),
	TAVERLY_DUNGEON("Taverly Dungeon", TeleportType.MONSTER_KILLING, new Position(2884, 9800, 0), false, new int[] {},
			true, "", ""),
	RELEKKA_DUNGEON("Rellekka Dungeon", TeleportType.MONSTER_KILLING, new Position(2792, 10019, 0), false, new int[] {},
			true, "", ""),
	BRIMHAVEN_DUNGEON("Brimhaven Dungeon", TeleportType.MONSTER_KILLING, new Position(2681, 9563, 0), false,
			new int[] {}, true, "", ""),
	FREMINEK_DUNGEON("Fremennik Slayer Dungeon", TeleportType.MONSTER_KILLING, new Position(2807, 10002, 0), false,
			new int[] {}, true, "", ""),
	SMOKE_DEVILS("Smoke Devils", TeleportType.MONSTER_KILLING, new Position(3208, 9379, 0), false,
			new int[] { -1, 12002 }, true, "Looks like somebody has been vaping in this dungeon", "Bring a facemask!"),
	DUST_DEVILS("Dust Devils", TeleportType.MONSTER_KILLING, new Position(3239, 9364, 0), false,
			new int[] { 20736, 2513, 20736 }, true, "Looks like somebody has been farting in this dungeon",
			"Bring a facemask!"),
	DARK_BEAST("Dark Beasts", TeleportType.MONSTER_KILLING, new Position(2018, 4639, 0), false, new int[] { -1, 11235 },
			true, "What these fellas lack in their downstairs area", "make it up with they're 'long' horn"),
	DEMONIC_GORILLAS("Demonic Gorillas", TeleportType.MONSTER_KILLING, new Position(2130, 5647, 0), false,
			new int[] { -1, 19529 }, true, "Some people say that these gorillas",
			"are haramabe, back to haunt the living!"),
	SKELETAL_WYVERNS("Skeletal Wyverns", TeleportType.MONSTER_KILLING, new Position(3055, 9564, 0), false,
			new int[] { -1, 11286 }, true, "Ice cold, baby.", ""),
	MITHRIL_DRAGONS("Mithril Dragons", TeleportType.MONSTER_KILLING, new Position(1748, 5326, 0), false,
			new int[] { -1, 11286 }, true, "", ""),

	/* Player Killing */
	MAGE_BANK("Mage Bank", TeleportType.PLAYER_KILLING, new Position(2540, 4717, 0), false,
			new int[] { 2412, 2413, 2414 }, true, "You will be teleported in a safe area", ""),
	EDGEVILLE("Edgeville", TeleportType.PLAYER_KILLING, new Position(3087, 3517, 0), false, new int[] {}, true,
			"You will be teleported in a safe area", "Time to destroy some people!"),
	CASTLE("Castle", TeleportType.PLAYER_KILLING, new Position(3002, 3626, 0), false, new int[] {}, false,
			"This teleport is in 14 Wilderness", ""),
	EAST_DRAGONS("East Dragons", TeleportType.PLAYER_KILLING, new Position(3333, 3666, 0), false,
			new int[] { -1, 1540 }, false, "This teleport is in 19 Wilderness", "Beware of Dragons!"),
	WEST_DRAGONS("West Dragons", TeleportType.PLAYER_KILLING, new Position(2976, 3597, 0), false,
			new int[] { -1, 1540 }, false, "This teleport is in 10 Wilderness", "Beware of Dragons!"),
	GRAVES("Graveyard", TeleportType.PLAYER_KILLING, new Position(3180, 3671, 0), false, new int[] { -1, 6722 }, false,
			"This teleport is in 19 Wilderness", "Don't let the undead eat your nuts!"),
	LAVA_DRAGONS("Lava Dragons", TeleportType.PLAYER_KILLING, new Position(3195, 3865, 0), false,
			new int[] { -1, 11286 }, false, "This teleport is in the wilderness lvl 44", "It is also in multi!"),
	WILDERNESS_RESOURCE2("Wilderness Resource", TeleportType.PLAYER_KILLING, new Position(3184, 3947, 0), false,
			new int[] { 11934, 451, 1513 }, false, "This teleport is in the lvl 50+ wilderness!",
			"YOLO tho, am I right??"),

    /* Boss Killing */
    KING_BLACK_DRAGON("King Black Dragon", TeleportType.BOSS_KILLING, new Position(2997, 3849, 0), false, new int[]{-1, 12653}, false, "This teleport is in 40+ wilderness", "High combat and advanced gear is recommended"),
    ZULRAH("Zulrah", TeleportType.BOSS_KILLING, new Position(0, 0, 0), true, new int[]{12921, 12939, 12940}, true, "High range & magic is highly recommended", "Beware of her poisonous venom!"),
    KRAKEN("Kraken", TeleportType.BOSS_KILLING, new Position(2976, 4384, 0), true, new int[]{12004, 12655, 12004}, true,  "Make sure to have high magic defence", "Kraken is stronger than usual!"),
    CRAZY_ARCH("Crazy Archaeologist", TeleportType.BOSS_KILLING, new Position(2966, 3698, 0), false, new int[]{}, false, "This teleport is in 23 wilderness", "Be careful, he's crazy!"),
    CHAOS_FANATIC("Chaos Fanatic", TeleportType.BOSS_KILLING, new Position(2982, 3832, 0), false, new int[]{-1, 11995}, false, "This teleport is in 40 wilderness", "This guy is a fanatic for chaos!"),
    CALLISTO("Callisto", TeleportType.BOSS_KILLING, new Position(3274, 3847, 0), false, new int[]{-1, 13178}, false, "This teleport is in 41 wilderness & is a multi-zone", "Cautious of his roar and pkers!"),
    SCORPIA("Scorpia", TeleportType.BOSS_KILLING, new Position(3233, 3944, 0), false, new int[]{-1, 13181}, false, "This teleport is in 53 wilderness & is a multi-zone", "Watch out for pkers!"),
    VETION("Vet'ion", TeleportType.BOSS_KILLING, new Position(3217, 3781, 0), false, new int[]{13179, 12601, 13180}, false, "This teleport is in 33 wilderness & is a multi-zone", "Watch out for pkers!"),
    CORPOREAL_BEAST("Corporeal Beast", TeleportType.BOSS_KILLING, new Position(2967, 4383, 2), false, new int[]{}, true, "Having a high combat level is recommended", "Bringing a team is also advisable"),
    DAGGANOTHS("Dagannoth", TeleportType.BOSS_KILLING, new Position(1912, 4367, 0), false, new int[]{6731, 6737, 6733}, true, "", ""),
    LIZARD_SHAMAN("Lizard Shaman", TeleportType.BOSS_KILLING, new Position(1454, 3690, 0), false, new int[]{-1, 13576}, true, "Ever wanted to just murder a bunch of lizards?", "Well here is your chance, kill them all!"),

    ICE_DEMON("Ice Demon", TeleportType.BOSS_KILLING, new Position(2525, 4656, 0), false, new int[]{11943, 11941, 13724}, true,
    		"Take on a demon made of ice?", "...best of luck my dood."),
    PORAZDIR("Porazdir", TeleportType.BOSS_KILLING, new Position(3230, 10198, 0), false, new int[]{12073, 22111, 21225}, false, "Zamorak's finest beast.", "He's dangerous..."),
    REVS("Revenant Caves", TeleportType.BOSS_KILLING, new Position(3126, 3784, 0), false, new int[]{20035, 20038, 20044}, false, "They have no souls", "the undead is rising again"),
    VORKATH("Vorkath", TeleportType.BOSS_KILLING, new Position(2271, 4046, 0), false, new int[]{21992, 12073, 11286}, true, "a Demonic dragon", "Scary af"),
    TARN("Mutant Tarn", TeleportType.BOSS_KILLING, new Position(3037, 5346, 0), false, new int[]{13190, 13814, 20151}, true, "Ever wanted to just murder a bunch of mutant?", "Well here is your chance, kill them all!"),
    KURASK("Kurask", TeleportType.BOSS_KILLING, new Position(0, 0, 0), true, new int[]{22301, 22304, 22307}, true, "Kurask is a beast...", "But you FEAR NO ONE!!!"),
  //  ZULRAH("Zulrah", TeleportType.BOSS_KILLING, new Position(0, 0, 0), true, new int[]{12921, 12939, 12940}, true, "High range & magic is highly recommended", "Beware of her poisonous venom!"),

//    CERBERUS("Cerberus", TeleportType.BOSS_KILLING, new Position(2872, 9847, 0), 1500, false, "", ""),
//    LIZARDMAN_SHAMAN("Lizardman Shaman", TeleportType.BOSS_KILLING, new Position(1495, 3700, 0), 1500, false, "", ""),
//    GIANT_MOLE("Giant Mole", TeleportType.BOSS_KILLING, new Position(1761, 5186, 0), 1500, false, "", ""),;;
    ;

//    CERBERUS("Cerberus", TeleportType.CITIES, new Position(2872, 9847, 0), 1500, false, "", ""),
//    LIZARDMAN_SHAMAN("Lizardman Shaman", TeleportType.CITIES, new Position(1495, 3700, 0), 1500, false, "", ""),
//    GIANT_MOLE("Giant Mole", TeleportType.CITIES, new Position(1761, 5186, 0), 1500, false, "", ""),;;
	;

	/** The name of the teleport. */
	private final String name;

	/** The type of the teleport. */
	private final TeleportType type;

	/** The position of the teleport. */
	private final Position position;

	/** If the teleport is a special case. */
	private final boolean special;

	/** The item display of the teleport. */
	private final int[] items;

	private final boolean customsAllowed;

	public boolean getcustomsAllowed() {
		return customsAllowed;
	}

	/** Creates a new <code>Teleport<code>. */
	Teleport(String name, TeleportType type, Position position, boolean special, int[] items, boolean customsAllowed,
			String... strings) {
		this.name = name;
		this.type = type;
		this.position = position;
		this.special = special;
		this.items = items;
		this.customsAllowed = customsAllowed;
		this.strings = strings;
	}

	/** The information strings of the teleport. */
	private final String[] strings;

	/** Gets the name of the teleport. */
	public String getName() {
		return name;
	}

	/** Gets the type of the teleport. */
	public TeleportType getType() {
		return type;
	}

	/** Gets the position of the teleport. */
	public Position getPosition() {
		return position;
	}

	/** Gets the npc display of the itemcontainer. */
	public int[] getDisplay() {
		return items;
	}

	/** If the teleport is a special case. */
	public boolean isSpecial() {
		return special;
	}

	/** Gets the information strings of the teleport. */
	public String[] getStrings() {
		return strings;
	}

	@Override
	public String toString() {
		return "Name: " + name + " - Type: " + type + " - Special: " + special;
	}
}
