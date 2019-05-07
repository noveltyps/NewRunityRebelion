package plugin.click.button;

import io.server.content.scratchcard.ScratchCard;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class ScratchCardButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		switch (button) {

		case 20016:
		case 20021:
		case 20026:
		case 20033:
		case 20037:
			new ScratchCard(player).reveal(button);
			break;
		}
		return false;
	}
}