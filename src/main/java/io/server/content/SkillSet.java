package io.server.content;

import java.util.Arrays;
import java.util.Optional;

import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.net.packet.out.SendInputAmount;

/**
 * Handles setting a combat skill.
 *
 * @author Daniel
 */
public class SkillSet {

	public enum SkillData {
		ATTACK(8654, Skill.ATTACK), STRENGTH(8657, Skill.STRENGTH), DEFENCE(8660, Skill.DEFENCE),
		RANGED(8663, Skill.RANGED), PRAYER(8666, Skill.PRAYER), MAGIC(8669, Skill.MAGIC),
		HITPOINTS(8655, Skill.HITPOINTS);

		private final int button;
		public final int skill;

		SkillData(int button, int skill) {
			this.button = button;
			this.skill = skill;
		}

		public static Optional<SkillData> forButton(int button) {
			return Arrays.stream(values()).filter(skillData -> skillData.button == button).findFirst();
		}
	}

	public static void set(Player player, SkillData data) {
		int max = player.achievedSkills[data.skill];
		player.send(new SendInputAmount("Enter your desired <col=255>" + Skill.getName(data.skill)
				+ "</col> level (1-<col=255>" + max + "</col>)", 2, input -> {
					int level = Integer.parseInt(input);
					if (player.skills.getLevel(data.skill) != player.skills.getMaxLevel(data.skill)) {
						player.dialogueFactory.sendStatement("Your level has to be fully restored before changing it.")
								.execute();
						return;
					}
					if (level > max) {
						player.dialogueFactory.sendStatement("You have not unlocked that level yet.",
								"You must manually achieve it first!").execute();
						return;
					}
					player.skills.setMaxLevel(data.skill, level);
					player.skills.setCombatLevel();
					player.updateFlags.add(UpdateFlag.APPEARANCE);
					player.dialogueFactory
							.sendStatement(
									"You have successfully set your <col=255>" + Skill.getName(data.skill)
											+ "</col> level",
									"to <col=255>" + level + "</col>.",
									"You will not earn any experience unless you revert back to your",
									"original level. However, the experience counter will still display.")
							.execute();
				}));
	}
}
