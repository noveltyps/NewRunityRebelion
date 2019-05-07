package plugin.click.button;

import io.server.content.Obelisks;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class ObeliskButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		int obelisk = player.attributes.get("OBELISK") != null ? player.attributes.get("OBELISK", Integer.class) : -1;
		if (obelisk != -1) {
			switch (button) {
			case -14524:
				Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_44);
				return true;
			case -14523:
				Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_27);
				return true;
			case -14522:
				Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_35);
				return true;
			case -14521:
				Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_13);
				return true;
			case -14520:
				Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_19);
				return true;
			case -14519:
				Obelisks.get().activate(player, obelisk, Obelisks.ObeliskData.LEVEL_50);
				return true;
			}
		}
		return false;
	}
}
