package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class SkillAreaCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		Teleportation.teleport(player, Config.SKILL_ZONE, 20, () -> {
			player.send(new SendMessage("@or2@Welcome to the Skilling Zone, " + player.getName() + "!"));
		});
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	
	@Override
	public String description() {
		return "Teleports you to the skilling area.";
	}
}
