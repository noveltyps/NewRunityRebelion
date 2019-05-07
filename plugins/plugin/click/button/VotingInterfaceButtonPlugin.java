package plugin.click.button;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;
/**
 * 
 * @author Adam_#6723
 *
 */

public class VotingInterfaceButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		String text = "";
		switch (button) {
		case 16403:
			text = "You recieve\\n1,000,0 Million\\nCoins!\\nDonators recieve\\n2,000,0 Million";
			player.send(new SendString(text, 16421));
			clicked = 0;
			break;
		case 16406:
			text = "You recieve 200\\nAnglerfish";
			player.send(new SendString(text, 16421));
			clicked = 1;
			break;
		case 16409:
			text = "You recieve a\\nDouble Exp Ticket";
			player.send(new SendString(text, 16421));
			clicked = 2;
			break;
		case 16412:
			text = "You recieve a\\nCrystal Key";
			player.send(new SendString(text, 16421));
			clicked = 3;
			break;
		case 16415:
			text = "You recieve a\\nMystery Box";
			player.send(new SendString(text, 16421));
			clicked = 4;
			break;
		case 16418:
			clicked = 5;
			text = "You recieve a\\nScrach Card\\nNote 1/5 Chance\\nOf Scratc Card";
			player.send(new SendString(text, 16421));
			player.sendMessage("NOTE: THERE'S 1/5 CHANCE OF OBTAINING A SCRATCH CARD!!!");
			break;
		case 16422:
			claim(player);
			break;
		}
		player.send(new SendString(text, 16421));
		return false;
	}
	
	
	/**
	 * Checks the last button they've clicked.
	 */
	int clicked = -1;

	/**
	 * 
	 * @param player
	 * @param clicked
	 * @return Depending on the clicked value, it'll execute something.
	 */

	public void claim(Player player) {		
		if(!player.inventory.contains(new Item(7478, 1))) {
			return;
		}
		player.inventory.remove(new Item(7478, 1));
		switch (clicked) {
		case 0:
			if(PlayerRight.isDonator(player)) {
				player.sendMessage("You claimed 1 Million coins from your Vote book.");
				player.inventory.add(new Item(995, 1000000));
			} else {
				player.sendMessage("You claimed 2 Million coins from your Vote book.");
				player.inventory.add(new Item(995, 2000000));
			}
			break;
		case 1:
			player.sendMessage("You claimed 200 Anglerfish from your Vote book.");
			player.inventory.add(new Item(13442, 200));	
			break;
		case 2:
			player.sendMessage("You recieve a tradeble double exp ticket from your Vote book.");
			player.inventory.add(new Item(2528, 1));
			break;
		case 3:			
			player.sendMessage("You recieve a crystal key chest from your Vote book.");
			player.inventory.add(new Item(989, 1));
			break;
		case 4:
			player.sendMessage("You recieve a mystery box from your Vote book.");
			player.inventory.add(new Item(6199, 1));
			break;
		case 5:
			if(Utility.random(1, 5) == 1) {
				player.sendMessage("You recieve a scratch card from the vote token");
				player.inventory.add(new Item(455, 1));
			} else {
				player.sendMessage("Better luck next time!");
			}
			break;
		}
		player.interfaceManager.close();
	}
}
