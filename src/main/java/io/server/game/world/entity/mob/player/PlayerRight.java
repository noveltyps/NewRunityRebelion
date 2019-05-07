package io.server.game.world.entity.mob.player;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the player right data.
 *
 * @author Daniel
 */

public enum PlayerRight {

	PLAYER("Player", "000000", 0, -1, 4111), 
    MODERATOR("Moderator", "245EFF", 1, -1, 4116),
    GLOBAL_MODERATOR("Global Moderator", "96039F", 1, -1, 4116),
    ADMINISTRATOR("Administrator", "D17417", 2, -1, 4116), 
    DEVELOPER("Developer", "4070FF", 4, -1, 4117),
    OWNER("Owner", "ED0C0C", 4, -1, 4117),
    

    DONATOR("Donator", "9C4B2F", 5, 10, 4112), 
    SUPER_DONATOR("Super Donator", "2F809C", 6, 35, 4112),
    EXTREME_DONATOR("Extreme Donator", "158A76", 7, 100, 4113), 
    ELITE_DONATOR("Elite Donator", "2CA395", 8, 250, 4114),
    KING_DONATOR("King Donator", "E32973", 9, 500, 4115), 
    SUPREME_DONATOR("Supreme Donator", "E30b1A", 10, 750, 4115),

    CLASSIC("Classic", "B1800A", -1, -1, 4115), 
    YOUTUBER("Youtuber", "91111A", 11, -1, 4112),
    
    IRONMAN("Ironman", "7A6F74", 12, -1, 4112), 
    ULTIMATE_IRONMAN("Ultimate Ironman", "7A6F74", 13, -1, 4113),
    HARDCORE_IRONMAN("Hardcore Ironman", "7A6F74", 14, -1, 4114), 
    
    HELPER("Support", "C900FF", 16, -1, 4115),
    //GRAPHIC("Graphic", "CE795A", 17, -1, 4112),
    TRUSTED_GAMBLER("Trusted Gambler", "FFA500", 15, -1, 4117),
    DONATION_MANAGER("Manager", "69005A", 17, -1, 4114),
    GLOBAL_DONATOR("Global Donator", "FFFFFF", 3, 1000, 4117),
    
    // this makes sense? should work right?

    ;

	/** The rank name. */
	private final String name;

	/** The crown identification. */
	private final int crown;

	private final int moneyRequired;

	/** The rank color. */
	private final String color;

	/** The rank rest animation. */
	private final int restAnimation;

	/** Constructs a new <code>PlayerRight</code>. */
	PlayerRight(String name, String color, int crown, int moneyRequired, int restAnimation) {
		this.name = name;
		this.color = color;
		this.crown = crown;
		this.moneyRequired = moneyRequired;
		this.restAnimation = restAnimation;
	}

	public static Optional<PlayerRight> lookup(int id) {
		return Arrays.stream(values()).filter(it -> it.crown == id).findFirst();
	}

	public static PlayerRight getRankByName(String name) {
		return valueOf(name);
	}

	/** Checks if the player has developer status. */
	public static boolean isDeveloper(Player player) {
		return player.right.equals(OWNER) || player.right.equals(DEVELOPER) || player.right.equals(DONATION_MANAGER);
	}
	
	public static boolean isPlayer(Player player) {
		return player.right.equals(PLAYER);
	}
	
	/** Checks if the player has developer status. */
/*	public static boolean isGambleManager(Player player) {
		return player.right.equals(GAMBLE_MANAGER) || player.right.equals(GAMBLE_MANAGER) || player.right.equals(DONATION_MANAGER);
	}*/
	

	
	public static boolean isBugTester(Player player) {
		return player.right.equals(GLOBAL_DONATOR) || player.right.equals(GLOBAL_DONATOR);
	}
	/** Checks if the player is a privileged member. */
	public static boolean isPriviledged(Player player) {
		return isDeveloper(player) || player.right.equals(ADMINISTRATOR);
	}

	/** Checks if the player is a management member. */
	public static boolean isManagement(Player player) {
		return isPriviledged(player) || player.right.equals(MODERATOR) || player.right.equals(GLOBAL_MODERATOR) || player.right.equals(HELPER) || player.right.equals(ADMINISTRATOR) 
				;
	}
	
	/** Checks if the player is a Youtuber member. */
	public static boolean isYoutuber(Player player) {
		return player.right.equals(YOUTUBER);
	}


	/** Checks if the player has donator status. */
	public static boolean isDonator(Player player) {
		return isManagement(player)  || isYoutuber(player) || player.donation.getSpent() >= DONATOR.getMoneyRequired();
	}

	/** Checks if the player has super donator status. */
	public static boolean isSuper(Player player) {
		return isManagement(player) || player.donation.getSpent() >= SUPER_DONATOR.getMoneyRequired();
	}

	/** Checks if the player has extreme donator status. */
	public static boolean isExtreme(Player player) {
		return isManagement(player) || player.donation.getSpent() >= EXTREME_DONATOR.getMoneyRequired();
	}

