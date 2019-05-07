package io.server.game.world.entity.skill;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.google.common.primitives.Doubles;

import io.server.content.WellOfGoodwill;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
import io.server.content.event.InteractionEvent;
import io.server.content.skill.impl.cooking.Cooking;
import io.server.content.skill.impl.crafting.Crafting;
import io.server.content.skill.impl.firemaking.Firemaking;
import io.server.content.skill.impl.fishing.Fishing;
import io.server.content.skill.impl.fletching.Fletching;
import io.server.content.skill.impl.herblore.Herblore;
import io.server.content.skill.impl.hunter.Hunter;
import io.server.content.skill.impl.mining.Mining;
import io.server.content.skill.impl.prayer.BoneSacrifice;
import io.server.content.skill.impl.runecrafting.Runecraft;
import io.server.content.skill.impl.smithing.Smelting;
import io.server.content.skill.impl.smithing.Smithing;
import io.server.content.skill.impl.thieving.Thieving;
import io.server.content.skill.impl.woodcutting.Woodcutting;
import io.server.content.worldevent.WorldEvent;
import io.server.content.worldevent.WorldEventType;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.World;
import io.server.game.world.entity.EntityType;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.net.packet.out.SendChatBoxInterface;
import io.server.net.packet.out.SendExpCounter;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendSkill;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

/**
 * Manages all skills related to an mob.
 *
 * @author Michael | Chex
 * @Edited by Adam / Adameternal123
 */
public class SkillManager {

	/** The mob to manage for. */
	private final Mob mob;

	/** The experience counter. */
	public double experienceCounter;

	/** An array of skills that belong to an mob. */
	private Skill[] skills;

	/** The mob's combat level. */
	private double combatLevel;

	/** Constructs a new {@code SkillManager} object. */
	public SkillManager(Mob mob) {
		this.mob = mob;
		this.skills = new Skill[mob.isPlayer() ? Skill.SKILL_COUNT : 7];
		for (int index = 0; index < skills.length; index++) {
			boolean hitpoints = mob.isPlayer() && index == 3;
			skills[index] = hitpoints ? new Skill(index, 10, 1154) : new Skill(index, 1, 0);
		}
		if (mob.isPlayer()) {
			skills[Skill.HUNTER] = new Hunter(1, 0);
			skills[Skill.COOKING] = new Cooking(1, 0);
			skills[Skill.HERBLORE] = new Herblore(1, 0);
			skills[Skill.CRAFTING] = new Crafting(1, 0);
			skills[Skill.THIEVING] = new Thieving(1, 0);
			skills[Skill.FLETCHING] = new Fletching(1, 0);
			skills[Skill.PRAYER] = new BoneSacrifice(1, 0);
			skills[Skill.FIREMAKING] = new Firemaking(1, 0);
			skills[Skill.RUNECRAFTING] = new Runecraft(1, 0);
			skills[Skill.MINING] = new Mining(1, 0);
			skills[Skill.WOODCUTTING] = new Woodcutting(1, 0);
			skills[Skill.SMITHING] = new Smithing(1, 0);
			skills[Skill.FISHING] = new Fishing(1, 0);
		}
	}

	/** Calculates the combat level of an mob. */
	public static double calculateCombat(int attack, int defence, int strength, int hp, int prayer, int ranged,
			int magic) {
		final double base_calculation = .25 * (defence + hp + Math.floor(prayer / 2));
		final double melee_calculation = .325 * (attack + strength);
		final double range_calculation = .325 * (Math.floor(ranged / 2) + ranged);
		final double magic_calculation = .325 * (Math.floor(magic / 2) + magic);
		return Math.floor(base_calculation + Doubles.max(melee_calculation, range_calculation, magic_calculation));
	}

	/** Gets the skill for an id. */
	public Skill get(int id) {
		if (id < 0 || id >= skills.length) {
			throw new IllegalArgumentException("The skill id is out of bounds! id=" + id);
		}
		return skills[id];
	}

	/** Gets the level of a skill. */
	public int getLevel(int id) {
		return get(id).getLevel();
	}

	/** Gets the highest possible level of a skill. */
	public int getMaxLevel(int id) {
		return get(id).getMaxLevel();
	}

	/** Sets the level of a skill. */
	public void setLevel(int id, int level) {
		get(id).setLevel(level);
		refresh(id);
	}

