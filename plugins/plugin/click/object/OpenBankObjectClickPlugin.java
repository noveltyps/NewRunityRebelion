package plugin.click.object;

import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.ObjectDefinition;

public class OpenBankObjectClickPlugin extends PluginContext {

	@Override
	protected boolean firstClickObject(Player player, ObjectClickEvent event) {
		final ObjectDefinition def = event.getObject().getDefinition();

		if (def == null || def.name == null) {
			return false;
		}

		final String name = def.name.toLowerCase();

		if (name.contains("bank booth") || name.contains("clan bank") || name.equals("open chest")
				|| name.equals("bank chest") || name.equals("grand exchange booth")) {
			player.bank.open();
			return true;
		}

		return false;
	}

	@Override
	protected boolean secondClickObject(Player player, ObjectClickEvent event) {
		final ObjectDefinition def = event.getObject().getDefinition();

		if (def == null || def.name == null) {
			return false;
		}

		final String name = def.name.toLowerCase();

		if (name.contains("bank booth") || name.contains("clan bank") || name.equals("grand exchange booth")) {
			player.bank.open();
			return true;
		}

		return false;
	}

}
