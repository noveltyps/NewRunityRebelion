package io.server.content.skill.impl.magic.spell.impl;

import java.util.Arrays;

import io.server.Config;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.skill.impl.magic.Magic;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.content.skill.impl.magic.spell.Spell;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendForceTab;
import io.server.net.packet.out.SendMessage;

/**
 * The high alchemy spell.
 *
 * @author Daniel
 */
public class HighAlchemy implements Spell {

	@Override
	public String getName() {
		return "High alchemy";
	}

	@Override
	public int getLevel() {
		return 55;
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(554, 5), new Item(561, 1) };
	}

	@Override
	public void execute(Player player, Item item) {
		if (player.spellbook != Spellbook.MODERN)
			return;

		if (!player.spellCasting.castingDelay.elapsed(500)) {
			return;
		}

		if (Arrays.stream(Magic.UNALCHEABLES).anyMatch($it -> item.getId() == $it.getId())) {
			player.send(new SendMessage("You can not alch this item!"));
			return;
		}

		int value = item.getHighAlch();

		if (value > 100_000_000) {
			player.send(new SendMessage("The value of this item is too high and can not be high-alched."));
			return;
		}
		
		if(player.inventory.getFreeSlots() == 0) {
			player.message("You got no space left, store some of your items in your bank.");
			return;
		}

		player.animate(new Animation(713));
		player.graphic(new Graphic(113, true));
		player.inventory.remove(item.getId(), 1);
		player.inventory.removeAll(getRunes());
		player.inventory.add(Config.CURRENCY, value == 0 ? 1 : value);
		player.inventory.refresh();
		player.send(new SendForceTab(6));
		player.skills.addExperience(Skill.MAGIC,
				(65 * (Config.MAGIC_MODIFICATION)) * new ExperienceModifier(player).getModifier());
		AchievementHandler.activate(player, AchievementKey.HIGH_ALCHEMY, 1);
		player.spellCasting.castingDelay.reset();
		player.action.clearNonWalkableActions();
		RandomEventHandler.trigger(player);
	}
}
