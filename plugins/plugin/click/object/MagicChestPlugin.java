package plugin.click.object;

import io.server.content.CrystalChest;
import io.server.game.action.impl.CrystalChestAction;
import io.server.game.event.impl.ItemOnItemEvent;
import io.server.game.event.impl.ItemOnObjectEvent;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class MagicChestPlugin extends PluginContext {

	@Override
	protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
		boolean valid = false;
		if (event.getUsed().getId() == 985 && event.getWith().getId() == 987) {
			valid = true;
		} else if (event.getUsed().getId() == 987 && event.getWith().getId() == 985) {
			valid = true;
		}

		if (valid) {
			CrystalChest.createKey(player);
			return true;
		}
		
		if (event.getUsed().getId() == 1547 && event.getWith().getId() == 1547) {
			valid = true;
		} else if (event.getUsed().getId() == 1547 && event.getWith().getId() == 1547) {
			valid = true;
		}

		if (valid) {
			CrystalChest.createKey(player);
			return true;
		}
		return false;
	}

	@Override
	protected boolean firstClickObject(Player player, ObjectClickEvent event) {
		if (event.getObject().getId() != 13291
) {
			return false;
		}

		if (!player.inventory.contains(CrystalChest.MKEY)) {
			player.dialogueFactory.sendItem("Magic Chest Key", "You need a Magic Chest key to enter this chest!",
					CrystalChest.MKEY.getId());
			player.send(new SendMessage("You need a Magic Chest to enter this chest!"));
			return true;
		}

		if (player.inventory.remaining() < 3) {
			player.send(new SendMessage("You need at lest 3 free inventory spaces to enter the chest."));
			return true;
		}

		player.action.execute(new CrystalChestAction(player), true);
		return true;
	}

	@Override
	protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
		if (event.getUsed().getId() == 1547 && event.getObject().getId() == 13291) {
			if (!player.inventory.contains(CrystalChest.MKEY)) {
				player.dialogueFactory.sendItem("Magic Key", "You need a magic key to enter this chest!",
						CrystalChest.MKEY.getId());
				player.send(new SendMessage("You need a magic key to enter this chest!"));
				return true;
			}

			if (player.inventory.remaining() < 3) {
				player.send(new SendMessage("You need at lest 3 free inventory spaces to enter the chest."));
				return true;
			}

			player.action.execute(new CrystalChestAction(player), true);
			return true;
		}

		return false;
	}

}
