package plugin.click.npc;

import io.server.content.dialogue.impl.RoyalKingDialogue;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.skill.impl.runecrafting.RunecraftTeleport;
import io.server.content.skill.impl.slayer.SlayerOfferings;
import io.server.content.upgrading.UpgradeDisplay;
import io.server.game.event.impl.NpcClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

public class NpcThirdClickPlugin extends PluginContext {

	@Override
	protected boolean thirdClickNpc(Player player, NpcClickEvent event) {
		switch (event.getNpc().id) {
		case 5523:
			player.dialogueFactory.sendDialogue(new RoyalKingDialogue(1));
			break;
		case 490:
			SlayerOfferings.offer(player);
			break;
		case 311:
			player.playerAssistant.claimIronmanArmour();
			break;
		case 6797:
			player.dialogueFactory.sendOption("Upgrade an Item", () -> {
				new UpgradeDisplay(player).execute();
			}, "Nowhere", player.interfaceManager::close).execute();

			break;
		case 3220:
			player.dialogueFactory.sendOption("Mind Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, RunecraftTeleport.MIND.getPosition(), 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Air Altar!, " + player.getName() + "!"));
					});
				}
			}, "Earth Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, RunecraftTeleport.EARTH.getPosition(), 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Nature altar, " + player.getName() + "!"));
					});

				}
			}, "Cosmic Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, RunecraftTeleport.COSMIC.getPosition(), 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Blood altar, " + player.getName() + "!"));
					});
				}
			}, "Body Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, RunecraftTeleport.BODY.getPosition(), 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Law Altar, " + player.getName() + "!"));
					});
				}
			}, "Nowhere", player.interfaceManager::close).execute();
			break;
		}
		return false;
	}

}