	/** Sets the max level of a skill. */
	public void setMaxLevel(int id, int level) {
		setExperience(id, Skill.getExperienceForLevel(level));
		if (mob.isPlayer() && id <= Skill.MAGIC && !mob.getPlayer().quickPrayers.getEnabled().isEmpty()) {
			List<Prayer> deactivate = new LinkedList<>();
			for (Prayer prayer : mob.getPlayer().quickPrayers.getEnabled())
				if (!prayer.canToggle(mob.getPlayer()))
					deactivate.add(prayer);
			if (!deactivate.isEmpty())
				mob.getPlayer().quickPrayers.deactivate(deactivate.toArray(new Prayer[deactivate.size()]));
		}
	}

	/** Sets the experience for a skill. */
	public void setExperience(int id, double experience) {
		Skill skill = get(id);
		int level = Skill.getLevelForExperience(experience);
		skill.setLevel(level);
		skill.setMaxLevel(level);
		skill.setExperience(experience);
		refresh(id);
	}

	/** Sets the experience for a npc skill. */
	public void setNpcMaxLevel(int id, int level) {
		Skill skill = get(id);
		skill.setLevel(level);
		skill.setMaxLevel(level);
	}

	/** Sets the level of a skill. */
	public void modifyLevel(Function<Integer, Integer> modification, int id, int lowerBound, int upperBound) {
		Skill skill = get(id);
		skill.modifyLevel(modification, lowerBound, upperBound, false);
	}

	/** Handles regressing the skills. */
	public void regress(int skill) {
		Skill s = get(skill);
		if (s.getLevel() > s.getMaxLevel()) {
			s.modifyLevel(level -> level - 1, 0, s.getLevel(), false);
			refresh(skill);
		} else if (s.getLevel() < s.getMaxLevel()) {
			s.modifyLevel(level -> level + 1, 0, s.getMaxLevel(), false);
			refresh(skill);
		}
	}

	/** Sets the level of a skill. */
	public void modifyLevel(Function<Integer, Integer> modification, int id) {
		Skill skill = get(id);
		skill.modifyLevel(modification, false);
	}

	/** Gets the total level of the mob. */
	public int getTotalLevel() {
		int total = 0;
		for (Skill skill : skills) {
			total += skill.getMaxLevel();
		}
		return total;
	}

	public long getTotalXp() {
		long totalXp = 0;
		for (Skill skill : skills) {
			totalXp += skill.getExperience();
		}
		return totalXp;
	}

	/** Refreshes all the skills for the mob. */
	public void refresh() {
		for (final Skill skill : skills) {
			refresh(skill.getSkill());
		}
	}

	/** Restores a specific skill. */
	public void restore(int id) {
		Skill skill = get(id);
		skill.setLevel(skill.getMaxLevel());
		refresh(id);
	}

	/** Restores all the skills. */
	public void restoreAll() {
		IntStream.range(0, skills.length).forEach(this::restore);
	}

	/** Handles a player logging in. */
	public void login() {
		Smelting.clearInterfaces(mob.getPlayer());
		refresh();
		setCombatLevel();
	}

	/** Handles mastering all skills. Only players should access this. */
	public void master() {
		for (int index = 0; index < Skill.SKILL_COUNT; index++) {
			setMaxLevel(index, 99);
			if (index < 7) {
				mob.getPlayer().achievedSkills[index] = 99;
				mob.getPlayer().achievedExp[index] = 200000000;
			}

		}
		mob.getPlayer().send(new SendMessage("You have successfully mastered all skills."));
		setCombatLevel();
		mob.updateFlags.add(UpdateFlag.APPEARANCE);
	}

	/** Refreshes a skill to a player's client if this class's mob is a player. */
	public void refresh(int id) {
		if (mob.isPlayer()) {
			Skill skill = get(id);
			mob.getPlayer().send(new SendSkill(skill));
		}
	}

	/** Checks if the mob is maxed in all skills. */
	public boolean isMaxed() {
		int maxCount = Skill.SKILL_COUNT;
		int count = 0;
		for (int index = 0; index < maxCount; index++) {
			if (getMaxLevel(index) >= 99) {
				count++;
			} else {
				if (index == Skill.HUNTER)
					count++;
				if (index == Skill.FARMING)
					count++;
				if (index == Skill.CONSTRUCTION)
					count++;
			}
		}
		return count == maxCount;
	}

