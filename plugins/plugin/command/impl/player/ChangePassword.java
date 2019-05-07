package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class ChangePassword implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		String password = parts[1];

		for (int i = 0; i < Config.BAD_STRINGS.length; i++) {
			if (password.contains(Config.BAD_STRINGS[i])) {
				player.send(new SendMessage("Your password can't contain any invalid words!"));
				return;
			}
		}
		
		if (password.length() >= 15) {
			player.send(new SendMessage("Your password lenght can't be longer than 15"));
			return;
		}
		
		if (password.length() < 5) {
			player.send(new SendMessage("Your password lenght can't be shorter than 5"));
			return;
		}
		
		if (player.getPassword().equals(password)) {
			player.send(new SendMessage("You can't set the same password!"));
			return;
		}
		
		player.setPassword(password);
		player.send(new SendMessage("@red@ Your account details have been updated!"));
		player.send(new SendMessage("@red@ Please logout for action to take place!"));
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Changes your password to the specified password.";
	}
}
