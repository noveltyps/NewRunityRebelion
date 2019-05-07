package io.server.content.activity.impl.bosscontrol;

import io.server.content.activity.panel.Activity_Panel;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.util.Utility;

class BossControlPanel extends Activity_Panel {

	private BossControl BossControl;

	BossControlPanel(BossControl BossControl, Player player) {
		super(player, "Boss Control");
		this.BossControl = BossControl;
	}

	public void update(BossControlNode node) {
		if (BossControl.lobby) {
			set(0, "Next Departure: <col=FF5500>" + Utility.getTime(BossControl.getTicks()) + "</col>");
			set(1, "Players Ready: <col=FF5500>" + BossControl.getActiveSize() + "</col>");
			set(2, "(Need <col=FF5500>2</col> to 25 players)");
			set(3, "Points: <col=FF5500>0</col>");
			setFooter("Players Ready:");
			setProgress((int) Utility.getPercentageAmount(BossControl.getActiveSize(), io.server.content.activity.impl.bosscontrol.BossControl.PLAYER_CAPACITY));
		} else {
			set(0, "Time remaining: <col=FF5500>" + Utility.getTime(BossControl.getTicks()) + "</col>");
			set(1, "Knight's health: <col=FF5500>" + BossControl.voidKnight.getCurrentHealth() + "</col>");
			set(2, "Damage: <col=FF5500>" + player.activityDamage + "</col>");
			int dead = 0;
			for (int index = 0; index <= 3; index++) {
				String value = "dead";
				if (BossControl.portals[index] != null && !BossControl.portals[index].isDead()) {
					value = "" + BossControl.portals[index].getCurrentHealth();
				} else {
					dead++;
				}
				set(3 + index, io.server.content.activity.impl.bosscontrol.BossControl.PORTAL_NAMES[index] + ": <col=FF5500>" + value + "</col>");
			}
			setFooter("Minigame Completion:");
			setProgress((int) Utility.getPercentageAmount(dead, 4));
		}
		setItem(new Item(11666));
	}
}
