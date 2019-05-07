package plugin.click.object;

import io.server.content.Obelisks;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class ObeliskObjectClickPlugin extends PluginContext {

	@Override
	protected boolean firstClickObject(Player player, ObjectClickEvent event) {
		return Obelisks.get().activate(player, event.getObject().getId());
	}

}