	/** The interactionEvent listener. */
	public boolean onEvent(InteractionEvent interactionEvent) {
		if (mob.is(EntityType.PLAYER)) {
			Player player = (Player) mob;
			boolean success = false;
			for (final Skill skill : skills) {
				success |= skill.onEvent(player, interactionEvent);
			}
			return success;
		}
		return false;
	}
	

	/**
	 * Adds experience to a given skill. The skill level will increase if the
	 * experience causes the skill to level up.
	 */
	public void addExperience(int id, double experience) {
		if ((id >= 0 && id <= 6) && id != 5) {
			addExperience (id, experience * 4, true);
		} else {
			addExperience(id, experience, true);
		}
	}

	/**
	 * Adds experience to a given skill. The skill level will increase if the
	 * experience causes the skill to level up.
	 */
	public void addExperience(int id, double experience, boolean levelUp) {
		addExperience(id, experience, levelUp, true);
	}

	/**
	 * Adds experience to a given skill. The skill level will increase if the
	 * experience causes the skill to level up.
	 */
	public void addExperience(int id, double experience, boolean levelUp, boolean counter) {
		if (!mob.isPlayer() || mob.getPlayer().settings.lockExperience)
			return;
		
		
		Player player = (Player) mob;
		Skill skill = get(id);
		double old = skill.getExperience();

		WorldEvent event = World.eventHandler.getEvent(WorldEventType.EXP);
		
		if (event != null)
			experience *= event.getModifier();
		
		if (WellOfGoodwill.isActive())
			experience *= 1.5;
		
		int maxLevel = Skill.getLevelForExperience(old);
		SkillData skillData = SkillData.values()[id];
		boolean combatSkill = skillData.isCombatSkill();

		if (counter) {
			experienceCounter += experience;
		}

		player.send(new SendExpCounter(skill.getSkill(), (int) experience, counter));

		if (combatSkill && skill.getMaxLevel() < player.achievedSkills[id]) {
			return;
		}

		int newMax = Skill.getLevelForExperience(skill.addExperience(experience));
		updateSkill(player, maxLevel, newMax, skill, levelUp);

		if (skill.getExperience() >= 200_000_000) {
			AchievementHandler.activate(player, AchievementKey.EXPERIENCE_MASTERY, 1);
		}

		if (combatSkill) {
			player.achievedExp[id] += experience;
			if (maxLevel < 99 && newMax != maxLevel) {
				player.achievedSkills[id] = skill.getMaxLevel();
			}
			updateCombat();
			mob.updateFlags.add(UpdateFlag.APPEARANCE);
		} else {
//            ClanManager.addExperience(player, experience);
		}

		player.send(new SendSkill(skill));
	}

	/** Handles updating a skill. */
	private void updateSkill(Player player, int maxLevel, int newMax, Skill skill, boolean levelUp) {
		if (maxLevel < 99 && newMax != maxLevel) {
			skill.setMaxLevel(newMax);
			skill.modifyLevel(level -> (newMax - maxLevel) + level, levelUp);
			if (levelUp) {
				// adam should disable levling up stopping skills anaomly
				// player.action.clearNonWalkableActions(); // causes flax task to stop upon
				// leveling up
				showLevelUpInterface(player, skill);
				if (newMax == 99)
					World.sendMessage("<col=7B44B3>RebelionX: <col=" + player.right.getColor() + ">" + player.getName()
							+ " </col>has just reached level 99 in <col=7B44B3>" + Skill.getName(skill.getSkill())
							+ "</col>!");
			} else {
				player.send(new SendMessage(
						"Congratulations, you have reached " + Utility.getAOrAn(Skill.getName(skill.getSkill())) + " "
								+ Skill.getName(skill.getSkill()) + " level of " + skill.getMaxLevel() + "."));
			}
			if (skill.getMaxLevel() == 99)
				AchievementHandler.activate(player, AchievementKey.SKILL_MASTERY, 1);
		}
	}

	private void showLevelUpInterface(Player player, Skill skill) {
		SkillData skillData = SkillData.values()[skill.getSkill()];
		String line1 = "Congratulations! You've just advanced " + Utility.getAOrAn(skillData.toString()) + " "
				+ skillData + " level!";
		String line2 = "You have reached level " + skill.getMaxLevel() + "!";
		player.send(new SendString(line1, skillData.getFirstLine()));
		player.send(new SendString(line2, skillData.getSecondLine()));
		player.send(new SendChatBoxInterface(skillData.getChatbox()));
		player.send(new SendMessage("Congratulations, you just advanced " + Utility.getAOrAn(skillData.toString()) + " "
				+ skillData + " level."));
		player.graphic(new Graphic(199, UpdatePriority.VERY_HIGH));
		player.dialogueFactory.setActive(true);
	}

