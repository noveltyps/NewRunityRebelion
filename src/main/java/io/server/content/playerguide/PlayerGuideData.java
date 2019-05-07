package io.server.content.playerguide;

/**
 * Stores the player guide data
 *
 * @author Nerik#8690 & Herb#7084 
 *
 */

public enum PlayerGuideData {

	Introduction(-9485, PlayerGuideDifficulty.EASY, "Introduction",
			new String[] { "If you need any information,", "based on BrutalOS, select one of the options"," shown in our navigation bar",
					"on the left hand side", "", "I will then give you information on",
					"the topic within this section.", }),

	Economy_Information(-9484, PlayerGuideDifficulty.EASY, "Main Currency?",
			new String[] { "Our economy is built by the players,", "We have introduced 1Bill and 500 Mill Tickets.",
					"Bosses mainly drop these tickets", "They can also be obtained from Mystery boxes",
					"From donating and from voting.", "They can be spent in various shops ", "all over BrutalOS",
					"such as the Rare Store and Customs store", "& PVM & Misc Items Store!", "", "", "", "", "", "", "",
					"", "" }),

	Money_Making(-9483, PlayerGuideDifficulty.EASY, "Making Bank?", 
			new String[] { "There are various ways in which a,", "player can obtain money on BrutalOS.",
			"One of most effective ways is PvM'ing", "& Skilling.", "BrutalOS has over 30+ Bosses.",
			"NPC Drops can be found by using ::drops", "teleports can be accessed with the","teleport button under the minimap.",
			"Customs can be obtained from these bosses,", "aswell as Mystery boxes."}),

	Voting(-9482, PlayerGuideDifficulty.EASY, "Voting",
			new String[] { "Use ::vote to vote for us",
					"Voting for our server will increase", "the amount of players on BrutalOS.", "",
					"Voting will also allow you to recieve vote", "points which can use to obtain various,",
					"items from the voting store!.", "", "", "", "", "", "", "", "", "" }),

	Achievements(-9481, PlayerGuideDifficulty.EASY, "Achievements",
			new String[] { "Achievements are completed by ", 
					"various types of activity.", "You may view the list by",
					"opening the tab next to your inventory.", "Click achievements to open the list.", 
					"Each achievements will list the task, your",
					"progression and the reward points", "when completed.", "", "", "", "", "", "", "", "" }),

	Player_Functions(-9480, PlayerGuideDifficulty.EASY, "Player Functions",
			new String[] { "You may interact with other plyers.",
					"Functions such as trading, dueling, folowing", "and messaging players are most common.",
					"Other functions include clan chats,", "useful when teaming up with players " , "for events such as the",
					"Wilderness, bossing, minigames and more.", "", "", "", "", "", "", "", "", "" }),

	Teleporting(-9479, PlayerGuideDifficulty.EASY, "Teleporting",
			new String[] { "Use the teleport button to open up", "the interface where we have listed", " all available teleports our interface", "", "", "", "",
					"", "", "", "", "" }),

	Shops(-9478, PlayerGuideDifficulty.EASY, "Shops",
			new String[] { "Shops are located north west" ,"of Edgeville bank. You may", "teleport there using ::shops",
					"", "You will find most Npcs you would need at the" ,"::Shops area.",
					"Be sure to right click the Npcs", "and fully explore their options!", "", "", "", "", "", "", "",
					"", "" }),

	Gambling(-9477, PlayerGuideDifficulty.EASY, "Gambling",
			new String[] { "Gambling is located at ::gamble.", "", "", "", "", "", "", "", "",
					"" }),

	Commands(-9476, PlayerGuideDifficulty.EASY, "Commands",
			new String[] { "You may view the entire command list", "by typing in ::commands.", "", "", "", "", "", "",
					"", "", "" }),

	Donate(-9475, PlayerGuideDifficulty.EASY, "Donate",
			new String[] { "You can help support BrutalOS ",
					"and help it grow by donating.", "You will be rewarded alot of wealth.", "There are also tonnes of perks", "for Donators!",
					"Type in ::store to view the full list of perks!", "", "", "", "", "", "", "", "", "" }),

	Help_me(-9474, PlayerGuideDifficulty.EASY, "Help me",
			new String[] { "- Contact a staff member on ::staff", "- Ask a question in the BrutalOS clan chat for",
					"other veteran players to answer you.", "- Contat a staff member on ::discord",
					"- Ask for help on the ::forums", "", "", "", "", "", "", "", "", "" }),
	
	Beginner_Guide(-9473, PlayerGuideDifficulty.EASY, "Beginner Guide",
			new String[] { "1# Check out Forums/Discord for any guides" ,"2# Check out shops and the home area",
					"", "3# Ask for help in the clan chat" ,"4# Go train your stats at ::train , ::mediumzone",
					"5# Checkout ::Drops  and ::simulator", "6# If you want to upgrade your weapons and advance make sure to use the upgrade system in home (nieve)", "", "", "", "", "", "", "",
					"", "" });




	private int buttonId;
	private PlayerGuideDifficulty difficulty;
	private String title;
	private String[] content;

	PlayerGuideData(int buttonId, PlayerGuideDifficulty difficulty, String title, String[] content) {
		this.buttonId = buttonId;
		this.difficulty = difficulty;
		this.title = title;
		this.content = content;
	}

	public int getButtonId() {
		return buttonId;
	}

	public PlayerGuideDifficulty getDifficulty() {
		return difficulty;
	}

	public String getTitle() {
		return title;
	}

	public String[] getContent() {
		return content;
	}

}