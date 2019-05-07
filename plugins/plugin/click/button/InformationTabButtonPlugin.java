package plugin.click.button;
import io.server.Config;
import io.server.content.DropDisplay;
import io.server.content.DropSimulator;
import io.server.content.RoyaltyProgram;
import io.server.content.achievement.AchievementInterface;
import io.server.content.presetsnew.PresetsInterfaceHandler;
import io.server.content.skill.impl.slayer.SlayerTab;
import io.server.content.staff.PanelType;
import io.server.content.staff.StaffPanel;
import io.server.content.tittle.TitleManager;
import io.server.content.writer.InterfaceWriter;
import io.server.content.writer.impl.InformationWriter;
import io.server.content.writer.impl.QuestWriter;
import io.server.content.writer.impl.SettingWriter;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendForceTab;
import io.server.net.packet.out.SendURL;

public class InformationTabButtonPlugin extends PluginContext {

	/**
	 * 
	 * @author Adam_#6723
	 */

	@Override
	protected boolean onClick(Player player, int button) {
		switch (button) {
		
		case 29404:
		   if (PlayerRight.isPriviledged(player)) {
				StaffPanel.open(player, PanelType.INFORMATION_PANEL);
				return true;
			}
			player.send(new SendURL("http://www.brutalos.org"));
			return true;
		case 29429:
			new PresetsInterfaceHandler(player).open();
			System.err.println("Opening presets!");
			break;
		case 29440:
			InterfaceWriter.write(new InformationWriter(player));
			return true;
		/*case 29420:
			PlayerGuideHandler guide = new PlayerGuideHandler();
			guide.open(player);
			break;*/
			
		case 29420:
			player.interfaceManager.setSidebar(Config.WRENCH_TAB, 60400);
			player.send(new SendForceTab(Config.WRENCH_TAB));
			player.send(new SendConfig(980, 4));
			//player.send(new SendConfig(980, 4));
			player.message("welcome to the Player Panel");
			player.message("@red@This contains all the useful panels a player needs!");
			player.message("@red@Inlcuding the starter zone for New comers!");
			return true;
		case 29421:
		case 29419:
		case 29410:
			//SettingWriter command = new SettingWriter(player);
			player.dialogueFactory.sendOption("Misc Settings", () -> {
				player.dialogueFactory.onAction(() -> SettingWriter.open(player));
			},  "Drop Simulator", () -> {
				player.dialogueFactory.onAction(() -> DropSimulator.open(player));
			}, "Kill Logger", () -> {
				player.dialogueFactory.onAction(player.activityLogger::open);
			}, "Title Manager", () -> {
				player.dialogueFactory.onAction(() -> TitleManager.open(player));
			}, "Slayer Interface", () -> {
				player.dialogueFactory.onAction(() -> player.slayer.open(SlayerTab.MAIN));
			}).execute();
			break;
		/** here **/
		case 29423:
			InterfaceWriter.write(new AchievementInterface(player));
			player.interfaceManager.setSidebar(Config.QUEST_TAB, 35_000);
			// AchievementInterface.open(player);
			break;
		case 29426:
			DropDisplay.open(player);
			break;
		case 29432:
			player.send(new SendForceTab(Config.MUSIC_TAB));
			break;
		case 29411:

			InterfaceWriter.write(new InformationWriter(player));
			return true;
		case -30531:
		case -28219:
		case -30131:
			InterfaceWriter.write(new InformationWriter(player));
			player.interfaceManager.setSidebar(Config.QUEST_TAB, 29_400);
			return true;
		case 29413:
		case 29405:
		case -30528:
			InterfaceWriter.write(new QuestWriter(player));
			player.interfaceManager.setSidebar(Config.QUEST_TAB, 35_400);
			return true;
		case 29408:
		case -30128:
			InterfaceWriter.write(new AchievementInterface(player));
			player.interfaceManager.setSidebar(Config.QUEST_TAB, 35_000);
			return true;
		/*
		 * case 29410: player.interfaceManager.setSidebar(Config.QUEST_TAB, 37_300);
		 * break;
		 */
		case 29414:
		case -30525:
		case -30125:
			player.dialogueFactory.sendOption("Royalty program", () -> {
				player.dialogueFactory.onAction(() -> RoyaltyProgram.open(player));
			},  "Drop Simulator", () -> {
				player.dialogueFactory.onAction(() -> DropSimulator.open(player));
			}, "Activity Logger", () -> {
				player.dialogueFactory.onAction(player.activityLogger::open);
			}, "Title Manager", () -> {
				player.dialogueFactory.onAction(() -> TitleManager.open(player));
			}, "Slayer Interface", () -> {
				player.dialogueFactory.onAction(() -> player.slayer.open(SlayerTab.MAIN));
			}).execute();
			return true;
		}
		return false;
	}
}
