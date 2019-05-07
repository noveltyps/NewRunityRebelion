package io.server.game.world.entity.combat.strategy.player.custom;

import static io.server.game.world.entity.combat.CombatUtil.getHitDelay;

import io.server.Config;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.basic.MagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;

public class DragonfireShieldStrategy extends MagicStrategy<Player> {
	private static final DragonfireShieldStrategy INSTANCE = new DragonfireShieldStrategy();
	private static CombatProjectile PROJECTILE;

	static {
		try {
			PROJECTILE = CombatProjectile.getDefinition("Dragonfire Shield");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		int hitDelay = getHitDelay(attacker, defender, CombatType.MAGIC) + 2;
		int hitsplatDelay = 1;
		return new CombatHit[] {
				CombatUtil.generateDragonfire(attacker, defender, 25, hitDelay, hitsplatDelay, false) };
	}

	@Override
	public boolean canAttack(Player attacker, Mob defender) {
		if (attacker.dragonfireCharges == 0) {
			attacker.message("You have no charges to do this!");
			return false;
		}
		return true;
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		PROJECTILE.getAnimation().ifPresent(attacker::animate);
		PROJECTILE.getStart().ifPresent(attacker::graphic);

		int damage = 0;
		for (Hit hit : hits) {
			damage += hit.getDamage();
		}

		if (damage > 0) {
			damage *= Config.COMBAT_MODIFICATION;
			attacker.skills.addExperience(Skill.DEFENCE, damage / 2);
			if (defender.isNpc()) {
				attacker.skills.addExperience(Skill.HITPOINTS, damage / 3);
				attacker.skills.addExperience(Skill.MAGIC, damage / 2);
			}
		}
		attacker.interact(defender);
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		PROJECTILE.sendProjectile(attacker, defender);
		attacker.dragonfireCharges--;
	}

	@Override
	public void hit(Player attacker, Mob defender, Hit hit) {
		PROJECTILE.getEnd().ifPresent(defender::graphic);
		attacker.resetFace();
	}

	@Override
	public void hitsplat(Player attacker, Mob defender, Hit hit) {
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return new Animation(6696, UpdatePriority.HIGH);
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 5;
	}

	@Override
	public int getAttackDistance(Player attacker, FightType fightType) {
		return 10;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

	public static DragonfireShieldStrategy get() {
		return INSTANCE;
	}

}
