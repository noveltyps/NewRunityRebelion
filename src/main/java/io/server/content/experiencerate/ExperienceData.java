package io.server.content.experiencerate;

import io.server.game.world.entity.mob.player.PlayerRight;

/***
 * Experience rate modifier
 * 
 * @author Nerik#8690
 *
 */
public enum ExperienceData {

	NORMAL(PlayerRight.PLAYER, 17.0), 
	HELPER(PlayerRight.HELPER, 17.0), MODERATOR(PlayerRight.MODERATOR, 17.0),
	ADMINISTRATOR(PlayerRight.ADMINISTRATOR, 17.0), DEVELOPER(PlayerRight.DEVELOPER, 17.0), OWNER(PlayerRight.OWNER, 17.0),

	DONATOR(PlayerRight.DONATOR, 17.0), SUPER_DONATOR(PlayerRight.SUPER_DONATOR, 17.0),
	EXTREME_DONATOR(PlayerRight.EXTREME_DONATOR, 17.0), ELITE_DONATOR(PlayerRight.ELITE_DONATOR, 17.0),
	KING_DONATOR(PlayerRight.KING_DONATOR, 17.0), SUPREME_DONATOR(PlayerRight.SUPREME_DONATOR, 17.0),
	YOUTUBER(PlayerRight.YOUTUBER, 17.0), TRUSTED_GAMBLER(PlayerRight.TRUSTED_GAMBLER, 17.0),
	DONATION_MANAGER(PlayerRight.DONATION_MANAGER, 17.0),

	IRONMAN(PlayerRight.IRONMAN, 8.0), ULTIMATE_IRONMAN(PlayerRight.ULTIMATE_IRONMAN, 5.5),
	HARDCORE_IRONMAN(PlayerRight.HARDCORE_IRONMAN, 5.0), CLASSIC(PlayerRight.CLASSIC, 4.0);

	private PlayerRight mode;
	private double modifier;

	ExperienceData(PlayerRight mode, double modifier) {
		this.mode = mode;
		this.modifier = modifier;
	}

	public PlayerRight getMode() {
		return mode;
	}

	public double getModifier() {
		return modifier;
	}
}
