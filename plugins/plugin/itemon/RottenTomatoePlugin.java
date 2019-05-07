package plugin.itemon;

import io.server.game.event.impl.ItemOnNpcEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;

/**
 * Handles the rotten tomatoe plugin.
 *
 * @author Daniel.
 */
public class RottenTomatoePlugin extends PluginContext {

	@Override
	protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
		if (!PlayerRight.isDonator(player) || !PlayerRight.isDeveloper(player) || !PlayerRight.isManagement(player))
			return false;

		Item item = event.getUsed();

		if (item.getId() != 5733)
			return false;

		Npc npc = event.getNpc();

		player.dialogueFactory.sendStatement("<col=255>" + npc.getName(), "spawn=" + npc.spawnPosition + "");
		player.dialogueFactory.execute();
		return true;
	}
}
