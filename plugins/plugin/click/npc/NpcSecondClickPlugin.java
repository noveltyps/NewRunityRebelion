package plugin.click.npc;

import io.server.content.dialogue.impl.RealmLordDialogue;
import io.server.content.dialogue.impl.RoyalKingDialogue;
import io.server.content.masterminer.MasterMinerGUI;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.skill.impl.runecrafting.RunecraftTeleport;
import io.server.content.skill.impl.slayer.SlayerTab;
import io.server.content.store.Store;
import io.server.game.event.impl.NpcClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

public class NpcSecondClickPlugin extends PluginContext {

	@Override
	protected boolean secondClickNpc(Player player, NpcClickEvent event) {
		final int id = event.getNpc().id;
		switch (id) {
		/* King Royal dialogue */
		case 5523:
			player.dialogueFactory.sendDialogue(new RoyalKingDialogue(2));
			break;
		case 3893:
			new MasterMinerGUI(player).open();
			break;
			
		case 1389:
			Store.STORES.get("Referral Point Store").open(player);
			break;
			
		case 1143:
			Store.STORES.get("The Clanmaster's Store").open(player);
			break;

		case 394:
			player.bank.open();
			break;

		case 3220:
			player.dialogueFactory.sendOption("Chaos Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, RunecraftTeleport.CHAOS.getPosition(), 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Air Altar!, " + player.getName() + "!"));
					});
				}
			}, "Death Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, RunecraftTeleport.DEATH.getPosition(), 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Nature altar, " + player.getName() + "!"));
					});

				}
			}, "Soul Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, RunecraftTeleport.SOUL.getPosition(), 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Blood altar, " + player.getName() + "!"));
					});
				}
			}, "Fire Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, RunecraftTeleport.FIRE.getPosition(), 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Law Altar, " + player.getName() + "!"));
					});
				}
			}, "Nowhere", player.interfaceManager::close).execute();
			break;

		case 7481:
			Store.STORES.get("Brutal Vote Store").open(player);
			break;

		case 1602:
		case 1603:
			Store.STORES.get("Kolodion's Arena Store").open(player);
			break;

		case 506:
		case 507:
		case 513: // falador female
		case 512: // falador male
		case 1032:
			Store.STORES.get("The General Store").open(player);
			break;

		/* Emblem trader. */
		case 315:
			Store.STORES.get("The PvP Store").open(player);
			break;
		case 2898:
		case 2897:
			player.bank.open();
			break;

		case 311:
			if (PlayerRight.isIronman(player) || PlayerRight.isDeveloper(player)) {
				Store.STORES.get("Ironman Store").open(player);
			} else {
				player.message("you cannot access this store, unless you are ironman");

			}
			break;

		/* Nieve */
		case 490:
		case 6797:
			player.slayer.open(SlayerTab.MAIN);
			break;

		/* Realm Lord */
		case 15:
			RealmLordDialogue.enterBattleRealm(player.dialogueFactory);
			break;

		/* Zeke */
		case 527:
			Store.STORES.get("Zeke's Superior Scimitars").open(player);
			break;
		}
		return false;
	}

}
