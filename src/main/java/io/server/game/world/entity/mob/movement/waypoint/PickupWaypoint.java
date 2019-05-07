package io.server.game.world.entity.mob.movement.waypoint;

import io.server.game.world.Interactable;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.position.Position;

public class PickupWaypoint extends Waypoint {

	private final Player player;
	private final Item item;
	private final Position position;

	public PickupWaypoint(Player player, Item item, Position position) {
		super(player, Interactable.create(position, 0, 0));
		this.player = player;
		this.item = item;
		this.position = position;
	}

	@Override
	public void onDestination() {
		mob.movement.reset();
		GroundItem.pickup(player, item, position);
		cancel();
	}

	@Override
	protected int getRadius() {
		return 0;
	}

}
