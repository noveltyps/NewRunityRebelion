package plugin.click.button;

import io.server.Config;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.store.Store;
import io.server.content.tittle.TitleManager;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class DonatorButtonPlugin extends PluginContext {

	/**
	 * @author Adam_#6723
	 */
	@Override
	protected boolean onClick(Player player, int button) {
		if (button == -8328) {
			player.donatorDeposit.confirm();
			return true;
		}
		if (button == -15115) {
			Teleportation.teleport(player, Config.DONATOR_ZONE);
			return true;
		}
		if (button == -15111) {
			TitleManager.open(player);
			player.message("Here donators can set their titles!");
		}
		if (button == -15107) {
			Store.STORES.get("Pk Rewards Shop 1").open(player);
			player.message(
					"Donator's can purchase items only exclusive to donors!");
		}
		return false;
	}
}
