package io.server.content.experiencerate;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;

/**
 * Adjusts the experience rate
 * 
 * @author Nerik#8690
 *
 */
public class ExperienceModifier {

	private Player player;

	public ExperienceModifier(Player player) {
		this.player = player;
	}

	public double getModifier() {
		for (ExperienceData experience : ExperienceData.values()) {
			if (experience.getMode().equals(player.right)) {
				return experience.getModifier();
			}
		}
		return 1.0;
	}

}
