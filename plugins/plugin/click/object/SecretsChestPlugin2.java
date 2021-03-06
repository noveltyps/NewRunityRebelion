package plugin.click.object;

import io.server.content.SecretsChest2;
import io.server.game.action.impl.SecretsChestAction2;
import io.server.game.event.impl.ItemOnItemEvent;
import io.server.game.event.impl.ItemOnObjectEvent;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class SecretsChestPlugin2 extends PluginContext {

	

	@Override
	protected boolean firstClickObject(Player player, ObjectClickEvent event) {
		if (event.getObject().getId() != 31574) {
			return false;
		}

		if (!player.inventory.contains(SecretsChest2.KEY)) {
			player.dialogueFactory.sendItem("Secrets Key 2", "You need a Secrets key 2 to enter this chest!",
					SecretsChest2.KEY.getId());
			player.send(new SendMessage("You need a Secrets key 2 to enter this chest!"));
			return true;
		}

		if (player.inventory.remaining() < 3) {
			player.send(new SendMessage("You need at lest 3 free inventory spaces to enter the chest."));
			return true;
		}

		player.action.execute(new SecretsChestAction2(player), true);
		return true;
	}

	@Override
	protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
		if (event.getUsed().getId() == 3462 && event.getObject().getId() == 31574) {
			if (!player.inventory.contains(SecretsChest2.KEY)) {
				player.dialogueFactory.sendItem("Screts Key 2", "You need a Secrets key 2 to enter this chest!",
						SecretsChest2.KEY.getId());
				player.send(new SendMessage("You need a Secrets key 2 to enter this chest!"));
				return true;
			}

			if (player.inventory.remaining() < 3) {
				player.send(new SendMessage("You need at lest 3 free inventory spaces to enter the chest."));
				return true;
			}

			player.action.execute(new SecretsChestAction2(player), true);
			return true;
		}

		return false;
	}

}
