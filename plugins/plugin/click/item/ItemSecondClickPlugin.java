package plugin.click.item;

import io.server.Config;
import io.server.content.Gambling.RollType;
import io.server.content.skill.impl.hunter.net.HunterNetting;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

public class ItemSecondClickPlugin extends PluginContext {
//	was not saved? no try now il restart server :P
	@Override
	protected boolean secondClickItem(Player player, ItemClickEvent event) {
		if ((event.getItem().getId() >= 11238 && event.getItem().getId() <= 11256) || event.getItem().getId() == 19732)
			HunterNetting.lootJar(player, event.getItem());
		switch (event.getItem().getId()) {
		
		case 15100:
			player.gambling.rollDice(0, RollType.BLACKJACK_STAY);
			break;

		case 15098:
			player.gambling.rollDice(100, RollType.CLAN);
			break;

		case 4079:
			player.animate(1458);
			break;

		case 12414:
		case 12418:
		case 11802:
		case 11804:
		case 11806:
		case 11808:
		case 12436:
		case 12415:
		case 12417:
		case 20368:
		case 20370:
		case 20372:
		case 20374:
		case 12416:
		case 20000:
		case 20366:
		case 19720:
		case 19722:
			player.message("@red@To Dismantle this item, use a chisel on the item!");
			break;
		case 13342:
		case 20760:
		case 13280:

			if (player.inventory.contains(13280)) {

				player.dialogueFactory.sendOption("Donator Portal", () -> {
					if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
						player.message("@or2@you can't teleport above 30 wilderness");
					} else {
						Teleportation.teleport(player, Config.PORTAL_ZONE, 20, () -> {
							player.send(new SendMessage(
									"@or2@Welcome to the Grotesque Guardians!, " + player.getName() + "!"));
						});
					}
				}, "Karamja", () -> {
					if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
						player.message("@or2@you can't teleport above 30 wilderness");
					} else {
						Teleportation.teleport(player, Config.KARAMJA, 20, () -> {
							player.send(new SendMessage("@or2@Welcome to the Karamja, " + player.getName() + "!"));
						});

					}
				}, "Draynor Village", () -> {
					if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
						player.message("@or2@you can't teleport above 30 wilderness");
					} else {
						Teleportation.teleport(player, Config.DRAYNOR, 20, () -> {
							player.send(new SendMessage("@or2@Welcome to the Draynor, " + player.getName() + "!"));
						});
					}
				}, "Al-Kahrid", () -> {
					if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
						player.message("@or2@you can't teleport above 30 wilderness");
					} else {
						Teleportation.teleport(player, Config.AL_KHARID, 20, () -> {
							player.send(new SendMessage("@or2@Welcome to the Al Kharid, " + player.getName() + "!"));
						});
					}
				}, "Nowhere", player.interfaceManager::close).execute();
			}
			break;

		/* Enchanted Gem */
		case 4155:
			player.message(player.slayer.partner == null
					? "You don't have a slayer partner! Use the gem on another player to get one!"
					: "Your current slayer partner is: " + player.slayer.partner.getName());
			break;

		default:
			return false;
		}

		return true;
	}

}
