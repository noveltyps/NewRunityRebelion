package io.server.content.bosspoints;

import io.server.game.world.entity.mob.player.Player;

public class BossPoints {

	/** getters and setters, to access skilling points and set them. **/

	int bossPoints;
	//private Player player;

	public void bossPoints(Player player) {
		//this.player = player;
		bossPoints++;
	}

	public int getBossPoints() {
		return bossPoints;
	}

	public void setBossPoints(int bossPoints) {
		this.bossPoints = bossPoints;
	}

	/** increments skilling points. **/
	public static void incrementSkillingPoints(Player player, int bossPoints, Object object) {
		player.skillingPoints++;
	}

}
