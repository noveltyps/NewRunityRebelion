package plugin.click.button;

import io.server.content.Skillguides.AttackApp;
import io.server.content.Skillguides.DefenceApp;
import io.server.content.Skillguides.HerbloreApp;
import io.server.content.Skillguides.MagicApp;
import io.server.content.Skillguides.PrayerApp;
import io.server.content.Skillguides.RangingApp;
import io.server.content.Skillguides.RunecraftingApp;
import io.server.content.Skillguides.StrengthApp;
import io.server.content.skill.impl.firemaking.FiremakingData;
import io.server.content.skill.impl.magic.teleport.TeleportType;
import io.server.content.teleport.TeleportHandler;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;

public class SkillsButtonPlugin extends PluginContext {

	FiremakingData firemaking = null;

	/**
	 * @author adameternal123 / Arlo handles clicking on the skills button. making
	 *         it similar to how ruse had it handled.
	 */
	@Override
	protected boolean onClick(Player player, int button) {
		// COMBAT GUIDES
		if (button == 8654) {// ATTACK
			AttackApp.open(player);
		}
		if (button == 8657) {// STR
			StrengthApp.open(player);
		}
		if (button == 8660) {// DEF
			DefenceApp.open(player);
		}
		if (button == 8663) {// RANGE
			RangingApp.open(player);
		}
		if (button == 8669) {// MAGE
			MagicApp.open(player);
		}
		if (button == 8666) {// PRAYER
			PrayerApp.open(player);

		}
		// N/A
		if (button == 8655) {
			player.message("This skill does not have a guide £!");
		}
		// SKILLING GUIDES
		if (button == 8661) {// HERB
			HerbloreApp.open(player);
		}
		if (button == 8672) {// RC
			RunecraftingApp.open(player);
		}
		if (button == 8658) {
			TeleportHandler.open(player, TeleportType.SKILLING);
		}

		return false;
	}
}
