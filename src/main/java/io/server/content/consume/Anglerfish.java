package io.server.content.consume;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723 Handles the overhealing of anglerfish, don't ask why i
 *         created seperate class...didn't wanna touch the aids they call
 *         "eat.java".
 *
 */

public class Anglerfish {

	/**
	 * The method that executes the Angler fish actionn.
	 *
	 * @param player the player to do this action for. This method only executes
	 *               after they've elapsed the food delay tier.
	 * @param slot 
	 */
	public static void onAnglerEffect(Player player, int slot) {
		if (player.foodDelay.elapsed(1300)) {
			modifySkill(player, Skill.HITPOINTS, 0.21, 2);
			player.animate(new Animation(829, UpdatePriority.LOW));
			player.getCombat().reset();
			player.getCombat().cooldown(2);
			player.foodDelay.reset();
			player.inventory.remove(new Item(13441, 1), slot);
			player.skills.refresh(Skill.HITPOINTS);
			player.send(new SendMessage("You eat the " + "Anglerfish" + "."));
		}

	}

	/**
	 * The method that executes the basic effect food action that will append the
	 * level of {@code skill}.
	 *
	 * @param player the player to do this action for.
	 */
	public static void modifySkill(Player player, int skill, double percentage, int base) {
		Skill s = player.skills.get(skill);
		int realLevel = s.getMaxLevel();

		final int boostLevel = (int) (realLevel * percentage + base);

		int cap = s.getLevel();
		if (cap < realLevel + boostLevel) {
			cap = realLevel + boostLevel;
		}

		if (skill == Skill.HITPOINTS && boostLevel < 0) {
			int damage = boostLevel;
			if (player.getCurrentHealth() + damage <= 0)
				damage = -player.getCurrentHealth() + 1;
			player.damage(new Hit(-damage));
		} else {
			player.skills.get(skill).modifyLevel(level -> level + boostLevel, 0, cap, false);
		}

		player.skills.refresh(skill);
	}

}
