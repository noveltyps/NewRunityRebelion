package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;

/**
 * 
 * @author Adam_#6723
 *
 */

public class PetCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.sendMessage("looping..");
		for (int i = 0; i < rewards().length; i++) {
			if (rewards()[i] != null) {
				player.bank.deposit(rewards()[i]);
			}
		}
		player.sendMessage("finished....");
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}
	
	protected Item[] rewards() {
        return new Item[]{
                new Item(13247, 1),
                new Item(20851, 1),
                new Item(12653, 1),
                new Item(12651, 1),
                new Item(12643, 1),
                new Item(12644, 1),
                new Item(12645, 1),
                new Item(12646, 1),
                new Item(12647, 1),
                new Item(12648, 1),
                new Item(12649, 1),
                new Item(12650, 1),
                new Item(12652, 1),
                new Item(12654, 1),
                new Item(12655, 1),
                new Item(13178, 1),
                new Item(13177, 1),
                new Item(13179, 1),
                new Item(13181, 1),
                new Item(11995, 1),
                new Item(12921, 1),
                new Item(12816, 1),
                new Item(13225, 1),
                new Item(13321, 1),
                new Item(13320, 1),
                new Item(13323, 1),
                new Item(20659, 1),
                new Item(20661, 1),
                new Item(20663, 1),
                new Item(20665, 1),
                new Item(13322, 1),
                new Item( 9105,1),
				new Item( 8133,1),
				new Item( 8134,1),				
				new Item( 7420,1),
				new Item(21511 ,1),				
				new Item(21509 ,1),
				new Item(13262 ,1),
				new Item(4149 ,1),				
				new Item(7505 ,1),
				new Item(21992 ,1),
				new Item(20693 ,1),
				new Item(21273 ,1),
				new Item( 20851 ,1),
				new Item( 13247,1),				
				new Item( 20693,1),
				new Item(  20693,1),
				new Item( 21992,1),				
				new Item( 7505,1),
				new Item( 4149,1),
				new Item( 13262,1),				
				new Item( 21509,1),
				new Item(  21511,1),
				new Item(  7420,1)

        };
    }

	@Override
	public String description() {
		return "Spawns and deposits all pets into your bank.";
	}
}
