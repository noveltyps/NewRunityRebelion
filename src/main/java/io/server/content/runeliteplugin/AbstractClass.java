package io.server.content.runeliteplugin;

import io.server.game.world.entity.mob.player.Player;

public interface AbstractClass {
	
	public void onOpen(Player player);
	
	public void onClose(Player player);
	
	public void onUpdate(Player player);
}
