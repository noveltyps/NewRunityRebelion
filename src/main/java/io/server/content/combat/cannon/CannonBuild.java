package io.server.content.combat.cannon;

import io.server.game.Animation;
import io.server.game.task.TickableTask;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.object.CustomGameObject;

/**
 * @Adam_#6723
 * 
 *
 *
 */
public class CannonBuild extends TickableTask {

	private final Player player;
	private final Cannon cannon;

	public CannonBuild(Player player, Cannon cannon) {
		super(false, 1);
		this.player = player;
		this.cannon = cannon;
	}

	@Override
	protected void onSchedule() {
		player.locking.lock();
	}

	@Override
	protected void onCancel(boolean logout) {
		player.locking.unlock();
	}

	@Override
	protected void tick() {
		switch (tick) {
		case 1:
			cannon.setStage(CannonManager.Setup.BASE);
			player.face(Direction.NORTH_EAST);
			player.animate(new Animation(827));
			player.inventory.remove(new Item(6));
			break;
		case 2:
			cannon.getObject().register();
			break;
		case 3:
			cannon.setStage(CannonManager.Setup.STAND);
			player.animate(new Animation(827));
			player.inventory.remove(new Item(8));
			break;
		case 5:
			cannon.setStage(CannonManager.Setup.BARRELS);
			player.animate(new Animation(827));
			player.inventory.remove(new Item(10));
			break;
		case 6:
			cannon.getObject().unregister();
			cannon.setObject(new CustomGameObject(9, cannon.getPosition()));
			cannon.getObject().register();
			break;
		case 7:
			cannon.setStage(CannonManager.Setup.FURNACE);
			player.animate(new Animation(827));
			player.inventory.remove(new Item(12));
			break;
		case 8:
			cannon.getObject().unregister();
			cannon.setObject(new CustomGameObject(6, cannon.getPosition()));
			cannon.getObject().register();
			break;
		case 9:
			cannon.setRotation(CannonManager.Rotation.NORTH);
			cannon.setStage(CannonManager.Setup.COMPLETE_CANNON);
			player.locking.unlock();
			CannonManager.ACTIVE_CANNONS.put(player.getName(), cannon);
			cancel();
			break;
		}
	}
}
