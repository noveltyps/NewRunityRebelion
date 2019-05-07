package io.server.game.task.impl;

import io.server.game.task.Task;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class SuperAntipoisonTask extends Task {
	private final Player player;

	public SuperAntipoisonTask(Player player) {
		super(false, 50);
		this.player = player;
	}

	@Override
	public void execute() {
		if (player.getPoisonImmunity().get() <= 0)
			this.cancel();

		if (player.getPoisonImmunity().decrementAndGet(50) <= 50)
			player.send(new SendMessage("Your resistance to poison is about to wear off!"));

		if (player.getPoisonImmunity().get() <= 0)
			cancel();
	}

	@Override
	protected void onCancel(boolean logout) {
		player.send(new SendMessage("Your resistance to poison has worn off!"));
		player.getPoisonImmunity().set(0);
	}

}