	/** Handles updating the combat level. */
	private void updateCombat() {
		int oldLevel = (int) getCombatLevel();
		setCombatLevel();
		int newLevel = (int) getCombatLevel();
		if (newLevel != oldLevel)
			mob.getPlayer().send(new SendMessage("You've reached a combat level of " + newLevel + "."));
	}

	/**
	 * Sends all skills to this player's client. If the mob created with this object
	 * is not a player, no code will be executed.
	 */
	public void sendSkills() {
		if (mob.is(EntityType.PLAYER)) {
			Player player = (Player) mob;
			for (Skill skill : skills) {
				player.send(new SendSkill(skill));
			}
		}
	}

	public void resetSkilling() {
		boolean doingSkill = false;
		for (Skill skill : skills) {
			if (skill.isDoingSkill()) {
				doingSkill = true;
				skill.setDoingSkill(false);
			}
		}
		if (doingSkill) {
			mob.resetAnimation();
		}
	}

	/** Calculates the combat level of an mob. */
	public double calculateCombat() {
		return calculateCombat(getMaxLevel(Skill.ATTACK), getMaxLevel(Skill.DEFENCE), getMaxLevel(Skill.STRENGTH),
				getMaxLevel(Skill.HITPOINTS), getMaxLevel(Skill.PRAYER), getMaxLevel(Skill.RANGED),
				getMaxLevel(Skill.MAGIC));
	}

	/** Gets the experience counter value. */
	public int getExpCounter() {
		return (int) Math.floor(experienceCounter);
	}

	/** Gets the mob's combat level. */
	public double getCombatLevel() {
		return combatLevel;
	}

	/** Sets the mob's combat level. */
	public void setCombatLevel() {
		this.combatLevel = calculateCombat();
	}

	/** Gets the skills of the mob. */
	public Skill[] getSkills() {
		return skills;
	}

	/** Sets the skills of the mob. */
	public void setSkills(Skill[] skills) {
		this.skills = skills;
		if (mob.isPlayer()) {
			this.skills[Skill.HUNTER] = new Hunter(skills[Skill.HUNTER].getLevel(),
					skills[Skill.HUNTER].getExperience());
			this.skills[Skill.PRAYER] = new BoneSacrifice(skills[Skill.PRAYER].getLevel(),
					skills[Skill.PRAYER].getExperience());
			this.skills[Skill.COOKING] = new Cooking(skills[Skill.COOKING].getLevel(),
					skills[Skill.COOKING].getExperience());
			this.skills[Skill.CRAFTING] = new Crafting(skills[Skill.CRAFTING].getLevel(),
					skills[Skill.CRAFTING].getExperience());
			this.skills[Skill.THIEVING] = new Thieving(skills[Skill.THIEVING].getLevel(),
					skills[Skill.THIEVING].getExperience());
			this.skills[Skill.HERBLORE] = new Herblore(skills[Skill.HERBLORE].getLevel(),
					skills[Skill.HERBLORE].getExperience());
			this.skills[Skill.FLETCHING] = new Fletching(skills[Skill.FLETCHING].getLevel(),
					skills[Skill.FLETCHING].getExperience());
			this.skills[Skill.FIREMAKING] = new Firemaking(skills[Skill.FIREMAKING].getLevel(),
					skills[Skill.FIREMAKING].getExperience());
			this.skills[Skill.RUNECRAFTING] = new Runecraft(skills[Skill.RUNECRAFTING].getLevel(),
					skills[Skill.RUNECRAFTING].getExperience());
			this.skills[Skill.MINING] = new Mining(skills[Skill.MINING].getLevel(),
					skills[Skill.MINING].getExperience());
			this.skills[Skill.SMITHING] = new Smithing(skills[Skill.SMITHING].getLevel(),
					skills[Skill.SMITHING].getExperience());
			this.skills[Skill.WOODCUTTING] = new Woodcutting(skills[Skill.WOODCUTTING].getLevel(),
					skills[Skill.WOODCUTTING].getExperience());
			this.skills[Skill.FISHING] = new Fishing(skills[Skill.FISHING].getLevel(),
					skills[Skill.FISHING].getExperience());
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
