package plugin.itemon.npc;

import java.util.Random;

import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.game.event.impl.ItemOnNpcEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendMessage;

public class GamblerPlugin extends PluginContext {
	
	private Npc npc;
	private Item item;

	@Override
	protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
		if (event.getNpc().id != 1012) {
			return false;
		}
		DialogueFactory factory = player.dialogueFactory;
		item = event.getUsed();
		npc = event.getNpc();
		npc.face(player);

		if (item.isStackable()) {
			factory.sendNpcChat(1012, Expression.HAPPY, "Oooh some " + item.getName() + "...",
					"How many of those would you like to bet?");
			factory.onAction(() -> World.schedule(1,
					() -> player.send(new SendInputAmount("How many " + item.getName() + " would you like to bet?", 10,
							input -> bet(factory, Integer.parseInt(input))))));
			factory.execute();
		} else {
			factory.sendNpcChat(1012, Expression.HAPPY, "Oooh a " + item.getName() + "...", "Let's roll it!");
			bet(factory, 1); // BOOTLEG CODE
			factory.execute();
		}
		return true;
	}

	private void bet(DialogueFactory factory, int i) {
		factory.clear();
		Player player = factory.getPlayer();

		if (!player.inventory.contains(item.getId(), i)) {
			factory.sendNpcChat(1012, Expression.LAUGH, "You do not have enough!");
			factory.execute();
			return;
		}

		int roll = new Random().nextInt(100);
		npc.speak("You rolled a " + roll);
		if (roll >= 60) {
			player.inventory.add(item.getId(), i);
		} else {
			player.inventory.remove(item.getId(), i);
		}
		player.send(new SendMessage("You rolled a " + roll + "."));
	}
}
