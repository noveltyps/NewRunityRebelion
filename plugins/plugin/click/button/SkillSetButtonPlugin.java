package plugin.click.button;

import io.server.content.SkillSet;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.position.Area;
import io.server.net.packet.out.SendMessage;

public class SkillSetButtonPlugin extends PluginContext {

	/*
	 * (author adam trinity)
	 * 
	 * @see
	 * io.battlerune.game.plugin.PluginContext#onClick(io.battlerune.game.world.
	 * entity.mob.player.Player, int)
	 */
	@Override
	protected boolean onClick(Player player, int button) {

		if (!SkillSet.SkillData.forButton(button).isPresent()) {
			return false;
		}
		if (Area.inWilderness(player)) {
			player.send(new SendMessage("You can not do this whilst in the wilderness."));
			return true;
		}
		if (player.getCombat().inCombat()) {
			player.send(new SendMessage("You can not do this whilst in combat."));
			return true;
		}
		if (!player.equipment.isEmpty()) {
			player.message("Please remove all your equipment before doing this!");
			return true;
		}
		if (!PlayerRight.isDonator(player)) {
			player.message("You have to be a Donator to be able to set your Melee combat stats!");
			return true;
		}
		// if(player.right.isDeveloper(player)) {

		SkillSet.SkillData data = SkillSet.SkillData.forButton(button).get();
		player.dialogueFactory.sendOption("Set desired level",
				() -> player.dialogueFactory.onAction(() -> SkillSet.set(player, data)), "Set regular level",
				() -> player.dialogueFactory.onAction(() -> {
					player.skills.setMaxLevel(data.skill, player.achievedSkills[data.skill]);
					player.skills.setExperience(data.skill, player.achievedExp[data.skill]);
					player.skills.refresh(data.skill);
					player.skills.setCombatLevel();
					player.updateFlags.add(UpdateFlag.APPEARANCE);
					player.dialogueFactory.sendStatement(
							"You have successfully set your <col=255>" + Skill.getName(data.skill) + "</col> level",
							"back to <col=255>" + player.skills.getMaxLevel(data.skill) + "</col>.",
							"You will gain experience now (unless manually toggled off).").execute();
				}), "How does this work?", () -> {
				}).execute();
		// }
		return true;

	}
}
