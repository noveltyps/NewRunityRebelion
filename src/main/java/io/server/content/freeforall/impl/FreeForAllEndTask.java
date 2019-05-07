package io.server.content.freeforall.impl;

import io.server.content.freeforall.FreeForAll;
import io.server.content.freeforall.FreeForAllType;
import io.server.game.world.entity.mob.player.Player;

/**
 * Free For All End Task
 * @author Nerik#8690
 *
 */

public class FreeForAllEndTask implements FreeForAllTask {

	@Override
	public void execute(Player player, String content) {
		if (FreeForAll.gameCount == 1) {
			FreeForAll.KEY_MAP.keySet().forEach(mob -> {
				if (mob != null) {
					if (FreeForAll.KEY_MAP.get(mob).equals(FreeForAllType.GAME)) {
						new FreeForAllLeaveTask(mob, "end").execute();
					}
				}
			});
		}
	}
}
