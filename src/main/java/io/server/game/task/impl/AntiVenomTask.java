package io.server.game.task.impl;

import io.server.game.task.Task;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class AntiVenomTask extends Task {
	private final Player player;

	public AntiVenomTask(Player player) {
		super(false, 50);
		this.player = player;
	}

	@Override
	public void execute() {
		if (player.getVenomImmunity().get() <= 0)
			this.cancel();

		if (player.getVenomImmunity().decrementAndGet(50) <= 50)
			player.send(new SendMessage("Your resistance to venom is about to wear off!"));

		if (player.getVenomImmunity().get() <= 0)
			cancel();
	}

	@Override
	protected void onCancel(boolean logout) {
		player.send(new SendMessage("Your resistance to venom has worn off!"));
		player.getVenomImmunity().set(0);
	}

}
