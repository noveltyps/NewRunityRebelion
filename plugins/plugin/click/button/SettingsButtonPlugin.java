package plugin.click.button;

import io.server.Config;
import io.server.content.writer.InterfaceWriter;
import io.server.content.writer.impl.SettingWriter;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendScreenMode;
import io.server.net.packet.out.SendString;

public class SettingsButtonPlugin extends PluginContext {

	@SuppressWarnings("static-access")
	@Override
	protected boolean onClick(Player player, int button) {
		int obelisk = player.attributes.get("OBELISK") != null ? player.attributes.get("OBELISK", Integer.class) : -1;
		if (obelisk == -1) {
			switch (button) {
			case -14524:
				player.settings.welcomeScreen = !player.settings.welcomeScreen;
				InterfaceWriter.write(new SettingWriter(player));
				return true;
			case -14523:
				player.settings.triviaBot = !player.settings.triviaBot;
				InterfaceWriter.write(new SettingWriter(player));
				return true;
			case -14522:
				player.settings.yell = !player.settings.yell;
				InterfaceWriter.write(new SettingWriter(player));
				return true;
			case -14521:
				player.settings.dropNotification = !player.settings.dropNotification;
				InterfaceWriter.write(new SettingWriter(player));
				return true;
			case -14520:
				player.settings.untradeableNotification = !player.settings.untradeableNotification;
				InterfaceWriter.write(new SettingWriter(player));
				return true;
			case -14519:
				player.settings.prestigeColors = !player.settings.prestigeColors;
				InterfaceWriter.write(new SettingWriter(player));
				return true;
			}
		}
		switch (button) {
		case -15505:
			player.settings.clientWidth = 765;
			player.settings.clientHeight = 503;
			player.send(new SendScreenMode(765, 503));
			return true;
		case -15502:
			player.settings.clientWidth = 766;
			player.settings.clientHeight = 559;
			player.send(new SendScreenMode(766, 559));
			return true;
		case -15499:
			player.settings.clientWidth = -1;
			player.settings.clientHeight = -1;
			player.send(new SendScreenMode(-1, -1));
			return true;
		case -15534:
			player.interfaceManager.setSidebar(Config.WRENCH_TAB, 50020);
			player.send(new SendConfig(980, 0));
			return true;
		case -15533:
			player.interfaceManager.setSidebar(Config.WRENCH_TAB, 50100);
			player.send(new SendConfig(980, 1));
			return true;
		case -15532:
			player.interfaceManager.setSidebar(Config.WRENCH_TAB, 50200);
			player.send(new SendConfig(980, 2));
			return true;
		case -15531:
			player.send(new SendMessage(":settingupdate:"));
			player.interfaceManager.setSidebar(Config.WRENCH_TAB, 50300);
			player.send(new SendConfig(980, 3));
			return true;
		case -15528:
			if (!PlayerRight.isDonator(player)) {
				player.dialogueFactory.sendStatement("You need to be a donator to access this tab!").execute();
				return true;
			} else if (player.right.isDonator(player) || player.right.isPriviledged(player)
					|| player.right.isElite(player) || player.right.isDeveloper(player)
					|| player.right.isExtreme(player) || player.right.isKing(player) || player.right.isSuper(player)
					|| player.right.isSupreme(player))
				player.interfaceManager.setSidebar(Config.WRENCH_TAB, 50400);
			player.send(new SendConfig(980, 4));
			player.message("welcome to the donators tab");
			return true;
		case -26213:
			player.settings.ESC_CLOSE = !player.settings.ESC_CLOSE;
			player.send(new SendConfig(594, player.settings.ESC_CLOSE ? 1 : 0));
			return true;
		case -15506:
			player.settings.setBrightness(4);
			player.send(new SendConfig(166, player.settings.brightness));
			return true;
		case -15507:
			player.settings.setBrightness(3);
			player.send(new SendConfig(166, player.settings.brightness));
			return true;
		case -15508:
			player.settings.setBrightness(2);
			player.send(new SendConfig(166, player.settings.brightness));
			return true;
		case -15509:
			player.settings.setBrightness(1);
			player.send(new SendConfig(166, player.settings.brightness));
			return true;
		case -15514:
			player.settings.setZoom(0, false);
			return true;
		case -15513:
			player.settings.setZoom(1, false);
			return true;
		case -15512:
			player.settings.setZoom(2, false);
			return true;
		case -15511:
			player.settings.setZoom(3, false);
			return true;

		case -15530:
			if (PlayerRight.isIronman(player)) {
				player.send(new SendMessage("As an iron man you can not do this!"));
				return true;
			}
			player.settings.acceptAid = !player.settings.acceptAid;
			player.send(new SendConfig(427, player.settings.acceptAid ? 1 : 0));
			return true;
		case -15529:
		case 1050:
			player.movement.setRunningToggled(!player.movement.isRunningToggled());
			return true;
		case -15435:
			player.settings.chatEffects = !player.settings.chatEffects;
			player.send(new SendConfig(171, player.settings.chatEffects ? 1 : 0));
			return true;
		case -15434:
			player.settings.privateChat = !player.settings.privateChat;
			player.send(new SendConfig(287, player.settings.privateChat ? 1 : 0));
			return true;
		case -15433:
			/*
			 * player.settings.profanityFilter = !player.settings.profanityFilter;
			 * player.send(new SendConfig(203, player.settings.profanityFilter ? 1 : 0));
			 */
			return true;
		case -15335:
			player.settings.mouse = !player.settings.mouse;
			player.send(new SendConfig(170, player.settings.mouse ? 1 : 0));
			return true;
		case -15334:
			player.settings.cameraMovement = !player.settings.cameraMovement;
			player.send(new SendConfig(207, player.settings.cameraMovement ? 1 : 0));
			return true;
		case -15496:
			player.interfaceManager.open(28500);
			return true;
		case 29417:
			player.attributes.set("OBELISK", -1);
			player.send(new SendString("Server Settings", 57312));
			player.send(new SendString("Click on the setting you would like to toggle", 57313));
			InterfaceWriter.write(new SettingWriter(player));
			player.interfaceManager.open(57310);
			return true;
		case -29517:
			player.send(new SendMessage(":settingupdate:"));
			player.interfaceManager.open(28_500);
			return true;
		case -15333:
			player.send(new SendMessage(":keybinding:"));
			player.interfaceManager.open(39300);
			return true;
		}
		return false;
	}
}
