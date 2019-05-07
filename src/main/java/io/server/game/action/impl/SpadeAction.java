package io.server.game.action.impl;

import io.server.content.activity.impl.barrows.BarrowsUtility;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

/**
 * Handles digging with a spade.
 *
 * @author Daniel.
 */
public final class SpadeAction extends Action<Player> {

	public SpadeAction(Player player) {
		super(player, 2, false);
	}

	@Override
	protected void onSchedule() {
		getMob().movement.reset();
		getMob().animate(new Animation(830));
		getMob().send(new SendMessage("You start digging..."));
	}

	@Override
	public void execute() {
		boolean found = false;

		if (BarrowsUtility.teleportPlayer(getMob()))
			found = true;

		if (!found)
			getMob().send(new SendMessage("You found nothing of interest."));
		cancel();
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	@Override
	public boolean prioritized() {
		return false;
	}

	@Override
	public String getName() {
		return "Spade Action";
	}
}