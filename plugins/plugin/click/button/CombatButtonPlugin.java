package plugin.click.button;

import java.util.Arrays;
import java.util.Optional;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.magic.Autocast;
import io.server.game.world.entity.combat.weapon.WeaponInterface;
import io.server.game.world.entity.mob.player.Player;

public class CombatButtonPlugin extends PluginContext {

	/** Array of all the auto retaliate button identifications. */
	private static final int[] AUTO_RETALIATE_BUTTON = { 24010, 24017, 24025, 24033, 24041, 24048, 24068, 24115,
			22845 };

	@Override
	protected boolean onClick(Player player, int button) {
		/* Auto retaliate. */
		if (Arrays.stream(AUTO_RETALIATE_BUTTON).anyMatch(b -> button == b)) {
			player.settings.autoRetaliate = !player.settings.autoRetaliate;
			return true;
		}

		if (Autocast.clickButton(player, button)) {
			return true;
		}

		Optional<FightType> fightButton = player.getWeapon().forFightButton(button);

		if (fightButton.isPresent()) {
			FightType fightType = fightButton.get();
			player.getCombat().setFightType(fightType);
			return true;
		}

		if (player.getCombatSpecial() != null && WeaponInterface.isSpecialButton(button)) {
			if (!player.isSpecialActivated()) {
				player.getCombatSpecial().enable(player);
			} else {
				player.getCombatSpecial().disable(player, true);
			}
			return true;
		}
		return false;
	}

}
