package io.server.content.activity.impl.pestcontrol;

import io.server.content.activity.panel.Activity_Panel;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.util.Utility;

class PestControlPanel extends Activity_Panel {

	private PestControl pestControl;

	PestControlPanel(PestControl pestControl, Player player) {
		super(player, "Pest Control");
		this.pestControl = pestControl;
	}

	public void update(PestControlNode node) {
		if (pestControl.lobby) {
			set(0, "Next Departure: <col=FF5500>" + Utility.getTime(pestControl.getTicks()) + "</col>");
			set(1, "Players Ready: <col=FF5500>" + pestControl.getActiveSize() + "</col>");
			set(2, "(Need <col=FF5500>2</col> to 25 players)");
			set(3, "Points: <col=FF5500>0</col>");
			setFooter("Players Ready:");
			setProgress((int) Utility.getPercentageAmount(pestControl.getActiveSize(), PestControl.PLAYER_CAPACITY));
		} else {
			set(0, "Time remaining: <col=FF5500>" + Utility.getTime(pestControl.getTicks()) + "</col>");
			set(1, "Knight's health: <col=FF5500>" + pestControl.voidKnight.getCurrentHealth() + "</col>");
			set(2, "Damage: <col=FF5500>" + player.activityDamage + "</col>");
			int dead = 0;
			for (int index = 0; index <= 3; index++) {
				String value = "dead";
				if (pestControl.portals[index] != null && !pestControl.portals[index].isDead()) {
					value = "" + pestControl.portals[index].getCurrentHealth();
				} else {
					dead++;
				}
				set(3 + index, PestControl.PORTAL_NAMES[index] + ": <col=FF5500>" + value + "</col>");
			}
			setFooter("Minigame Completion:");
			setProgress((int) Utility.getPercentageAmount(dead, 4));
		}
		setItem(new Item(11666));
	}
}
