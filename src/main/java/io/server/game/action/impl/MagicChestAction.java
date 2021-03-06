package io.server.game.action.impl;

import io.server.content.ActivityLog;
import io.server.content.CrystalChest;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

/**
 * Handles opening a crystal chest.
 *
 * @author Daniel
 */
public final class MagicChestAction extends Action<Player> {

	//private final GameObject object;

	private int object;

	public MagicChestAction(Player player/*, GameObject object*/) {
		super(player, 1);
		this.setObject(2191);
	}

	@Override
	protected void onSchedule() {
		getMob().locking.lock();
		getMob().inventory.remove(1547, 1);
		getMob().animate(new Animation(881));
		getMob().send(new SendMessage("You attempt to unlock the chest..."));
	}

	@Override
	public void execute() {
		cancel();
	}

	@Override
	protected void onCancel(boolean logout) {
		getMob().locking.unlock();
		getMob().inventory.add(1631, 1);
		getMob().inventory.add(CrystalChest.getReward());
		AchievementHandler.activate(getMob(), AchievementKey.MAGIC_CHEST, 1);
		getMob().send(new SendMessage("...you find a few items inside of the chest."));
		getMob().activityLogger.add(ActivityLog.MAGIC_CHEST);
	}

	@Override
	public String getName() {
		return "Magic chest";
	}

	@Override
	public boolean prioritized() {
		return false;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	public int getObject() {
		return object;
	}

	public void setObject(int object) {
		this.object = object;
	}
}