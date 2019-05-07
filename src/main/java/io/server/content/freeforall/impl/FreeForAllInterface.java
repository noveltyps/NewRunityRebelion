package io.server.content.freeforall.impl;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;

public class FreeForAllInterface implements FreeForAllTask {

	@Override
	public void execute(Player player, String content) {
		String[] parts = content.split(" ");
		String[] details = {"Timer: ", "State: ", "Players Ready: "};
		
		for(int i = 0; i < parts.length; i++) {
	 		player.send(new SendString(details[i] + parts[i], 23113 + i));
		}
	}

}
