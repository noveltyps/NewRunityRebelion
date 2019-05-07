package plugin.click.button;

import java.util.List;

import io.server.content.DropDisplay;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.npc.drop.NpcDropManager;
import io.server.game.world.entity.mob.player.Player;

public class DropDisplayButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (button >= -11020 && button <= -10992) {
			int base_button = -11020;
			int modified_button = (base_button - button) / 2;
			int index = Math.abs(modified_button);
			@SuppressWarnings("unchecked")
			List<Integer> key = player.attributes.get("DROP_DISPLAY_KEY", List.class);
			if (key == null || key.isEmpty())
				return false;
			if (index >= key.size())
				return false;
			DropDisplay.display(player, NpcDropManager.NPC_DROPS.get(key.get(index)));
			return true;
		}
		return false;
	}
}
