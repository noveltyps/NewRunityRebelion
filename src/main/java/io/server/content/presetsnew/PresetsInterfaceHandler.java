package io.server.content.presetsnew;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;

public class PresetsInterfaceHandler {
	
	protected Player player;
	
	public PresetsInterfaceHandler(Player player) {
		this.player = player;
	}
	
	public void open() {
		 sendStrings();
		 player.interfaceManager.open(42500);
	}
	
	public void sendStrings() {
		//sec
		int stringid = 42540;
		player.send(new SendString("126 Melee", stringid++));	
		player.send(new SendString("126 Hybrid", stringid++));	
		player.send(new SendString("126 Tribid", stringid++));	
		player.send(new SendString("Pure", stringid++));	
		player.send(new SendString("Pure tribrid", stringid++));	
		player.send(new SendString("anged rank", stringid++));	 ///DW
		player.send(new SendString("F2P", stringid++));	
		player.send(new SendString("Galvek Gear", stringid++));	
		player.send(new SendString("Misc runes", stringid++));	
		}
}
