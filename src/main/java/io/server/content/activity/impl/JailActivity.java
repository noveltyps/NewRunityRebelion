package io.server.content.activity.impl;

import io.server.Config;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

public class JailActivity extends Activity {
	
	private final Player player;

	private JailActivity(Player player) {
		super(Integer.MAX_VALUE, Mob.DEFAULT_INSTANCE_HEIGHT);
		this.player = player;
	}

	public static JailActivity create(Player player) {
		JailActivity activity = new JailActivity(player);
		player.move(Config.JAIL_ZONE);
		activity.add(player);
		activity.resetCooldown();
		player.setVisible(true);
		return activity;
	}

	@Override
	protected void start() {
			finish();
	}

	@Override
	public void onDeath(Mob mob) {
		player.move(Config.JAIL_ZONE);
		player.message("BAM! YOU'RE BACK!");
	}

	@Override
	public boolean canTeleport(Player player) {
		player.message("You are jailed you douche! untill staff member says otherwise.");
		player.message("ask a staff to check your jail duration.");
		return false;
	}

	@Override
	public void onRegionChange(Player player) {
		player.move(Config.JAIL_ZONE);
	}

	@Override
	public void finish() {
		remove(player);
		player.move(Config.DEFAULT_POSITION);
		player.message("Time's up! You are free to go, hope you learn from your mistakes.");
	}

	@Override
	public void cleanup() {

	}

	@Override
	public ActivityType getType() {
		return ActivityType.JAIL;
	}
}
