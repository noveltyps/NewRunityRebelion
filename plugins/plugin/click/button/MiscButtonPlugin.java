package plugin.click.button;

import io.server.Config;
import io.server.content.WellOfGoodwill;
import io.server.content.skill.impl.agility.Agility;
import io.server.content.skill.impl.construction.BuildableInterface;
import io.server.content.skill.impl.construction.BuildableType;
import io.server.content.skill.impl.magic.teleport.TeleportType;
import io.server.content.teleport.TeleportHandler;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendURL;

public class MiscButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (Agility.clickButton(player, button)) {
			return true;
		}
		switch (button) {
		case 31710:
			player.interfaceManager.close();
			break;
		case 32303:
		case -22534:
		case 2422:
		case 26802:
		case 28502:
		case -7534:
		case -23829:
			player.interfaceManager.close(false);
			return true;
		case 21301:
			player.interfaceManager.close(false);
			player.setVisible(true);
			return true;
		case 2458:
			player.logout();
			return true;
		case 21304:
			player.send(new SendURL(Config.LATEST_ANNOUNCEMENT_THREAD));
			return true;
		case 21307:
			player.send(new SendURL(Config.LATEST_UPDATE_THREAD));
			return true;
		case 850:
			if (player.house.isInside()) {
				BuildableInterface.open(player, BuildableType.MAIN_OBJECT);
			} else {
				TeleportHandler.open(player, TeleportType.FAVORITES);
			}
			return true;

        case -6027:
            player.mysteryBox.spin();
            return true;
		case 13390:
			player.interfaceManager.close();
			return true;
		case 14176:
			player.dialogueFactory.clear();
			return true;
		case 14175:
			player.playerAssistant.handleDestroyItem();
			return true;
		case 26702:
			player.lootingBag.close();
			return true;
		case -21823:
			player.send(new SendInputAmount("How much would you like to contribute?", 8, input -> {
				input = input.toLowerCase();
				input = input.replaceAll("k", "000");
				input = input.replaceAll("m", "000000");
				input = input.replaceAll("b", "000000000");

				try {
					long amount = Long.parseLong(input);

					if (amount > Integer.MAX_VALUE) {
						amount = Integer.MAX_VALUE;
					}

					WellOfGoodwill.contribute(player, (int) amount);
				} catch (Exception ex) {
					player.message("This amount is invalid.");
				}
			}));
			return true;
		}
		return false;
	}
}
