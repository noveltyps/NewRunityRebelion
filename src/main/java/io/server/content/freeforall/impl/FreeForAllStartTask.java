package io.server.content.freeforall.impl;

import io.server.content.freeforall.FreeForAll;
import io.server.content.freeforall.FreeForAllType;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerOption;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendPlayerOption;

public class FreeForAllStartTask implements FreeForAllTask {

	@Override
	public void execute(Player player, String content) {
		System.out.println("Start Task Executed");
		FreeForAll.KEY_MAP.replace(player, FreeForAllType.LOBBY, FreeForAllType.GAME);
		FreeForAll.lobbyCount--; FreeForAll.gameCount++;
		
		Teleportation.teleport(player, new Position(3292, 4958, 0));
		
		player.equipment.manualWearAll(FreeForAll.gear.getEquipment());
		
		for(int i = 0; i < FreeForAll.gear.getInventory().length; i++) {
			player.inventory.add(FreeForAll.gear.getInventory()[i]);
		}

		player.spellbook = FreeForAll.gear.getSpellBook();
		
		player.equipment.refresh();
		player.inventory.refresh();
		
		player.send(new SendPlayerOption(PlayerOption.ATTACK, true));
		
	}

}