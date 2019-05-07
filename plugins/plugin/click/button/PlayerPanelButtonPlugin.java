package plugin.click.button;

import io.server.Config;
import io.server.content.playerguide.PlayerGuideHandler;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.teleport.TeleportHandler;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendForceTab;
import io.server.net.packet.out.SendMessage;

public class PlayerPanelButtonPlugin extends PluginContext {

	/**
	 * @author Adam/adameternal123
	 */

	protected boolean onClick(Player player, int button) {

		if (player.wilderness > 20 && !PlayerRight.isPriviledged(player)) {
			player.send(new SendMessage("You can't teleport above 20 wilderness!"));
			return false;
		}
		/*if(player.getCombat().inCombat()) {
			player.send(new SendMessage("You can't do this whilst in combat!"));
			return false;
		}*/
		if (button == -5115) {
			Teleportation.teleport(player, Config.STARTER_ZONE);
			player.send(new SendMessage("@blu@You have teleported to Starter Zone! All NPC's share the same drop table!"));
			player.send(new SendMessage("@red@ - 1/100 chance of starter V2 Box!"));
			player.send(new SendMessage("@red@ - 1/200 chance of Mystery Box!"));
			player.send(new SendMessage("@red@ - 1/500 chance of 10$ Bond!"));
		}
		if(button == -5107) {
			new PlayerGuideHandler().open(player);
		}
		if(button == -5111) {
			Teleportation.teleport(player, Config.MEDIUM_ZONE);
			player.send(new SendMessage("You have teleported to Medium Zone!"));
			player.send(new SendMessage("@red@ - 1/200 chance of Mbox!"));
			player.send(new SendMessage("@red@ - 1/350 chance of Eto Armour!"));
			player.send(new SendMessage("@red@ - 1/500 chance of 10$ Bond!"));

		}
		if(button == -5103) {
			player.send(new SendForceTab(Config.MUSIC_TAB));
		}
		if(button == -5099) {
			player.move(new Position(3086, 3485, 0));
			player.message("@red@ Click the portal to enter the minigame!");
			player.message("@red@best money maker on Brutal");
		}
		if(button == -5095) {
			player.dialogueFactory.sendOption("AFK-Mining", () -> {
				player.dialogueFactory.onAction(() -> player.move(new Position(2910, 4832, 0)));
			},  "AFK-Fishing", () -> {
				player.dialogueFactory.onAction(() -> Teleportation.teleport(player, Config.AFK_FISHING));
			}, "AFK-Woodcutting", () -> {
				player.dialogueFactory.onAction(() -> Teleportation.teleport(player, Config.AFK_WOODCUTTING));
			}, "AFK-Firemaking", () -> {
				player.dialogueFactory.onAction(() -> Teleportation.teleport(player, Config.AFK_FIREMAKING));
			}).execute();
		}
		if(button == -5091) {
			TeleportHandler.open(player);
		}
		if(button == -5803) {
			player.send(new SendForceTab(Config.MUSIC_TAB));
		}
		return false;
	}
	
}
