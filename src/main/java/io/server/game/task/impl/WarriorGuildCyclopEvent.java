package io.server.game.task.impl;

import io.server.game.task.Task;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;

/**
 * An randomevent which handles the warrior guild cyclops randomevent.
 *
 * @author Daniel | Obey
 */
public class WarriorGuildCyclopEvent extends Task {

	/** The player instance. */
	private final Player player;

	/** Constructs a new <code>WarriorGuildCyclopEvent</code>. */
	public WarriorGuildCyclopEvent(Player player) {
		super(90);
		this.player = player;
		this.attach(player);
	}

	@Override
	public void execute() {
		if (player.isDead()) {
			cancel();
			return;
		}
		if (!player.isValid()) {
			cancel();
			return;
		}
		if (!player.inActivity()) {
			cancel();
			return;
		}
		if (!Area.inCyclops(player)) {
			cancel();
			return;
		}
		if (!player.inventory.contains(new Item(8851, 10))) {
			if (Area.inCyclops(player)) {
				player.move(new Position(2846, 3540, 2));
			}
			player.dialogueFactory
					.sendStatement("You have been removed from the Cyclops", "room as you have run out of tokens.")
					.execute();
			return;
		}
		player.inventory.remove(new Item(8851, 10));
		player.send(new SendMessage("10 tokens have been removed from your inventory."));
		if (!player.inventory.contains(new Item(8851, 10))) {
			if (Area.inCyclops(player)) {
				player.move(new Position(2846, 3540, 2));
			}
			player.dialogueFactory
					.sendStatement("You have been removed from the Cyclops", "room as you have run out of tokens.")
					.execute();
		}
	}

	@Override
	protected void onSchedule() {
		player.warriorGuidTask = true;
	}

	@Override
	protected void onCancel(boolean logout) {
		player.warriorGuidTask = false;
	}

}