	/** Checks if the player has elite donator status. */
	public static boolean isElite(Player player) {
		return isManagement(player) || player.donation.getSpent() >= ELITE_DONATOR.getMoneyRequired();
	}

	/** Checks if the player has king donator status. */
	public static boolean isKing(Player player) {
		return isManagement(player) || player.donation.getSpent() >= KING_DONATOR.getMoneyRequired();
	}
	
	/** Checks if the player has Supreme donator status. */
	public static boolean isSupreme(Player player) {
		return isManagement(player) || player.donation.getSpent() >= SUPREME_DONATOR.getMoneyRequired();
	}
	
	public static boolean isGlobal(Player player) {
		return isManagement(player) || player.donation.getSpent() >= GLOBAL_DONATOR.getMoneyRequired();
	}

	/** Checks if the player is an ironman. */
	public static boolean isIronman(Player player) {
		return player.right.equals(IRONMAN) || player.right.equals(ULTIMATE_IRONMAN)
				|| player.right.equals(HARDCORE_IRONMAN);
	}

	/** Gets the crown display. */
	public static String getCrown(Player player) {
		return player.right.equals(PLAYER) ? "" : "<img=" + (player.right.getCrown() - 1) + ">";
	}

	public String getCrownText() {
		return this == PLAYER ? "" : "<img=" + (crown - 1) + ">";
	}
	
	//private static ImmutableList<PlayerRight> VALUES = ImmutableList.of(DONATOR);

	public static int getForumGroupId(PlayerRight right) {
		switch (right) {

		case DONATOR:
			return 10;

		case SUPER_DONATOR:
			return 14;

		case EXTREME_DONATOR:
			return 11;

		case ELITE_DONATOR:
			return 12;

		case KING_DONATOR:
			return 16;

		case SUPREME_DONATOR:
			return 17;

		default:
			return -1;
		}
	}

	/** Gets the deposit amount. */
	public static int getDepositAmount(Player player) {
		if (isKing(player))
			return 28;
		else if (isElite(player))
			return 20;
		else if (isExtreme(player))
			return 15;
		else if (isSuper(player))
			return 10;
		else if (isDonator(player))
			return 5;
		else if (isSupreme(player))
			return 28;
		return 1;
	}

	public static int getPresetAmount(Player player) {
		if (isKing(player))
			return 10;
		else if (isElite(player))
			return 9;
		else if (isExtreme(player))
			return 8;
		else if (isSuper(player))
			return 7;
		else if (isDonator(player))
			return 6;
		else if (isSupreme(player))
			return 10;
		return 5;
	}

	public static int getPkPoints(Player player) {
		if (isKing(player))
			return 15;
		else if (isElite(player))
			return 14;
		else if (isExtreme(player))
			return 13;
		else if (isSuper(player))
			return 12;
		else if (isDonator(player))
			return 11;
		else if (isSupreme(player))
			return 20;
		return 10;
	}

	public static PlayerRight forSpent(int spent) {
		if (spent >= PlayerRight.GLOBAL_DONATOR.getMoneyRequired())
			return PlayerRight.GLOBAL_DONATOR;
		if (spent >= PlayerRight.SUPREME_DONATOR.getMoneyRequired())
			return PlayerRight.SUPREME_DONATOR;
		if (spent >= PlayerRight.KING_DONATOR.getMoneyRequired())
			return PlayerRight.KING_DONATOR;
		if (spent >= PlayerRight.ELITE_DONATOR.getMoneyRequired())
			return PlayerRight.ELITE_DONATOR;
		if (spent >= PlayerRight.EXTREME_DONATOR.getMoneyRequired())
			return PlayerRight.EXTREME_DONATOR;
		if (spent >= PlayerRight.SUPER_DONATOR.getMoneyRequired())
			return PlayerRight.SUPER_DONATOR;
		if (spent >= PlayerRight.DONATOR.getMoneyRequired())
			return PlayerRight.DONATOR;
		return null;
	}

	/** Gets the name of the rank. */
	public String getName() {
		return name;
	}

	/** Gets the crown of the rank. */
	public int getCrown() {
		return crown;
	}

	public int getMoneyRequired() {
		return moneyRequired;
	}

	/** Gets the color of the rank. */
	public String getColor() {
		return color;
	}

	/** Gets the rest animation of the rank. */
	public int getRestAnimation() {
		return restAnimation;
	}

	public final boolean greater(PlayerRight other) {
		return ordinal() > other.ordinal();
	}

	public final boolean greaterOrEqual(PlayerRight other) {
		return ordinal() >= other.ordinal();
	}

	public final boolean less(PlayerRight other) {
		return ordinal() < other.ordinal();
	}

	public final boolean lessOrEqual(PlayerRight other) {
		return ordinal() <= other.ordinal();
	}

	public static boolean isGambleManager(Player player2) {
		// TODO Auto-generated method stub
		return false;
	}

}
