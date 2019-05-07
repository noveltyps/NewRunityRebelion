package io.server.util.parser.old.defs;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.SkillRequirement;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Represents the requirements for equipping items.
 * 
 * @author Seven
 */
public class EquipmentRequirement extends SkillRequirement {

	/**
	 * Creates a new {@link EquipmentRequirement}.
	 *
	 * @param level The level required
	 *
	 * @param skill The skill required.
	 */
	public EquipmentRequirement(int level, int skill) {
		super(level, skill);
	}

	/**
	 * Determines if a player can equip a specified item.
	 * 
	 * @param player The player that is equipping the item.
	 * 
	 * @param itemId The id of the item to check.
	 */
	public static boolean canEquip(Player player, int itemId) {
		final EquipmentDefinition req = EquipmentDefinition.EQUIPMENT_DEFINITIONS.get(itemId);
		if (req == null)
			return true;

		for (final SkillRequirement r : req.getRequirements()) {
			if (r == null)
				continue;

			if (player.skills.getMaxLevel(r.getSkill()) < r.getLevel()) {
				player.send(new SendMessage("You need " + Utility.getAOrAn(Skill.getName(r.getSkill())) + " "
						+ Skill.getName(r.getSkill()) + " level of " + r.getLevel() + " to equip this item."));
				return false;
			}
		}
		return true;
	}

}
