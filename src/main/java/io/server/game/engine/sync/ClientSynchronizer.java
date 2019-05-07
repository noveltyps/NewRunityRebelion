package io.server.game.engine.sync;

import io.server.game.world.entity.MobList;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;

public interface ClientSynchronizer {

	void synchronize(MobList<Player> players, MobList<Npc> npcs);

}
