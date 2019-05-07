package plugin.click.item;

import java.util.concurrent.TimeUnit;

import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Area;

public class DwarvenRockCakePlugin extends PluginContext {

	@Override
	protected boolean firstClickItem(Player player, ItemClickEvent event) {
		if (event.getItem().getId() == 7509) {
			if (!player.itemDelay.elapsed(599, TimeUnit.MILLISECONDS))
				return true;

			if (player.getCombat().inCombat()) {
				player.message("You can not eat this while in combat!");
				return true;
			}

			if (Area.inWilderness(player)) {
				player.message("You better not eat this while in the wilderness!");
				return true;
			}

			int health = player.getCurrentHealth();
			int damage = health - 1;

			if (damage <= 0) {
				player.message("You better not eat that!");
				return true;
			}

			player.speak("Ouch!");
			player.damage(new Hit(player.getCurrentHealth() - 1));
			player.itemDelay.reset();
			return true;
		}
		return false;
	}
}
