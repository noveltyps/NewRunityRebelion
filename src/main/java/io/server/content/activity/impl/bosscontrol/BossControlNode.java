package io.server.content.activity.impl.bosscontrol;

import io.server.content.ActivityLog;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.content.activity.panel.Activity_Panel;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Area;
import io.server.util.Utility;

/**
 * A {@code BossControlNode} handles pest control activity events for a specific
 * to player.
 */
final class BossControlNode extends Activity {

	private BossControl BossControl;
	/** The player that owns this node. */
	private final Player player;


	/** Constructs a new {@link BossControlNode} object for a player. */
	BossControlNode(BossControl BossControl, Player player) {
		super(1, BossControl.getInstance());
		this.BossControl = BossControl;
		this.player = player;
		player.activityDamage = 0;
	}

	@Override
	protected void start() {
		player.move(io.server.content.activity.impl.bosscontrol.BossControl.BOAT.transform(Utility.random(4), Utility.random(6)));
		player.dialogueFactory.sendNpcChat(1756, "Go with strength!", "Defend the void knight and destroy the portals!",
				"You are our only hope!").execute();
	}

	@Override
	public void finish() {
		getPanel().ifPresent(Activity_Panel::close);
		boolean portalsDead = BossControl.portalSet.isEmpty();
		boolean damageDone = player.activityDamage >= 50;
		if (!Area.inBossControlLobby(player))
			World.schedule(1, () -> {
				if (portalsDead && damageDone) {
					player.activityLogger.add(ActivityLog.BOSS_CONTROL);
					player.setpestPoints(player.getpestPoints() + 12);
					player.dialogueFactory.sendNpcChat(1756, "You have beaten the minigame!",
							"You were rewarded with " + player.pestPoints + " pest control points.",
							"You now have: " + player.pestPoints + ".").execute();
				} else if (!damageDone)
					player.dialogueFactory.sendNpcChat(1756, "You must deal at least 50 damage for a reward.", 
							"Quit trying to leach!").execute();
				else
					player.dialogueFactory.sendNpcChat(1756, "You have failed to kill all portals!", 
							"I am oh so disappointed in you.").execute();
			});
		player.move(io.server.content.activity.impl.bosscontrol.BossControl.END_POSITION);
		player.activityDamage = 0;
	}


	@Override
	public void cleanup() {
	}

	@Override
	public ActivityType getType() {
		return ActivityType.BOSS_CONTROL;
	}
}
