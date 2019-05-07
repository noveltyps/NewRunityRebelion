package io.server.content.skill.impl.magic.spell.impl;

import java.util.Optional;

import io.server.Config;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.content.skill.impl.magic.spell.Spell;
import io.server.content.skill.impl.smithing.SmeltingData;
import io.server.game.Graphic;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendForceTab;

/**
 * The superheat spell
 * 
 * @author Daniel
 */
public class SuperHeat implements Spell {

	@Override
	public String getName() {
		return "Super heat";
	}

	@Override
	public int getLevel() {
		return 43;
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(554, 1), new Item(561, 1) };
	}

	@Override
	public void execute(Player player, Item item) {
		if (player.spellbook != Spellbook.MODERN)
			return;
		
		if (!player.spellCasting.castingDelay.elapsed(599))
			return;

		Optional<SmeltingData> data = SmeltingData.getDefinitionByItem(item.getId());
		
		if (!data.isPresent()) {
			player.message("You can not superheat this item!");
			return;
		}

		if (!player.inventory.containsAll(data.get().required)) {
			player.message("You do not contain the required items to super heat!");
			return;
		}

		if (player.skills.getMaxLevel(Skill.SMITHING) < data.get().requirement) {
			player.message("You need a smithing level of " + data.get().requirement + " to do super heat this item!");
			return;
		}

		player.animate(722);
		player.graphic(new Graphic(148, false));
		player.send(new SendForceTab(6));
		player.inventory.removeAll(data.get().required);
		player.inventory.addAll(data.get().produced);
		player.skills.addExperience(Skill.MAGIC,
				(53 * Config.MAGIC_MODIFICATION) * new ExperienceModifier(player).getModifier());
		player.skills.addExperience(Skill.SMITHING,
				(data.get().experience * Config.SMITHING_MODIFICATION) * new ExperienceModifier(player).getModifier());
		player.spellCasting.castingDelay.reset();
	}
}
