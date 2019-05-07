package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class PrivateZoneCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		DialogueFactory factory = player.dialogueFactory;
		factory.sendOption("Private Room", () -> {
			Teleportation.teleport(player, new Position(3108, 3161, 3));
			player.send(new SendMessage("@or2@Welcome to the Negotiation Zone, " + player.getName() + "."));
		}, "Private Room 2", () -> {
			Teleportation.teleport(player, new Position(3208, 3218, 3));
			player.send(new SendMessage("@or2@Welcome to the Second Negotiation Zone, " + player.getName() + "."));
		}, "Private Room 3", () -> {
			Teleportation.teleport(player, new Position(3203, 3472, 3));
			player.send(new SendMessage("@or2@Welcome to the Third Negotiation Zone, " + player.getName() + "."));
		}, "Private Room 4", () -> {
			factory.sendStatement("Loading").sendOption("Private room 4", () -> {
				Teleportation.teleport(player, new Position(2726, 3491, 1));
				player.send(new SendMessage("@or2@Welcome to the Fourth Negotiation Zone, " + player.getName() + "."));
			}, "Staff zone", () -> {
				Teleportation.teleport(player, new Position(2602, 3874, 0));
				player.send(new SendMessage("@or2@Welcome to the staffzone, " + player.getName() + "."));
			}).execute();
		}, "Nevermind", factory::clear).execute();
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Teleports you to a private negotiation zone.";
	}
}
