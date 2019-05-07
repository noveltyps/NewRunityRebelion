package io.server.content.skill.impl.magic.spell.impl;

import java.util.concurrent.TimeUnit;

import io.server.content.skill.impl.magic.Spellbook;
import io.server.content.skill.impl.magic.spell.Spell;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.listener.other.VengeanceListener;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendWidget;

/**
 * Handles the vengeance spell.
 */
public class Vengeance implements Spell {
	@Override
	public String getName() {
		return "Vengeance";
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(9075, 4), new Item(557, 10), new Item(560, 2) };
	}

	@Override
	public int getLevel() {
		return 94;
	}

	@Override
	public void execute(Player player, Item item) {
		if (player.spellbook != Spellbook.LUNAR)
			return;
		if (player.skills.getMaxLevel(Skill.DEFENCE) < 45) {
			player.send(new SendMessage("You need a defence level of 45 to cast this spell!"));
			return;
		}
		if (player.venged && !PlayerRight.isDeveloper(player)) {
			player.send(new SendMessage("You already have vengeance casted!"));
			return;
		}
		if (player.spellCasting.vengeanceDelay.elapsedTime(TimeUnit.SECONDS) < 30 && !PlayerRight.isDeveloper(player)) {
			player.send(new SendMessage("You can only cast vengeance once every 30 seconds."));
			return;
		}
		player.animate(new Animation(4410, UpdatePriority.HIGH));
		player.graphic(new Graphic(726, true));
		player.spellCasting.vengeanceDelay.reset();
		player.send(new SendWidget(SendWidget.WidgetType.VENGEANCE, 30));
		player.venged = true;
		player.getCombat().addListener(VengeanceListener.get());
	}
}
