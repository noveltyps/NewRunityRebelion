package io.server.content.activity.randomevent;

import io.server.content.activity.randomevent.impl.DrillDemonEvent;
import io.server.content.activity.randomevent.impl.GenieEvent;
import io.server.content.activity.randomevent.impl.MimeEvent;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Area;
import io.server.util.RandomUtils;
import io.server.util.Utility;

public class RandomEventHandler {

	public static void trigger(Player player) {
		if (Area.inWilderness(player))
			return;
		if (player.playerAssistant.busy())
			return;
		if (player.inActivity())
			return;
		if (!player.isVisible())
			return;

		int base = 200;

		if (Utility.random(base) != 0)
			return;

		int events = RandomUtils.inclusive(1, 3);

		switch (events) {
		case 1:
			GenieEvent.create(player);
			break;
		case 2:
			MimeEvent.create(player);
			break;
		case 3:
			DrillDemonEvent.create(player);
			break;
		}
	}
}
