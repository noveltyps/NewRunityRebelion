package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;

/**
 * @author Adam_#6723
 */

public class PnpcCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		final String message = "That player was not valid, please re-select a player.";

		player.send(new SendInputMessage("Enter id", 10, input -> {
			if (player != null) {
				player.playerAssistant.transform(Integer.parseInt(input), false);
			}
			player.send(new SendMessage(
					player == null ? message
							: "You have turned " + player.getName() + " into "
									+ NpcDefinition.get(Integer.parseInt(input)).getName() + ".",
					MessageColor.DARK_BLUE));
		}));

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Changes your character's appearance to the specified NPC ID.";
	}
}
