package io.server.game.task.impl;

import java.util.concurrent.TimeUnit;

import io.server.content.activity.Activity;
import io.server.content.store.Store;
import io.server.game.task.TickableTask;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.util.Stopwatch;

public class PlayerRemovalTask extends TickableTask {

	private final Player player;
	private final Stopwatch stopwatch = Stopwatch.start();
	//private final boolean force;
	private boolean flag;

	public PlayerRemovalTask(Player player/*, boolean force*/) {
		super(true, 0);
		this.player = player;
		//this.force = force;
	}

	@Override
	protected void tick() {

		// wait till player death task is finished
		if (player.isDead()) {
			return;
		}

		// player is in combat for too long
		if (player.getCombat().isUnderAttack() && stopwatch.elapsedTime(TimeUnit.SECONDS) < 60) {
			return;
		}

		// if a player is in an activity they can logout if the activity lets them or
		// unless 60 seconds has elapsed
		if (Activity.evaluate(player, it -> !it.canLogout(player)) && stopwatch.elapsedTime(TimeUnit.SECONDS) > 60) {
			return;
		}

		player.interfaceManager.close();
		
		if (!flag) {
			PlayerSerializer.save(player);
			flag = true;
			return;
		}

		Store.removePersonalStore(player);
		
		// make sure the players account has saved first, player saving is on a
		// different thread so its gonna take
		// a few seconds
		if (player.saved.get()) {
			cancel();
		}

	}

	@Override
	protected void onCancel(boolean logout) {
		player.unregister();
	}

}
