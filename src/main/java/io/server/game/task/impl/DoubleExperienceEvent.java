package io.server.game.task.impl;

import io.server.content.WellOfGoodwill;
import io.server.game.task.Task;
import io.server.game.world.World;

/**
 * An randomevent which starts double experience for 1 hour.
 *
 * @author Daniel | Obey
 */
public class DoubleExperienceEvent extends Task {

	public DoubleExperienceEvent() {
		super(100);
	}

	@Override
	public void execute() {
		if (!WellOfGoodwill.isActive()) {
			cancel();
			return;
		}

		WellOfGoodwill.activeTime++;

		if (WellOfGoodwill.activeTime == 15) {
			World.sendMessage("<col=2b58a0>WOG</col>: 15 Minutes left until the well is expired.");
		} else if (WellOfGoodwill.activeTime == 25) {
			World.sendMessage("<col=2b58a0>WOG</col>: 5 Minutes left until the well is expired.");
		} else if (WellOfGoodwill.activeTime == 30) {
			cancel();
		}
	}

	@Override
	protected void onCancel(boolean logout) {
		World.sendMessage("<col=2b58a0>WOG</col>: The well has expired and needs replenishing!");
		WellOfGoodwill.activeTime = -1;
		WellOfGoodwill.CONTRIBUTION = 0;
	}

}
