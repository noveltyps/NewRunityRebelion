package io.server.content.runeliteplugin;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;

public class RuneliteSkillingOverlay {
	
	protected Player player;
	
	public RuneliteSkillingOverlay(Player player) {
		this.player = player;
	}

	public void onOpen(int randomAmount, String skillName, String action) {
		for(int i = 15207; i < 15209; i++) {
			player.send(new SendString("", i));
			System.err.println("Reset Frames: " + i);
		}
		player.send(new SendString("1", 15205));
		player.send(new SendString("You are " + skillName, 15207));
		player.send(new SendString(action + "/hr: " + randomAmount, 15209));
	}

	public void onClose() {
		player.send(new SendString("0", 15205));
	}

	public void onUpdate(int currently, String action) {
		player.send(new SendString(action + ": " + currently, 15208));
	}

}
