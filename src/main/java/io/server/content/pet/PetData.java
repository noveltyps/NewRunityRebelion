package io.server.content.pet;

import java.util.Arrays;
import java.util.Optional;

import io.server.content.dialogue.DialogueFactory;

/**
 * Holds the data for pets.
 *
 * @author adam
 */
public enum PetData {
/*	CRAWLING_HAND("Crawling Hand", 4133, 457) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},*/
	BABY_RUNE_DRAGON("Baby Rune Dragon", 21911, 8091) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	PARANO1A("Parano1a", 7148, 2656) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Dont you touch me!");
			factory.sendStatement("Get back to grinding!");
			factory.execute();

		}
	},
	
	GROUDON("Groudon", 19923, 2627) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Dont you touch me!");
			factory.sendStatement("Get back to grinding!");
			factory.execute();

		}
	},
	
	BABY_RED_DRAGON("Baby Red Dragon", 8134, 137) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	HOBGOBLIN("Hobgoblin", 8133, 3354) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	SUQAH_MONSTER("Suqah Monster", 9105, 787) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	MUTATED_ZYGOMITE("Mutated Zygomite", 7420, 473) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	HERBIBORE("Herbibore", 21511, 7785) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	HERBI("Herbi", 21509, 7760) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	ABYSSAL_ORPHAN("Abyssal Orphan", 13262, 5883) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	ABYSSAL_DEMON("Abyssal Orphan", 4149, 5883) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hello");
			factory.sendNpcChat(5883, "Are you my dad?!");
			factory.sendPlayerChat("Im not your DAD! Ugly ass suckin noodle head.");
			factory.execute();

		}
	},
	PIRATE_PETE("Pirate Pete", 7505, 4052) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Why does it take pirates so long to learn the alphabet?");
			factory.sendNpcChat(4052, "Uhm I don't know.");
			factory.sendPlayerChat("Because they can spend years at C.");
			factory.sendNpcChat(4052, "LOOL I Get it! Aha! That was brilliant matey!");

			factory.execute();

		}
	},
	VORKATH_PET("Vorki", 21992, 8029) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Aren't you a little cute thing!");
			factory.sendNpcChat(8029, "I ain't cute when im burning my victims to ash! AHAHAH");
			factory.sendPlayerChat("Welp sheeeeeet then.");
			factory.execute();

		}
	},
	PHOENIX("Phoenix", 20693, 7370) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Adam has yet to add Phoenix's dialogue :(");
			factory.execute();
		}
	},
	HELLPUPPY("Hellpuppy", 13247, 3099) {
		@Override
		public void dialogue(DialogueFactory factory) {
			// TODO http://oldschoolrunescape.wikia.com/wiki/Hellpuppy
			factory.sendPlayerChat("Adam has yet to add Hellpuppy's dialogue :(");
			factory.execute();
		}
	},

	OLMLET("Olmlet", 20851, 7519) {
		@Override
		public void dialogue(DialogueFactory factory) {
			// TODO http://oldschoolrunescape.wikia.com/wiki/Olmlet
			factory.sendPlayerChat("adam has yet to add Olmlet's dialogue :(");
			factory.execute();
		}
	},
	SKOTOS("Skotos", 21273, 7671) {
		@Override
		public void dialogue(DialogueFactory factory) {
			// TODO http://oldschoolrunescape.wikia.com/wiki/Skotos
			factory.sendPlayerChat("adam has yet to add Skoto's dialogue :(");
			factory.execute();
		}
	},

	PRINCE_BLACK_DRAGON("Prince Black Dragon", 12653, 6636) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Shouldn't a prince only have two heads?");
			factory.sendNpcChat(6636, "Why is that?");
			factory.sendPlayerChat("Well, a standard Black dragon has one, ",
					"the King has three, so in between must have two?");
			factory.sendNpcChat(6636, "You're overthinking this.");
			factory.execute();
		}
	},
	KREE("Kree'arra", 12649, 6643) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Huh... that's odd... I thought that would be big news.");
			factory.sendNpcChat(6643, "You thought what would be big news?");
			factory.sendPlayerChat("Well there seems to be an absence of a certain ",
					"ornithological piece: a headline regarding mass", "awareness of a certain avian variety.");
			factory.sendNpcChat(6643, "What are you talking about?");
			factory.sendPlayerChat("Oh have you not heard?", "It was my understanding that everyone had heard....");
			factory.sendNpcChat(6643, "Heard wha...... OH NO!!!!?!?!!?!");
			factory.sendPlayerChat("OH WELL THE BIRD, BIRD, BIRD, BIRD", "BIRD IS THE WORD. OH WELL THE BIRD,",
					"BIRD, BIRD, BIRD BIRD IS THE WORD.");
			factory.sendStatement("There's a slight pause as Kree'Arra Jr. goes stiff.");
			factory.execute();
		}
	},
	ZILYANA("Zilyana", 12651, 6633) {
		@Override
		public void dialogue(DialogueFactory factory) {
			if (factory.getPlayer().equipment.contains(11806)) {
				factory.sendPlayerChat("I FOUND THE GODSWORD!");
				factory.sendNpcChat(6633, "GOOD!!!!!");
			} else {
				factory.sendPlayerChat("FIND THE GODSWORD!");
				factory.sendNpcChat(6633, "FIND THE GODSWORD!");
			}
			factory.execute();
		}
	},
	TSUROTH("K'ril tsutsaroth", 12652, 6634) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("How's life in the light?");
			factory.sendNpcChat(6634, "Burns slightly.");
			factory.sendPlayerChat("You seem much nicer than your father. He's mean.");
			factory.sendNpcChat(6634, "If you were stuck in a very dark cave",
					"for centuries you'd be pretty annoyed too.");
			factory.sendPlayerChat("I guess.");
			factory.sendNpcChat(6634, "He's actually quite mellow really..");
			factory.sendPlayerChat("Uh.... Yeah.");
			factory.execute();

		}
	},
	GRAARDOR("General graardor", 12650, 6644) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Not sure this is going to be worth my", "time but... how are you?");
			factory.sendNpcChat(6644, "SFudghoigdfpDSOPGnbSOBNfdbd", "nopbdnopbddfnopdfpofhdARRRGGGGH");
			factory.sendPlayerChat("Nope. Not worth it.");
			factory.execute();

		}
	},
	PRIME("Daggonoth prime", 12644, 6629) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("So despite there being three kings, you're", "clearly the leader, right?");
			factory.sendNpcChat(6629, "Yes.");
			factory.sendPlayerChat("I'm glad I got you as a pet.");
			factory.sendNpcChat(6629, "Ugh. Human, I'm not a pet.");
			factory.sendPlayerChat("Stop following me then.");
			factory.sendNpcChat(6629, "I can't seem to stop.");
			factory.sendPlayerChat("Pet.");
			factory.execute();
		}
	},
	REX("Daggonoth rex", 12645, 6641) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Do you have any berserker rings?");
			factory.sendNpcChat(6641, "Nope.");
			factory.sendPlayerChat("You sure?");
			factory.sendNpcChat(6641, "Yes.");
			factory.sendPlayerChat("So, if I tipped you upside down and shook you,",
					"you'd not drop any berserker rings?");
			factory.sendNpcChat(6641, "Nope.");
			factory.sendPlayerChat("What if I endlessly killed your father for weeks on end,", "would I get one then?");
			factory.sendNpcChat(6641, "Been done by someone, nope");
			factory.execute();
		}
	},
	SUPREME("Daggonoth supreme", 12643, 6628) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hey, so err... I kind of own you now.");
			factory.sendNpcChat(6628, "Tsssk. Next time you enter those caves,",
					"human, my father will be having words.");
			factory.sendPlayerChat("Maybe next time I'll add your brothers to my collection.");
			factory.execute();
		}
	},
	CHAOS("Chaos elemental", 11995, 2055) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Is it true a level 3 skiller caught one of your siblings?");
			factory.sendNpcChat(2055, "Yes, they killed my mummy, kidnapped my brother,",
					"smiled about it and went to sleep.");
			factory.sendPlayerChat("Aww, well you have me now! I shall call you",
					"Squishy and you shall be mine and you shall be my Squishy");
			factory.sendPlayerChat("Come on, Squishy come on, little Squishy!");
			factory.execute();
		}
	},
	KRAKEN("Kraken", 12655, 6640) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("What's Kraken?");
			factory.sendNpcChat(6640, "Not heard that one before.");
			factory.sendPlayerChat("How are you actually walking on land?");
			factory.sendNpcChat(6640, "We have another leg, just below the center of",
					"our body that we use to move across solid surfaces.");
			factory.sendPlayerChat("That's.... interesting.");
			factory.sendNpcChat(6640, "Please, let me go back to my duties now...");
			factory.execute();
		}
	},
	CALLISTO("Callisto cub", 13178, 5558) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Why the grizzly face?");
			factory.sendNpcChat(5558, "You're not funny...");
			factory.sendPlayerChat("You should get in the.... sun more.");
			factory.sendNpcChat(5558, "You're really not funny...");
			factory.sendPlayerChat("One second, let me take a picture of you", "with my.... kodiak camera.");
			factory.sendNpcChat(5558, " .....");
			factory.sendPlayerChat("Feeling.... blue.");
			factory.sendNpcChat(5558, "If you don't stop, I'm going to leave", "some... brown... at your feet, human.");
			factory.execute();
		}
	},
	MOLE("Baby mole", 12646, 6651) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hey, Mole. How is life above ground?");
			factory.sendNpcChat(6651, "Well, the last time I was above ground,",
					"I was having to contend with people throwing", "snow at some weird yellow duck in my park.");
			factory.sendPlayerChat("Why were they doing that?");
			factory.sendNpcChat(6651, "No idea, I didn't stop to ask as an angry",
					"mob was closing in on them pretty quickly.");
			factory.sendPlayerChat("Sounds awful.");
			factory.sendNpcChat(6651, "Anyway, keep Molin'!");
			factory.execute();
		}
	},
	ZULRAH_GREEN("Snakeling", 12921, 2127) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hey little snake!");
			factory.sendNpcChat(2127, "Soon, Zulrah shall establish dominion over this plane.");
			factory.sendPlayerChat("Wanna play fetch?");
			factory.sendNpcChat(2127, "Submit to the almighty Zulrah.");
			factory.sendPlayerChat("Walkies? Or slidies...?");
			factory.sendNpcChat(2127, "Zulrah's wilderness as a God will soon be demonstrated.");
			factory.sendPlayerChat("I give up...");
			factory.execute();
		}
	},
	ZULRAH_RED("Snakeling", 12939, 2128) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hey little snake!");
			factory.sendNpcChat(2128, "Soon, Zulrah shall establish dominion over this plane.");
			factory.sendPlayerChat("Wanna play fetch?");
			factory.sendNpcChat(2128, "Submit to the almighty Zulrah.");
			factory.sendPlayerChat("Walkies? Or slidies...?");
			factory.sendNpcChat(2128, "Zulrah's wilderness as a God will soon be demonstrated.");
			factory.sendPlayerChat("I give up...");
			factory.execute();
		}
	},
	ZULRAH_BLUE("Snakeling", 12940, 2129) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Hey little snake!");
			factory.sendNpcChat(2129, "Soon, Zulrah shall establish dominion over this plane.");
			factory.sendPlayerChat("Wanna play fetch?");
			factory.sendNpcChat(2129, "Submit to the almighty Zulrah.");
			factory.sendPlayerChat("Walkies? Or slidies...?");
			factory.sendNpcChat(2129, "Zulrah's wilderness as a God will soon be demonstrated.");
			factory.sendPlayerChat("I give up...");
			factory.execute();
		}
	},
	KAL_PRINCESS("Kalphite Princess", 12654, 6637) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("What is it with your kind and potato cactus?");
			factory.sendNpcChat(6637, "Truthfully?");
			factory.sendPlayerChat("Yeah, please.");
			factory.sendNpcChat(6637, "Soup. We make a fine soup with it.");
			factory.sendPlayerChat("Kalphites can cook?");
			factory.sendNpcChat(6637, "Nah, we just collect it and put it there",
					"because we know fools like yourself will come",
					"down looking for it then inevitably be killed by my mother.");
			factory.sendPlayerChat("Evidently not, that's how I got you!");
			factory.sendNpcChat(6637, "Touché");
			factory.execute();
		}
	},
	VETION("Vet'ion jr.", 13179, 5560) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Who is the true lord and king of the lands?");
			factory.sendNpcChat(5560, "The mighty heir and lord of the Wilderness.");
			factory.sendPlayerChat("Where is he? Why hasn't he lifted your burden?");
			factory.sendNpcChat(5560, "I have not fulfilled my purpose.");
			factory.sendPlayerChat("What is your purpose?");
			factory.sendNpcChat(5560, "Not what is, what was. A great war tore this",
					"land apart and, for my failings in protecting", "this land, I carry the burden of its waste.");
			factory.execute();

		}
	},
	VENENATIS("Venenatis spiderling", 13177, 5557) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("It's a damn good thing I don't have arachnophobia.");
			factory.sendNpcChat(5557, "We're misunderstood. Without us in your house,",
					"you'd be infested with flies and other REAL nasties.");
			factory.sendPlayerChat("Thanks for that enlightening fact.");
			factory.sendNpcChat(5557, "Everybody gets one.");
			factory.execute();

		}
	},
	SMOKE_DEV("Smoke devil", 12648, 6655) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Your kind comes in three different sizes?");
			factory.sendNpcChat(6655, "Four, actually.");
			factory.sendPlayerChat("Wow. Whoever created you wasn't very creative.",
					"You're just resized versions of one another!");
			factory.execute();

		}
	},
	SCORPIA("Scorpia's offspring", 13181, 5547) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("At night time, if I were to hold ultraviolet", "light over you, would you glow?");
			factory.sendNpcChat(5547, "Two things wrong there, human.");
			factory.sendPlayerChat("Oh?");
			factory.sendNpcChat(5547, "One, When has it ever been night time here?");
			factory.sendNpcChat(5547, "Two, When have you ever seen ultraviolet light around here?");
			factory.sendPlayerChat("Hm...");
			factory.sendNpcChat(5547, "In answer to your question though.", "Yes I, like every scorpion, would glow.");
			factory.execute();

		}
	},
	CORP("Dark Core", 12816, 318) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Got any sigils for me?");
			factory.sendStatement("The Core shakes its head.");
			factory.sendPlayerChat("Damnit Core-al!");
			factory.sendPlayerChat("Let's bounce!");
			factory.execute();

		}
	},
	JAD("Tzrek-jad", 13225, 5892) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Do you miss your people?");
			factory.sendNpcChat(5892, "Mej-TzTok-Jad Kot-Kl!", "(TzTok-Jad will protect us!)");
			factory.sendPlayerChat("No.. I don't think so.");
			factory.sendNpcChat(5892, "Jal-Zek Kl? (Foreigner hurt us?)");
			factory.sendPlayerChat("No, no, I wouldn't hurt you.");
			factory.execute();

		}
	},
	ROCK_GOLEM("Rock golem", 13321, 2182) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("So you're made entirely of rocks?");
			factory.sendNpcChat(6716, "Not quite, my body is formed mostly of minerals.");
			factory.sendPlayerChat("Aren't minerals just rocks?");
			factory.sendNpcChat(6716, "No, rocks are rocks, minerals are minerals.", "I am formed from minerals.");
			factory.sendPlayerChat("But you're a Rock Golem...");
			factory.execute();

		}
	},
	HERON("Heron", 13320, 6715) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendNpcChat(6715, "Hop inside my mouth if you want to live!");
			factory.sendPlayerChat("I'm not falling for that... I'm not a fish!", "I've got more foresight than that.");
			factory.execute();

		}
	},
	BABY_CHINCHOMPA("Baby chinchompa", 13323, 6718) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendNpcChat(6718, "Squeak squeak!");
			factory.execute();

		}
	},
	GIANT_SQUIRREL("Giant squirrel", 20659, 7334) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("So how come you are so agile?");
			factory.sendNpcChat(7334, "If you were so nutty about nuts, maybe you",
					"would understand the great lengths we go to!");
			factory.execute();

		}
	},
	TANGLEROOT("Tangleroot", 20661, 7335) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("How are you doing today?");
			factory.sendNpcChat(7335, "I am Tangleroot!");
			factory.execute();

		}
	},
	ROCKY("Rocky", 20663, 7336) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("*Whistles*");
			factory.sendStatement("You slip your hand into Rocky's pocket.");
			factory.sendNpcChat(7336, "OY!! You're going to have to do", "better than that! Sheesh, what an amateur.");
			factory.execute();

		}
	},
	RIFT_GUARDIAN("Rift guardian", 20665, 7337) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("Can you see your own rift?");
			factory.sendNpcChat(7337, "No. From time to time I feel it shift and",
					"change inside me though. It is an odd feeling.");
			factory.execute();

		}
	},
	BEAVER("Beaver", 13322, 6717) {
		@Override
		public void dialogue(DialogueFactory factory) {
			factory.sendPlayerChat("How much wood could a woodchuck chuck if a", "woodchuck could chuck wood?");
			factory.sendNpcChat(6717, "Approximately 32,768 depending on", "his woodcutting level.");
			factory.execute();
		}
	};

	/** The name of the pet. */
	private final String name;

	/** The item identification of the pet. */
	private final int item;

	/** The npc identification of the fucking faggot pet. */
	private final int npc;

	/** The dialogue of the pet. */
	public abstract void dialogue(DialogueFactory factory);

	/** Constructs a new <code>PetData<code>. */
	PetData(String name, int item, int npc) {
		this.item = item;
		this.npc = npc;
		this.name = name;
	}

	/** Gets the name of the pet. */
	public String getName() {
		return name;
	}

	/** Gets the item identification of the pet. */
	public int getItem() {
		return item;
	}

	/** Gets the npc identification of the pet. */
	public int getNpc() {
		return npc;
	}

	/** Gets the pet data based on the given item identification. */
	public static Optional<PetData> forItem(int id) {
		return Arrays.stream(values()).filter(a -> a.item == id).findAny();
	}

	/** Gets the pet data based on the given npc identification. */
	public static Optional<PetData> forNpc(int id) {
		return Arrays.stream(values()).filter(a -> a.npc == id).findAny();
	}

	/** Gets the pet data based on the given ordinal. */
	public static Optional<PetData> forOrdinal(int ordinal) {
		return Arrays.stream(values()).filter(pet -> pet.ordinal() == ordinal).findFirst();
	}
}
