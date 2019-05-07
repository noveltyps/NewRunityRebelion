package io.server.game.action.impl;

import io.server.content.ActivityLog;
import io.server.content.SecretsChest;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

/**
 * Handles opening a Secrets chest.
 *
 * @author ISmokeDope & Parano1a 5/2019
 */
public final class SecretsChestAction extends Action<Player> {

	//private final GameObject object;

	public SecretsChestAction(Player player/*, GameObject object*/) {
		super(player, 1);
		//this.object = object;
	}

	@Override
	protected void onSchedule() {
		getMob().locking.lock();
		getMob().inventory.remove(3468, 1);
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
		getMob().inventory.add(SecretsChest.getReward());
		AchievementHandler.activate(getMob(), AchievementKey.SECRETS_CHEST, 1);
		getMob().send(new SendMessage("...you find a few items inside of the chest."));
		getMob().activityLogger.add(ActivityLog.SECRETS_CHEST);
	}

	@Override
	public String getName() {
		return "Secrets chest";
	}

	@Override
	public boolean prioritized() {
		return false;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}
}