package io.server.content.activity.impl.battlerealm;

import static io.server.content.activity.impl.battlerealm.BattleRealm.LOBBY_COOLDOWN;

import io.server.content.activity.panel.Activity_Panel;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.util.Utility;

class BattleRealmPanel extends Activity_Panel {

	private BattleRealm BattleRealm;

	BattleRealmPanel(BattleRealm BattleRealm, Player player) {
		super(player, "BattleRealm");
		this.BattleRealm = BattleRealm;
	}

	public void update(BattleRealmNode node) {
		if (BattleRealm.lobby) {
			set(0, "Next Departure: <col=FF5500>" + Utility.getTime(BattleRealm.getTicks()) + "</col>");
			set(1, "Players Ready: <col=FF5500>" + BattleRealm.getActiveSize() + "</col>");
			set(2, "(Need <col=FF5500>3</col> to 25 players)");
			set(3, "Points: <col=FF5500>0</col>");
			set(4, "Team: " + node.getTeamAsString());
			setFooter("Minigame Launching");
			setProgress((int) Utility.getPercentageAmount(LOBBY_COOLDOWN - BattleRealm.getTicks(), LOBBY_COOLDOWN));
		} else {
			/*
			 * set(0, "Time Remaining: <col=FF5500>" +
			 * Utility.getTime(BattleRealm.getTicks()) + "</col>"); set(1,
			 * "You're in a game!!: <col=FF5500>" + "</col>"); set(2, "Damage: <col=FF5500>"
			 * + node.damage + "</col>");
			 */
			int dead = 0;

			set(0, "Team: " + node.getTeamAsString());
			set(1, "Team Lives: " + node.getTeamLives());
			/*
			 * for (int index = 1; index <= 3; index++) { String value = "dead"; value =
			 * "wheee"; set(index, "hi: <col=FF5500>" + value + "</col>"); }
			 */
			setFooter("Minigame Completion:");
			setProgress((int) Utility.getPercentageAmount(dead, 4));
		}
		setItem(new Item(11666));
	}
}
