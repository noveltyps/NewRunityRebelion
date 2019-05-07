package io.server.content.triviabot;

import io.server.util.RandomUtils;

/**
 * Holds all the trivia bot data.
 *
 * @author Adam_#6723
 */
public enum TriviaBotData {
	_1("Who is the owner of RebelionX?", "Parano1a"),
	_2("Name one game developer.", "Parano1a"),
	_3("What is the maximum combat level?", "126"), 
	_4("What is the highest tier of donator?", "supreme donator", "supreme"),
	_5("What is the lowest tier of donator?", "donator", "regular donator"),
	_6("What is the maximum amount of friends allowed?", "200", "two hundred", "two-hundred"),
	_7("What item can be rewarded from the Fight caves activity?", "fire cape", "f cape"),
	_9("What is the maximum level of any skill?", "99", "ninety nine", "ninety-nine"),
	_11("What server is dope af?", "RebelionX", "RebelionX"), 
	_12("What is the location name of home?", "edgeville", "edge"),
	_13("Name one of the rare drops that General Gaardoor drops.", "bandos tasset", "bandos boots", "bandos chestplate",
			"bandos hilt", "godsword shard", "bandos tassets"),
	_14("How many Hitpoints do Sharks heal?", "20", "twenty"),
	_15("What Magic level is needed to cast Ice Barrage", "94"),
	_16("What is the highest tier defender?", "dragon", "dragon defender"),
	_17("What is the required Attack level needed to wield the Abyssal whip?", "70", "seventy"),
	_18("What is the required Strength level needed to wield the Tzhaar-ket-om?", "60", "sixty"),
	_19("What is the required Ranged level needed to wield Red chinchompas?", "55", "fifty-five"),
	_21("What is the required Slayer level needed to kill skeletal Wyverns?", "72", "seventy-two"),
	_22("How many waves are there in the Fight Caves activity?", "15"),
	_23("How many charges can an amulet of glory hold?", "6", "six"),
	_24("What herb is required to make a Prayer potion?", "ranarr"),
	_25("What cmb lvl is required to join the veteran boat for the PC?", "100"),
	_28("What boss drops the Elysian sigil?", "corporeal beast", "corp", "corp beast"),
	_30("What level is Zulrah?", "725"),
	_31("Name 1 rune required to cast the tele-block spell.", "chaos", "law", "death"),
	_32("How many Nature runes are needed to cast the Entangle spell?", "4"),
	_33("The Amulet of fury is created using which gem?", "onyx"),
	_34("What Ranged level is required to wield the Toxic blowpipe?", "75", "seventy-five"),
	_35("What Attack level is required to wield the Armadyl godsword?", "75", "seventy-five"),
	_36("How much ranged bonus does the Armadyl chestplate provide?", "33", "thirty-three"),
	_37("How many types of godswords are there?", "4", "four"),
	_38("How many Marks of grace are needed to purchase the full graceful set?", "260"),
	_39("What logs can be cut at level 90 Woodcutting?", "redwood", "redwood logs"),
	_40("What is the Prayer requirement needed to wield the Elysian spirit shield?", "75", "seventy-five"),
	_41("What ore can be mined at level 70 Mining?", "adamantite", "adamantite ore", "adamamant ore"),
	_42("Which godsword has the strongest special attack in PvP?", "ags", "armadyl", "armadyl godsword"),
	_43("Which godswords special attack can heal you?", "sgs", "saradomin", "saradomin godsword"),
	_44("Which godswords special attack drains your opponents stats?", "bgs", "bandos", "bandos godsword"),
	_45("Which godswords special attack freezes your enemy?", "zgs", "zamorak", "zamorak godsword"),
	_46("Which spirit shield is best for magic?", "arcane", "arcane spirit shield"),
	_47("Name one of the Barrows brothers.", "dharok", "torag", "karil", "guthan", "verac", "ahrim"),
	_48("How long does a full teleblock last for? [ In minutes ].", "5", "five"),
	_49("What level Wilderness can you teleport from with a glory amulet?", "30", "thirty"),
	_50("How many Hitpoints do Dark crabs heal?", "22", "twenty two"),
	_51("What herb is required to make a Super restore potion?", "snapdragon"),
	_52("On which continent is the Woodcutting guild located?", "zeah"),
	_53("What ore can be mined at level 55 Mining?", "mithril", "mithril ore"),
	_54("What logs can be cut at level 60 Woodcutting?", "yew", "yew logs"),
	_55("What tree requires level 35 Woodcutting to chop?", "teak", "teak tree"),
	_56("A burgundy colored rock contains what ore?", "iron", "iron ore"),
	_57("A black colored rock contains what mineral?", "coal"),
	_58("Which combat style does General Graardor use most often?", "melee"),
	_59("How many combat styles does Zulrah use?", "3", "three"),
	_60("How many words are in this question?", "7", "seven"),
	_61("What Hunter level is required to catch Black chinchompas?", "73", "seventy three"),
	_62("How many bankers are there in Edgeville bank?", "3", "three"),
	_63("The Dragon claws' special attack deals how many hitsplats?", "4", "four"),
	_64("What Defence level is required to equip Bandos armour?", "65", "sixty five"),
	_65("What Crafting level is required to cut Onyx gems?", "90", "ninety"),
	_66("Steel bars are smelted using Coal and which ore?", "iron", "iron ore"),
	_67("How many Hitpoints do Sand crabs have?", "60", "sixty"),
	_68("Planks made from which log are used at high Construction levels?", "mahogany"),
	_69("What is the exact amount of experience needed for level 99 in a skill?", "13034431"),
	_70("How many times can a skill be prestiged?", "20", "Twenty"),
	_71("What special attack % does the Dragon dagger special use?", "25", "25%"),
	_72("What is the max damage of a single hit of the Dark bow's special attack?", "48", "fourty eight",
			"fourty-eight"),
	_73("What hunter level is required to catch black chinchompas?", "63", "sixty three"),
	/*_74("Which NPC sells skillcapes?", "Wise Old Man", "wise old man", "Wise old man"),*/
	_75("What is the entrance fee for brimhaven's agility arena?", "200", "200gp", "200 gp", "200 coins"),
	_76("Free players can use up to what type of arrows?", "adamant"),
	_77("What canoe can be made at level fifty-seven woodcutting?", "waka", "waka canoe"),
	_78("What farming level do you need to be to farm watermelon?", "47", "forty seven"),
	_79("When you bury big bones, how much prayer experience do you get?", "15", "fifteen"),
	_80("What is the name of the city where men and women turn into werewolves?", "canifis"),
	_90("What item prevents citizens from canifis from turning into a werewolf?", "wolfbane", "wolfbane dagger",
			"a wolfbane", "a wolfbane dagger"),
	_91("In the RFD quest, what is the name of the enemy that the Gypsy freezes?", "culinaromancer"),
	_92("What magic level is required to cast wind wave?", "62", "sixty two"),
	_93("The skull sceptre gives the wearer how many teleports to barbarian        village?", "five", "5"),
	_94("Which island do you sail to during the quest dragon slayer?", "crandor"),
	_95("Where is the magic guild located?", "yanille"),
	_96("What level is the dragon in dragon slayer?", "83", "eighty three"),
	_97("What is the fishing pet called?", "heron"), _98("What level are ducks?", "1", "one"),
	_99("What is the name of the dragon in dragon slayer?", "elvarg"),
	_100("Varrock lies in which kingdom?", "misthalin"),
	_101("What defence level do you need to be able to wear proselyte armor?", "30", "thirty"),
	_102("What woodcutting level do you need to chop the artic pine?", "54", "fifty four"),
	_104("In this skill, you can track and capture animals. What skill would this describe?", "hunter"),
	_105("What level are Jogres?", "53", "fifty three"),
	_106("What Slayer level do you need to be able to kill a Killerwatt?", "37", "thirty seven"),
	_107("What item is used to make bow strings?", "flax"),
	_108("How many Coal ores do you need to smelt a Mithril bar?", "4", "four"),
	_109("What Ranging Level do you need to be able to wear a Robin Hat?", "40", "forty"),
	_110("How many bars do you need to smith a Platebody of any kind?", "five", "5"),
	_111("What metal is made from 1 iron ore and 2 coal?", "steel"),
	_112("What Cooking Level do you need to be able to cook a Monkfish?", "62", "sixty two"),
	_113("What color is the party hat the Wise Old Man of Draynor Village is wearing?", "blue"),
	_114("What is the Varrock king's name?", "King Roald"),
	_115("What fairy ring code is needed to get the Star Flower in the quest, 'Fairy Tale Part 2'?", "ckp"),
	_116("Which herb is required to make agility potions?", "toadflax"),
	_117("What mining level must you be to enter the mining guild?", "60", "sixty"),
	_118("Vampire Slayer: What level is the vampire (Count Draynor)?", "34", "thirty four"),
	_119("What magic level do you need to cast ice plateau teleport?", "89", "eighty nine"),
	_120("What's better Marvel Or DC?", "89", "marvel"), 
	_121("Where is Glod located?", "ape atoll"),
	_122("What attack level is required to wield an Iron whip?", "1", "one"),
	_123("What wilderness level can you not teleport above?", "20", "twenty"),
	_124("Who is the owner of RebelionX?", "Parano1a", "Dani"),
	_125("How many partyhats are in a set?", "6", "six"),
	_128("What ingredient do you need to make Extreme Range potions?", "grenwall spikes"),
	_130("What gem is required to create a Ring of Wealth?", "dragon stone", "dragonstone"),
	_131("Who can you buy Skillcapes from?", "Wise Old Man"),
	_132("What attack/strength/defence/magic/range levels do you need to weild Barrows armor?", "70", "seventy"),
	_133("How many barrows brothers are there?", "6", "six"), _134("How many Godwars bosses are there?", "4", "four"),
	_135("Who do you protect in Pest Control?", "void knight"),
	_136("What is the strongest whip in-game?", "lime", "lime whip"),
	_142("How many dollars do you have to donate to gain access to Donator Island?", "10", "ten", "10$"),
	_143("What monster drops Camo?", "jungle demon"),
	_144("At what Magic level can you use Vengeance?", "94", "ninety four"),
	_145("At what Magic level can you use Ice Barrage?", "94", "ninety four"),
	_148("How many pets are you able to obtain?", "10", "ten"),
	_150("How many points do you get per game of Pest Control?", "12"),
	_151("How many marionette colors are there?", "3", "three"),
	_152("During what month did this server come out?", "February"), 
	_153("Which barrows brother uses magic?", "ahrim"),
	_154("Which barrows brother uses range?", "karil"),
	_155("Which barrows brother wields an axe?", "dharok"),
	_156("How many barrows brothers use melee?", "four", "4"),
	_157("What item does 1hp damage to yourself?", "rock cake", "dwarven rock cake", "rockcake"),
	_158("What monster drops an anchor?", "barrelchest", "barrel chest"),
	_159("What type of tree gives the most exp to cut?", "magic", "magic tree"),
	_160("What potion works like a super restore and antipoison?", "sanfew serum"),
	_165("How many moles are located near the mage arena?", "3", "three"),
	_166("In which city is a zoo located?", "ardougne"),
	_168("What skill is a tinderbox used for?", "firemaking"),
	_169("What skill is used to make potions?", "herblore"),
	_170("In what skill are you given tasks of monsters to kill?", "slayer"),
	_172("What item for slayer helm do you get from cave horrors?", "black mask", "mask", "blackmask"),
	_175("What is the highest level stall you can thieve in edgeville?", "magic", "magic stall"),
	_176("What altar switches prayers and curses?", "chaos", "chaos altar", "altar", "chaos"),
	_177("What is the best whip you can get from the zombie minigame?", "barrows", "barrows whip"),
	_178("How many zombie points does a full zombie set cost?", "5000", "five thousand"),
	_179("What portal is on the east side of pest control?", "blue", "blue portal"),
	_180("How many portals are in the pest control minigame?", "4", "four"),
	_181("What city does bob live in?", "lumbridge", "bobville"),
	_182("What monster drops eggs?", "chicken", "chickens"),
	_183("What is the max experience you can achieve in a skill?", "200m", "200000000"),
	_184("What bones give the most prayer experience?", "ancient bones", "ancient"),
	_187("How many slayer points does a hard slayer task give?", "15", "15 points", "fifteen", "fifteen points"),
	_188("What is the maximum number of players you can add to your friendslist?", "200", "two hundred"),

