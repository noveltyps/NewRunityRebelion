package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.content.staff.PanelType;
import io.server.content.staff.StaffPanel;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/**
 * 
 * @author Adam_#6723
 *
 */

public class ManagementCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		StaffPanel.open(player, PanelType.INFORMATION_PANEL);
       }

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isManagement(player);
	}

	@Override
	public String description() {
		return "Opens the management panel.";
	}
}
