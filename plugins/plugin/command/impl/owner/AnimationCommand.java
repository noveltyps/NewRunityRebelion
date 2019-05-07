package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.Animation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

public class AnimationCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		int id = Integer.parseInt(parts[1]);
		player.animate(new Animation(id));
		player.send(new SendMessage("Performing animation = " + id));

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Makes your character perform the specified animation.";
	}
}
