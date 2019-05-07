package io.server.content.itemaction.impl;

import io.server.content.emote.EmoteHandler;
import io.server.content.emote.EmoteUnlockable;
import io.server.content.itemaction.ItemAction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class DrillDemonBox extends ItemAction {

	private static final EmoteUnlockable[] EMOTES = { EmoteUnlockable.SIT_UP, EmoteUnlockable.PUSH_UP,
			EmoteUnlockable.JUMPING_JACK, EmoteUnlockable.JOG };

	private static final Item[] CAMO_CLOTHING = { new Item(6656), new Item(6654), new Item(6655) };

	@Override
	public String name() {
		return "Drill demon box";
	}

	@Override
	public String message(Item item) {
		return "You open the Drill demon box...";
	}

	@Override
	public int delay() {
		return 2;
	}

	@Override
	public boolean inventory(Player player, Item item, int opcode) {
		if (opcode != 1)
			return false;

		player.inventory.remove(item);
		int random = Utility.random(1, 5);

		switch (random) {
		case 1:
		case 2:
			if (!EmoteHandler.containsAll(player, EMOTES)) {
				EmoteUnlockable emote = EmoteHandler.selectRandom(player, EMOTES);
				player.emoteUnlockable.add(emote);
				EmoteHandler.refresh(player);
				player.send(new SendMessage("You have unlocked the "
						+ Utility.formatName(emote.name().toLowerCase().replace("_", "")) + " emote!"));
				return true;
			}
			int random2 = Utility.random(1, 2);
			if (random2 == 1) {
				Item clothing = Utility.randomElement(CAMO_CLOTHING);
				player.inventory.add(clothing);
				player.send(new SendMessage("You have received the " + clothing.getName() + " item!"));
			} else {
				player.inventory.add(new Item(995, 75000));
			}
			break;
		case 3:
		case 4:
			player.inventory.add(Utility.randomElement(CAMO_CLOTHING));
			break;
		case 5:
			player.inventory.add(new Item(995, 75000));
			break;
		}
		return true;
	}
}
