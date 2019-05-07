package io.server.game.task.impl;

import io.server.content.clanchannel.ClanRepository;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;

public class PlayerSaveEvent extends Task {

	public PlayerSaveEvent() {
		super(150);
	}

	@Override
	public void execute() {
		for (Player player : World.getPlayers()) {
			if (player != null) {
				PlayerSerializer.save(player);
			}
		}
		ClanRepository.saveAllActiveClans();
	}
}
