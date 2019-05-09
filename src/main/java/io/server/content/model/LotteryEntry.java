package io.server.content.model;

import io.server.game.world.entity.mob.player.Player;

public class LotteryEntry {
	
	private final Player player;
	private final int amount;
	
	public LotteryEntry(Player player, int amount) {
		this.player = player;
		this.amount = amount;
	}
	
	public Player getPlayer() { return this.player; }
	
	public int getAmount() { return this.amount; }

}
