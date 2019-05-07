package io.server.content.itemaction.impl;

import io.server.content.emote.EmoteHandler;
import io.server.content.emote.EmoteUnlockable;
import io.server.content.itemaction.ItemAction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class MimeBox extends ItemAction {

	private static final EmoteUnlockable[] EMOTES = { EmoteUnlockable.GLASS_BOX, EmoteUnlockable.GLASS_WALL,
			EmoteUnlockable.LEAN, EmoteUnlockable.CLIMB_ROPE };

	private static final Item[] MIME_COSTUME = { new Item(3057), new Item(3058), new Item(3059), new Item(3060),
			new Item(3061) };

	@Override
	public String name() {
		return "Mime box";
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
				Item clothing = Utility.randomElement(MIME_COSTUME);
				player.inventory.add(clothing);
				player.send(new SendMessage("You have received the " + clothing.getName() + " item!"));
			} else {
				player.inventory.add(new Item(995, 75000));
			}
			break;
		case 3:
		case 4:
			player.inventory.add(Utility.randomElement(MIME_COSTUME));
			break;
		case 5:
			player.inventory.add(new Item(995, 75000));
			break;
		}
		return true;
	}
}
