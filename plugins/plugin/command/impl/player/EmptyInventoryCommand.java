package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Area;
import io.server.util.Utility;

public class EmptyInventoryCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		if (Area.inWilderness(player) || player.getCombat().inCombat() || Area.inDuelArena(player)
				|| player.playerAssistant.busy()) {
			player.message("@or2@You can not clear your inventory at this current moment.");
			return;
		}

		if (player.inventory.isEmpty()) {
			player.message("@or2@You have nothing to empty!");
			return;
		}

		String networth = Utility.formatDigits(player.playerAssistant.networth(player.inventory));
		player.dialogueFactory.sendStatement("Are you sure you want to clear your inventory? ",
				"Container worth: <col=255>" + networth + " </col>coins.");
		player.dialogueFactory.sendOption("Yes", () -> {
			player.inventory.clear(true);
			player.dialogueFactory.clear();
		}, "Nevermind", () -> player.dialogueFactory.clear());
		player.dialogueFactory.execute();
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Empties your inventory.";
	}
}
