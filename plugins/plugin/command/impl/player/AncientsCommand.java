package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

public class AncientsCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.spellbook = Spellbook.ANCIENT;
		player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
		player.sendMessage("You have switched your magics to ancients..");
		player.sendMessage("points="+player.getReferralPoints());

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isPlayer(player);
	}

	@Override
	public String description() {
		return "Switches your spellbook to ancient magics.";
	}

}
