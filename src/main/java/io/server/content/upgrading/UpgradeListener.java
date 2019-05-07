package io.server.content.upgrading;

import io.server.game.task.Task;
import io.server.game.task.TaskManager;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * System Execution
 * 
 * @author Nerik#8690
 *
 */
public class UpgradeListener {

	private Player player;

	public UpgradeListener(Player player) {
		this.player = player;
	}

	public void execute() {
		if (player.getUpgradeSelected() != null) {
			if (player.inventory.contains(player.getUpgradeSelected().getItemInput())
					&& player.inventory.contains(player.getUpgradeSelected().getEtharRequirement())) {
				if (!player.getUpgradeSession()) {
					player.setUpgradeInSesson(true);
					setItems(player.getUpgradeSelected());
					setExecution(player.getUpgradeSelected());
				} else {
					player.send(new SendMessage("@red@You are already in a upgrading session!"));
				}
			} else {
				player.send(new SendMessage("@red@You don't have the required items!"));
			}
		}
	}

	private void setItems(UpgradeData data) {
		player.inventory.removeAll(new Item[] { data.getItemInput(), data.getEtharRequirement() });

	}

	private void setExecution(UpgradeData data) {
		TaskManager.schedule(new Task(1) {
			int tick = 0;

			@Override
			protected void execute() {
				switch (tick) {
				case 0:
					player.send(new SendMessage("Upgrading item..."));
					break;
				case 1:
					start(data);
					player.setUpgradeInSesson(false);
					cancel();
					break;
				}
				tick++;
			}

		});
	}

	private UpgradeStatus start(UpgradeData data) {
		if (Utility.random(100) < (100 * data.getChance())) {
			player.send(new SendMessage("@red@You have succesfully upgraded your item"));
			World.sendMessage(
					"@red@[Upgrade] " + String.format(player.getUsername() + " " + "has succesfully upgraded a " + data.getItemReward().getName()));
			player.inventory.add(data.getItemReward());
			return UpgradeStatus.SUCCES;
		}
		player.send(new SendMessage("@red@You failed to upgrade!"));
		return UpgradeStatus.FAILED;
	}

}
