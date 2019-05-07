package plugin.click.item;

import io.server.content.skill.impl.hunter.net.HunterNetting;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class ItemThirdClickPlugin extends PluginContext {

	@Override
	protected boolean thirdClickItem(Player player, ItemClickEvent event) {//forgot which one it was lol its 1 of the 3 right?
		//yea kk well we good now xd u did all 3 brb
		if ((event.getItem().getId() >= 11238 && event.getItem().getId() <= 11256) || event.getItem().getId() == 19732)
			HunterNetting.lootJar(player, event.getItem());
		switch (event.getItem().getId()) {

        case 15098:
        	player.inventory.replace(15098, 15099, true);
        	player.gambling.resetBlackjack();
        	break;
        case 15099:
        	player.inventory.replace(15099, 15098, true);
        	player.gambling.resetBlackjack();
        	break;
        case 15100:
        	player.inventory.replace(15100, 15098, true);
        	player.gambling.resetBlackjack();
        	break;
        	
		case 12418:
			player.message("@red@To Dismantle this item, use a chisel on the item!");
			break;

		case 4079:
			player.animate(1459);
			break;

		case 12436:
			player.send(new SendMessage("You have failed to slash the web apart."));

			break;

		/* Ring of recoil. */
		case 2550:
			int charges = player.ringOfRecoil;

			if (charges >= 40) {
				player.dialogueFactory
						.sendStatement("<col=A11A1A>Ring of recoil", "Your ring already has the maximum charges.")
						.execute();
				break;
			}

			player.dialogueFactory.sendStatement("<col=A11A1A>Ring of recoil",
					"You currently have <col=255>" + charges + "</col> charges until the ring breaks.",
					"Are you sure you would like to break the ring?").sendOption("Yes", () -> {
						player.ringOfRecoil = 40;
						player.inventory.remove(event.getItem());
						player.interfaceManager.close();
						player.send(new SendMessage(
								"<col=9A289E>Your ring has been drained of all charges; causing it to break."));
					}, "No", player.interfaceManager::close).execute();
			break;

		/* Enchanted Gem */
		case 4155:
			player.message("If you see this message, please tell Red Bracket (Developer) what you did!");
			/*
			 * player.message(player.slayer.partner == null ?
			 * "You don't have a slayer partner! Use the gem on another player to get one!"
			 * : "Your current slayer partner is: " + player.slayer.partner.getName());
			 */
			break;

		default:
			return false;

		}
		return true;
	}

}
