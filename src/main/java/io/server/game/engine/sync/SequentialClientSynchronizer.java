package io.server.game.engine.sync;

import io.server.game.engine.sync.task.NpcPostUpdateTask;
import io.server.game.engine.sync.task.NpcPreUpdateTask;
import io.server.game.engine.sync.task.NpcUpdateTask;
import io.server.game.engine.sync.task.PlayerPostUpdateTask;
import io.server.game.engine.sync.task.PlayerPreUpdateTask;
import io.server.game.engine.sync.task.PlayerUpdateTask;
import io.server.game.world.entity.MobList;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;

public final class SequentialClientSynchronizer implements ClientSynchronizer {

	@Override
	public void synchronize(MobList<Player> players, MobList<Npc> npcs) {
		players.forEach(player -> new PlayerPreUpdateTask(player).run());
		npcs.forEach(npc -> new NpcPreUpdateTask(npc).run());

		players.forEach(player -> new PlayerUpdateTask(player).run());
		players.forEach(player -> new NpcUpdateTask(player).run());

		players.forEach(player -> new PlayerPostUpdateTask(player).run());
		npcs.forEach(npc -> new NpcPostUpdateTask(npc).run());
	}

}
