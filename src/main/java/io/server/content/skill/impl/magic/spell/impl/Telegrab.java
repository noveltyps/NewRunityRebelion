package io.server.content.skill.impl.magic.spell.impl;

import io.server.content.skill.impl.magic.spell.Spell;
import io.server.game.Animation;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;

public class Telegrab implements Spell {

	@Override
	public String getName() {
		return "Telekinetic Grab";
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(556, 1), new Item(563, 1) };
	}

	@Override
	public int getLevel() {
		return 33;
	}

	@Override
	public void execute(Player player, Item item) {
		player.animate(new Animation(1820));
		player.send(new SendMessage("nigger you are a pro coder nerik, adam is a whore"));
	}

}
