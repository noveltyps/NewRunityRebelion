package plugin.click.button;

import io.server.content.ItemsKeptOnDeath;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class EquipmentButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (button == 27653) {
			player.equipment.openInterface();
			return true;
		}
		if (button == 27654) {
			ItemsKeptOnDeath.open(player);
			return true;
		}
		return false;
	}
}