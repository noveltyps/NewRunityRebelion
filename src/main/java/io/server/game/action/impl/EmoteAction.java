package io.server.game.action.impl;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;

/**
 * Handles performing an emote action
 *
 * @author Daniel
 */
public final class EmoteAction extends Action<Player> {
	/**
	 * The animation identification.
	 */
	private final int animation;

	/**
	 * The graphic identification.
	 */
	private final int graphic;

	/**
	 * Constructs a new <code>EmoteAction</code>.
	 *
	 * @param player    The player instance.
	 * @param animation The animation identification.
	 * @param graphic   The graphic identification.
	 */
	public EmoteAction(Player player, int animation, int graphic) {
		super(player, 3, true);
		this.animation = animation;
		this.graphic = graphic;
	}

	@Override
	public void execute() {
		if (animation != -1) {
			getMob().animate(new Animation(animation));
		}

		if (graphic != -1) {
			getMob().graphic(new Graphic(graphic));
		}

		cancel();
	}

	@Override
	public String getName() {
		return "Emote";
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