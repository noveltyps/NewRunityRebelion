package io.server.content.freeforall.impl;

import io.server.content.freeforall.FreeForAll;
import io.server.content.freeforall.FreeForAllType;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendFadeScreen;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendMinimapState;
import io.server.net.packet.out.SendMinimapState.MinimapState;

public class FreeForAllLobbyTask {

	protected Player player;

	public FreeForAllLobbyTask(Player player) {
		this.player = player;
	}

	private Position[] location = {new Position(3323, 4948, 0), new Position(3323, 4970, 0)};
	
	public void execute() {
		if (!getRestrictions()) {
			return;
		}

		player.send(new SendFadeScreen("You are teleporting to Free For All!", 1, 4));
		player.send(new SendMinimapState(MinimapState.HIDDEN));
		World.schedule(4, () -> {
			Teleportation.teleport(player, new Position(3292, 4958, 0));
			player.send(new SendMinimapState(MinimapState.NORMAL));
			player.send(new SendMessage("Welcome to the Free For All lobby!"));
			FreeForAll.KEY_MAP.putIfAbsent(player, FreeForAllType.LOBBY);
			FreeForAll.lobbyCount++;
			player.interfaceManager.openWalkable(23111);

		});

	}

	private boolean getRestrictions() {
		if (FreeForAll.gameStarted) {
			player.send(new SendMessage("@red@ There is currently a game running!"));
			return false;
		}
		if (player.inventory.getFreeSlots() < 28) {
			player.send(new SendMessage("@red@ You are not allowed to bring in any items!"));
			return false;
		}
		if (player.equipment.getFreeSlots() < 14) {
			player.send(new SendMessage("@red@ You are not allowed to bring in any equipment!"));
			return false;
		}
		return true;
	}
}
