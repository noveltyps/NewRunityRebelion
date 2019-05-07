package io.server.game.world.entity.combat.strategy.basic;

import io.server.content.experiencerate.ExperienceModifier;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.HitIcon;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.pathfinding.path.SimplePathChecker;
import io.server.util.Utility;

/**
 * @author Michael | Chex <-- they did a pooop job with it.
 * @edited by Adam_#6723 && Teek
 */
public abstract class MeleeStrategy<T extends Mob> extends CombatStrategy<T> {

	@Override
	public boolean withinDistance(T attacker, Mob defender) {
		FightType fightType = attacker.getCombat().getFightType();
		int distance = getAttackDistance(attacker, fightType);

		if (attacker.movement.needsPlacement() && defender.movement.needsPlacement() && !attacker.locking.locked()) {
			distance++;
			if (defender.movement.isRunning())
				distance++;
		}

		if (defender.id == 1739 || defender.id == 1740 || defender.id == 1741 || defender.id == 1742) {
			return Utility.withinDistance(attacker, defender, distance);
		}
		return Utility.withinDistance(attacker, defender, distance) && SimplePathChecker.checkLine(attacker, defender);
	}

	@Override
	public int modifyDamage(T attacker, Mob defender, int damage) {
		if (defender.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
			return damage *= defender.isNpc() ? 0.1 : 0.6;

		}
		return damage;
	}

	protected static void addCombatExperience(Player player, Hit... hits) {
		int exp = 0;
		for (Hit hit : hits) {
			if (hit.getHitIcon() == HitIcon.MELEE) {
				exp += hit.getDamage();
			} else if (hit.getHitIcon() == HitIcon.MAGIC) {
				MagicStrategy.addCombatExperience(player, 0, hit);
			}
		}

		exp *= player.experienceRate * new ExperienceModifier(player).getModifier();
		player.skills.addExperience(Skill.HITPOINTS, exp / 3);
		switch (player.getCombat().getFightType().getStyle()) {
		case ACCURATE:
			player.skills.addExperience(Skill.ATTACK, exp);
			break;
		case AGGRESSIVE:
			player.skills.addExperience(Skill.STRENGTH, exp);
			break;
		case DEFENSIVE:
			player.skills.addExperience(Skill.DEFENCE, exp);
			break;
		case CONTROLLED:
			exp /= 3;
			player.skills.addExperience(Skill.ATTACK, exp);
			player.skills.addExperience(Skill.STRENGTH, exp);
			player.skills.addExperience(Skill.DEFENCE, exp);
			break;
		}
	}

}