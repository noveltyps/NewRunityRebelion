package io.server.content.tittle;

import java.util.Arrays;
import java.util.Optional;

import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementList;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.util.generic.BooleanInterface;

/**
 * Holds all the title data that can be redeemed using the title itemcontainer.
 *
 * @author Daniel
 */
public enum Title implements BooleanInterface<Player> {

	CHOPPA("Choppa", "Complete the achievement:", "Chop 5,000 trees") {
		@Override
		public boolean activated(Player player) {
			return AchievementHandler.completed(player, AchievementList.WOODCUTTING_III);
		}
	},
	SIR("Sir", "Complete the achievement:", "Kill 10 players") {
		@Override
		public boolean activated(Player player) {
			return AchievementHandler.completed(player, AchievementList.KILLER);
		}
	},
	WAR_CHIEF("War-chief", "Complete the achievement:", "Kill 1,000 players") {
		@Override
		public boolean activated(Player player) {
			return AchievementHandler.completed(player, AchievementList.KILLER_III);
		}
	},
	HOLY_FUCK("Holy F***", "Complete the achievement:", "Kill 10,000 players") {
		@Override
		public boolean activated(Player player) {
			return AchievementHandler.completed(player, AchievementList.KILLER_IV);
		}
	},

	MASTER("Master", "Achieve level 99 in all skills", "") {
		@Override
		public boolean activated(Player player) {
			return player.skills.isMaxed();
		}
	},
	GODLY("Godly", "Achieve 200M experience in all", "available skills") {
		@Override
		public boolean activated(Player player) {
			return AchievementHandler.completed(player, AchievementList.EXPERIENCE_MASTERY_II);
		}
	},
	THE_GREAT("The Great", "Complete all available achievements", "") {
		@Override
		public boolean activated(Player player) {
			return AchievementHandler.completedAll(player);
		}
	},
	IRONMAN("Ironman", "Be a iron man.", "") {
		@Override
		public boolean activated(Player player) {
			return PlayerRight.isIronman(player);
		}
	},
	DONATOR("Donator", "Be a regular donator", "") {
		@Override
		public boolean activated(Player player) {
			return PlayerRight.isDonator(player);
		}
	},
	SUPER("Super", "Be a regular super donator", "") {
		@Override
		public boolean activated(Player player) {
			return PlayerRight.isSuper(player);
		}
	},
	EXTREME("Extreme", "Be a regular extreme donator", "") {
		@Override
		public boolean activated(Player player) {
			return PlayerRight.isExtreme(player);
		}
	},
	ELITE("Elite", "Be a regular elite donator", "") {
		@Override
		public boolean activated(Player player) {
			return PlayerRight.isElite(player);
		}
	},
	BEAST("Beast", "Have more than 5000 Boss Points!", "") {
		@Override
		public boolean activated(Player player) {
			return player.bossPoints >= 5000;
		}
	},
	DOPE("Dope", "Be a regular dope donator", "") {
		@Override
		public boolean activated(Player player) {
			return PlayerRight.isKing(player);
		}
	},
	SUPREME("Supreme Leader!", "Be a Supreme donator", "") {
		@Override
		public boolean activated(Player player) {
			return PlayerRight.isSupreme(player);
		}
	};

	/**
	 * The name of the title
	 */
	private final String name;

	/**
	 * The requirement of the title
	 */
	private final String[] requirement;

	/**
	 * Constructs a new <code>Title</code>.
	 *
	 * @param name        The name of the title.
	 * @param requirement The title requirement.
	 */
	Title(String name, String... requirement) {
		this.name = name;
		this.requirement = requirement;
	}

	/**
	 * Returns the player title name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the player title requirements
	 */
	public String[] getRequirement() {
		return requirement;
	}

	/**
	 * Gets the player title data based on the ordinal
	 */
	public static Optional<Title> forOrdinal(int ordinal) {
		return Arrays.stream(values()).filter(a -> a.ordinal() == ordinal).findAny();
	}
}
