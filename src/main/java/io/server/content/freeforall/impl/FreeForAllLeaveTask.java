package io.server.content.freeforall.impl;

import io.server.Config;
import io.server.content.freeforall.FreeForAll;
import io.server.content.freeforall.FreeForAllType;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;

public class FreeForAllLeaveTask {

	private Player player;
	private String type;

	public FreeForAllLeaveTask(Player player, String type) {
		this.player = player;
		this.type = type;
	}

	public void execute() {

		switch (type) {

		case "lobby":
			FreeForAll.lobbyCount--;
			FreeForAll.KEY_MAP.remove(player, FreeForAllType.LOBBY);
			reset();
			Teleportation.teleport(player, Config.DEFAULT_POSITION);
			player.send(new SendMessage("You have left the Free For All lobby!"));
			player.interfaceManager.clean(true);
			break;
		case "game":
			player.send(new SendMessage("You can't do this right now!"));
			break;
		case "logout":
			FreeForAll.KEY_MAP.remove(player);
			reset();
			Teleportation.teleport(player, Config.DEFAULT_POSITION);
			player.interfaceManager.clean(true);
			break;
		case "death":
			FreeForAll.gameCount--;
			FreeForAll.KEY_MAP.remove(player, FreeForAllType.GAME);
			reset();
			Teleportation.teleport(player, Config.DEFAULT_POSITION);
			player.send(new SendMessage("You have lost the Free For All game!"));
			player.interfaceManager.clean(true);
			break;
		case "end":
			FreeForAll.KEY_MAP.remove(player, FreeForAllType.GAME);
			reset();
			FreeForAll.gameCount--;
			Teleportation.teleport(player, Config.DEFAULT_POSITION);
			player.send(new SendMessage("You have won the free for all game!"));
			World.sendMessage("[Free For All] " + player.getUsername() + " has won the free for all game!");
			player.inventory.add(new Item(995, 10000));
			FreeForAll.gameStarted = false;
			player.interfaceManager.clean(true);
			break;
		}
	}

	private void reset() {
		player.equipment.clear();
		player.inventory.clear();
		player.equipment.refresh();
		player.inventory.refresh();
	}
}
