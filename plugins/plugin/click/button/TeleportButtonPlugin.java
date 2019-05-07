package plugin.click.button;

import io.server.Config;
import io.server.content.skill.impl.magic.teleport.TeleportType;
import io.server.content.teleport.TeleportHandler;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendForceTab;

public class TeleportButtonPlugin extends PluginContext {

	/**
	 * @Adam_#6723
	 */
	@Override
	protected boolean onClick(Player player, int button) {
		if (button >= -7484 && button <= -7440) {
			TeleportHandler.click(player, button);
			return true;
		}
		switch (button) {
		case 850:
			// ADAM I SWEAR TO FUCKING CHRIST IF YOU REMOVE THIS LINE ONE MORE GODDAMN TIME
			// I'M<<<<<<<<<<<<calm down| DRIVING A TRUCK DOWNTOWN SWEDEN AGAIN>>>>>>>>>>>>>
			// I'M FUCKING DONE WITH YOUR SHIT
			TeleportHandler.open(player);
			return true;
		case -7501:
			TeleportHandler.teleport(player);
			return true;
		case -7530:
			TeleportHandler.open(player, TeleportType.FAVORITES);
			return true;
		case 1541:
		case 13079:
		case -7526:
			TeleportHandler.open(player, TeleportType.MINIGAMES);
			return true;
		case 1540:
		case 13069:
		case -7522:
			TeleportHandler.open(player, TeleportType.SKILLING);
			return true;
		case 1170:
		case 13053:
		case -7518:
			TeleportHandler.open(player, TeleportType.MONSTER_KILLING);
			return true;
		case 1164:
		case 13035:
		case -7514:
			TeleportHandler.open(player, TeleportType.PLAYER_KILLING);
			return true;
		case 1174:
		case -7510:
			TeleportHandler.open(player, TeleportType.BOSS_KILLING);
			return true;
		case 13061:
			player.send(new SendForceTab(Config.MUSIC_TAB));
			break;
		case -7497:
		case -7496:
			TeleportHandler.favorite(player);
			return true;
		}
		return false;
	}
}