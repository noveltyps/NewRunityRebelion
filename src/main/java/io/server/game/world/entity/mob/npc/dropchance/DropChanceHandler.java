package io.server.game.world.entity.mob.npc.dropchance;


import io.server.Config;
import io.server.content.worldevent.WorldEvent;
import io.server.content.worldevent.WorldEventType;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

public class DropChanceHandler {

	public static int getRate(Player player) {
		int droprate = 0;
		for (int i = 0; i < player.equipment.getEquipment().length; i++) {
			if (player.equipment.getEquipment()[i] != null) {
				for (DropChanceData data : DropChanceData.values) {
					if (data.getItemId() == player.equipment.getEquipment()[i].getId()) {
						if (droprate >= 200) {
							droprate = 200;
						}
						droprate += data.getModifier();
					}
				}
			}
		}
		if (PlayerRight.isGlobal(player)) {
			droprate += 14;
		} else if (PlayerRight.isSupreme(player)) {
			droprate += 12;
		} else if (PlayerRight.isKing(player)) {
			droprate += 10;
		} else if (PlayerRight.isElite(player)) {
			droprate += 8;
		} else if (PlayerRight.isExtreme(player)) {
			droprate += 6;
		} else if (PlayerRight.isSuper(player)) {
			droprate += 4;
		} else if (PlayerRight.isDonator(player)) {
			droprate += 2;
		}
		
		WorldEvent dropRateBoost = World.eventHandler.getEvent(WorldEventType.DROP_RATE);
		
		if (dropRateBoost != null)
			droprate = (int) (droprate * (dropRateBoost.getModifier() / 100));
		
		WorldEvent event = World.eventHandler.getEvent(WorldEventType.DROP_RATE);
		
		if (event != null)
			droprate = (int) (droprate * event.getModifier());
		
		return droprate;
	}
}
