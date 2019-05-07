package plugin.click.button;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.exchange.ExchangeSession;

public class ExchangeSessionButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		ExchangeSession session = ExchangeSession.getSession(player).orElse(null);
		return session != null && session.onButtonClick(player, button);
	}
}
