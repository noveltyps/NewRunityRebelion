package io.server.content.activity.impl.battlerealm;

import static io.server.content.activity.impl.battlerealm.Constants.END_ZONE;

import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.content.activity.panel.Activity_Panel;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * A {@code BattleRealmNode} handles pest control activity events for a specific
 * to player.
 */
public class BattleRealmNode extends Activity {

	private BattleRealm battleRealm;
	/** The player that owns this node. */
	private final Player player;

	protected int damage;
	public int team;

	/** Constructs a new {@link BattleRealmNode} object for a player. */
	BattleRealmNode(BattleRealm battleRealm, Player player) {
		super(1, battleRealm.getInstance());
		this.battleRealm = battleRealm;
		this.player = player;
		this.damage = 0;
		player.battleRealmNode = this;
	}

	@Override
	protected void start() {
		this.player.inBattleRealm = true;
		moveToSpawn();
	}

	public void moveToSpawn() {
		switch (team) {
		case 1:
			player.move(Constants.Morphic_SPAWN.transform(Utility.random(4), Utility.random(6)));
			System.out.println("You are on Morphic's team!");
			break;
		case 2:
			player.move(Constants.Realmite_SPAWN.transform(Utility.random(4), Utility.random(6)));
			System.out.println("You are on Realmite's team!");
			break;
		default:
			team = RandomUtils.inclusive(1, 2);
			start();
			break;
		}
	}

	@Override
	public void finish() {
		System.out.println("Running finish for " + player.getName());
		getPanel().ifPresent(Activity_Panel::close);
		this.player.inBattleRealm = false;
		player.move(END_ZONE);

		// if (damge == 0)

		/*
		 * if (BattleRealm.portalSet.isEmpty() && damge != 0 &&
		 * BattleRealm.monsters.isEmpty() ) {
		 * player.activityLogger.add(ActivityLog.PEST_CONTROL);
		 * player.setpestPoints(player.getpestPoints() + 3);
		 * player.dialogueFactory.sendNpcChat(1756, "You have beaten the minigame!",
		 * "You were rewarded with " + player.pestPoints + " pest control points.",
		 * "You now have: " + player.pestPoints + ".").execute(); } else {
		 * player.dialogueFactory.sendNpcChat(1756, "You have failed!",
		 * "I am oh so disappointed in you.").execute(); }
		 */
	}

	@Override
	public void cleanup() {
	}

	@Override
	public ActivityType getType() {
		return ActivityType.PEST_CONTROL;
	}

	public String getTeamAsString() {
		return team == 0 ? "None!" : team == 1 ? "Morphic" : "Realmite";
	}

	public int getTeamLives() {
		return team == 1 ? battleRealm.morphicLives : battleRealm.realmiteLives;
	}

	public void decrementTeamLives() {
		if (team == 1) {
			battleRealm.morphicLives--;
		} else {
			battleRealm.realmiteLives--;
		}
	}
}