	;

	/** The trivial question. */
	private String question;

	/** The trivial answer. */
	private String[] answers;

	/** Used for removing whitespace **/
	private boolean removeSpacesInAnswer;

	/** Constructs a new <code>TriviaBotData</code>. */
	TriviaBotData(String question, String... answers) {
		this.question = question;
		this.answers = answers;
		this.removeSpacesInAnswer = false;
	}

	/** Constructs a new <code>TriviaBotData</code>. */
	TriviaBotData(boolean removeSpacesInAnswer, String question, String... answers) {
		this.removeSpacesInAnswer = removeSpacesInAnswer;
		this.question = question;
		this.answers = answers;
	}

	/** Gets the trivial question. */
	public String getQuestion() {
		return question;
	}

	/** Gets the trivial answer. */
	public String[] getAnswers() {
		return answers;
	}

	public void rerandomize() {
		if (question.contains("%twobyonedigitmultip")) {
			int ran1 = RandomUtils.inclusive(1, 99);
			int ran2 = RandomUtils.inclusive(1, 10);

			question = "What is " + ran1 + "x" + ran2 + "?" + "%twobyonedigitmultip";
			answers[0] = ran1 * ran2 + "";
		} else if (question.contains("%integralof1digitmultip")) {
			int ran1 = RandomUtils.inclusive(1, 10);
			int ran2 = RandomUtils.inclusive(1, 10);

			question = "In standard notation, what is the integral of " + ran1 + "*" + ran2 + "?"
					+ "%integralof1digitmultip";
			answers[0] = ran1 * ran2 + "x+C";
		} else if (question.contains("%base10tobase2")) {
			int answer = RandomUtils.inclusive(0, 128);
			question = "The base-10 equivalence of the following base-2 number \"" + Integer.toBinaryString(answer)
					+ "\" is:";
			answers[0] = answer + "";
		} else if (question.contains("%factor-polynomial")) {
			int a = RandomUtils.inclusive(-3, 3);
			int b = RandomUtils.inclusive(-3, 3);
			int c = RandomUtils.inclusive(-3, 3);

			question = "Name one root of x in the following polynomial: x^3";
			question += -(c + b + a) < 0 ? " " : " + ";
			question += (-(c + b + a)) + "x^2";
			question += (b * c + a * c + a * b) < 0 ? " " : " + ";
			question += (b * c + a * c + a * b) + "x";
			question += (a * b * c) > 0 ? " " : " + ";
			question += (-(a * b * c));

			answers[0] = a + "";
			answers[1] = b + "";
			answers[2] = c + "";

			question += "%factor-polynomial";
		} else if (question.contains("%probabilityofdrop")) {
			double n = RandomUtils.inclusive(100, 250);
			int kill = RandomUtils.inclusive(1, 500);

			question = "If the probability a boss drops a rare item is 1/" + (int) n
					+ " what is the percent  chance you do not receive any of the rare items after " + kill
					+ " kills? (2 digits of precision)";

			answers[0] = ("" + (Math.pow((n - 1) / n, kill))).substring(0, 4);
			answers[1] = ((Math.pow((n - 1) / n, kill)) + 0.01 + "").substring(0, 4);
			answers[2] = ((Math.pow((n - 1) / n, kill)) - 0.01 + "").substring(0, 4);
			answers[3] = ("%" + (Math.pow((n - 1) / n, kill)) * 100 + 1).substring(0, 3);
			answers[4] = ("%" + ((Math.pow((n - 1) / n, kill)) * 100 - 1)).substring(0, 3);
			answers[5] = ("%" + ((Math.pow((n - 1) / n, kill)) * 100)).substring(0, 3);
			answers[6] = ("" + (Math.pow((n - 1) / n, kill)) * 100 + 1).substring(0, 2) + "%";
			answers[7] = ("" + ((Math.pow((n - 1) / n, kill)) * 100 - 1)).substring(0, 2) + "%";
			answers[8] = ("" + ((Math.pow((n - 1) / n, kill)) * 100)).substring(0, 2) + "%";
			answers[9] = answers[6].substring(0, 2);
			answers[10] = answers[7].substring(0, 2);
			answers[11] = answers[8].substring(0, 2);

//            System.out.println("Answer is " + Arrays.toString(answers));

			question += "%probabilityofdrop";
		} else if (question.contains("%completeTheSquare")) {
			int a = RandomUtils.inclusive(-2, 2);
			while (a == 0) {
				a = RandomUtils.inclusive(-2, 2);
			}
			int b = 2 * a * RandomUtils.inclusive(-3, 3);
			int c = RandomUtils.inclusive(-7, 7);

			question = "Upon completing the square, it becomes apparent that the horizontal shift on " + a + "x^2";
			question += b < 0 ? " " : " + ";
			question += b + "x";
			question += c < 0 ? " " : " + ";
			question += c + " is what value?";
			answers[0] = -b / (2 * a) + "";

			/**
			 * Proof: ax^2 + bx + c = a(x^2 + bx/a + b^2/4a - b^2/4a) + c = a(x^2 + bx/a +
			 * b^2/4a) - b^2/4(a^2) + c = a(x + b/2a) + c According to
			 * https://www.softschools.com/math/calculus/function_transformations/ we can
			 * see that: F(x - k) is a horizontal shift k units to the right (positive)
			 * Therefor the horizontal shift would be -b/2a. -Red
			 */

			question += "%completeTheSquare";
		}
	}

	public boolean isRemoveSpacesInAnswer() {
		return removeSpacesInAnswer;
	}
}
