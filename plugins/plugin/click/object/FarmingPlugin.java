package plugin.click.object;

import io.server.game.event.impl.ItemOnObjectEvent;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.object.GameObject;
import io.server.net.packet.out.SendMessage;

/**
 * @author Ethan Kyle Millard <skype:pumpklins>
 * @since Sun, May 20, 2018 @ 1:48 PM
 * @edited and fixed by - Adam_#6723
 */
public class FarmingPlugin extends PluginContext {

	@Override
	protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
		Item item = event.getUsed();
		GameObject object = event.getObject();
		if (player.getFarming().fillWateringCans(item.getId(), object))
			return true;
		if (player.getFarming().plant(item.getId(), object.getX(), object.getY()))
			return true;
		if (player.getFarming().useItemOnPlant(item.getId(), object.getX(), object.getY()))
			return true;
		switch (event.getObject().getId()) {
		case 7836:
		case 7808:
			int amt = player.inventory.getAmount(6055);
			if (amt > 0) {
				player.inventory.remove(6055, amt);
				player.send(new SendMessage("You put the weed in the compost bin."));
				player.skills.addExperience(Skill.FARMING, 20 * amt);
			} else {
				player.send(new SendMessage("You do not have any weeds in your inventory."));
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean firstClickObject(Player player, ObjectClickEvent event) {
		if (player.getFarming().click(player, event.getObject().getX(), event.getObject().getY(), 1))
			return true;
		return false;
	}
}
