package io.server.game.world.entity.combat.strategy.player;

import io.server.content.activity.Activity;
import io.server.content.activity.impl.duelarena.DuelArenaActivity;
import io.server.content.activity.impl.duelarena.DuelRule;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.effect.impl.CombatPoisonEffect;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.basic.MeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;

public class PlayerMeleeStrategy extends MeleeStrategy<Player> {
	
	private static final PlayerMeleeStrategy INSTANCE = new PlayerMeleeStrategy();

	protected PlayerMeleeStrategy() {
		
	}

	@Override
	public boolean canAttack(Player attacker, Mob defender) {
		if (Activity.search(attacker, DuelArenaActivity.class).isPresent()) {
			DuelArenaActivity activity = Activity.search(attacker, DuelArenaActivity.class).get();

			if (activity.getRules().contains(DuelRule.NO_MELEE)) {
				return false;
			}

		}
		return true;
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		if (attacker.isSpecialActivated()) {
			attacker.getCombatSpecial().drain(attacker);
		}

		attacker.animate(getAttackAnimation(attacker, defender));

		if (!defender.isPlayer() || !PlayerRight.isIronman(attacker))
			addCombatExperience(attacker, hits);
	}

	@Override
	public void hit(Player attacker, Mob defender, Hit hit) {
		if (hit.getDamage() < 1) {
			return;
		}
		CombatPoisonEffect.getPoisonType(attacker.equipment.getWeapon()).ifPresent(defender::poison);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[] { nextMeleeHit(attacker, defender) };
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return attacker.getCombat().getFightType().getDelay();
	}

	@Override
	public int getAttackDistance(Player attacker, FightType fightType) {
		return fightType.getDistance();
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		int animation = attacker.getCombat().getFightType().getAnimation();

		if (attacker.equipment.hasShield()) {
			Item weapon = attacker.equipment.getShield();
			FightType fightType = attacker.getCombat().getFightType();
			animation = weapon.getAttackAnimation(fightType).orElse(animation);
		}

		if (attacker.equipment.hasWeapon()) {
			Item weapon = attacker.equipment.getWeapon();
			FightType fightType = attacker.getCombat().getFightType();
			animation = weapon.getAttackAnimation(fightType).orElse(animation);
		}

		return new Animation(animation, UpdatePriority.HIGH);
	}

	/*
	 * @Override public int modifyAccuracy(Player attacker, Mob defender, int roll)
	 * { if (CombatUtil.isFullVoid(attacker) && attacker.equipment.contains(11665))
	 * { roll *= 1.10; } return roll; }
	 * 
	 * @Override public int modifyAggressive(Player attacker, Mob defender, int
	 * roll) { if (CombatUtil.isFullVoid(attacker) &&
	 * attacker.equipment.contains(11665)) { roll *= 1.10; } return roll; }
	 */

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	public static PlayerMeleeStrategy get() {
		return INSTANCE;
	}

}
