package io.server.game.world.entity.combat.strategy.npc;

import java.util.function.Consumer;
import java.util.function.Predicate;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatImpact;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.attack.FormulaFactory;
import io.server.game.world.entity.combat.effect.impl.CombatPoisonEffect;
import io.server.game.world.entity.combat.effect.impl.CombatVenomEffect;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.basic.RangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.util.RandomUtils;

public class NpcRangedStrategy extends RangedStrategy<Npc> {

	private final CombatProjectile projectileDefinition;

	public NpcRangedStrategy(CombatProjectile projectileDefinition) {
		this.projectileDefinition = projectileDefinition;
	}

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		Animation animation = getAttackAnimation(attacker, defender);

		if (projectileDefinition.getAnimation().isPresent()
				&& (animation.getId() == -1 || animation.getId() == 65535)) {
			animation = projectileDefinition.getAnimation().get();
		}

		attacker.animate(animation);
		projectileDefinition.getStart().ifPresent(attacker::graphic);
		projectileDefinition.sendProjectile(attacker, defender);
	}

	@Override
    public void attack(Npc attacker, Mob defender, Hit hit) {
        if(defender != null && !defender.isNpc() && defender.getPlayer() != null)
            if(defender.getPlayer().prayer.isActive(Prayer.PROTECT_FROM_RANGE))
                hit.setDamage(attacker.isNpc() ? 0:hit.getDamage()/2);
        Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
        Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, null);
        projectileDefinition.getEffect().filter(filter).ifPresent(execute);

        if (!attacker.definition.isPoisonous()) {
            return;
        }

        if (CombatVenomEffect.isVenomous(attacker) && RandomUtils.success(0.25)) {
            defender.venom();
        } else {
            CombatPoisonEffect.getPoisonType(attacker.id).ifPresent(defender::poison);
        }
    }

	@Override
	public void hit(Npc attacker, Mob defender, Hit hit) {
		projectileDefinition.getEnd().ifPresent(defender::graphic);
	}

	@Override
	public CombatHit[] getHits(Npc attacker, Mob defender) {
		int max = projectileDefinition.getMaxHit();
		if (max == -1)
			max = FormulaFactory.getMaxHit(attacker, defender, getCombatType());
		return new CombatHit[] { nextRangedHit(attacker, defender, max, projectileDefinition) };
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	@Override
	public int getAttackDistance(Npc attacker, FightType fightType) {
		return 10;
	}

	@Override
	public Animation getAttackAnimation(Npc attacker, Mob defender) {
		return new Animation(attacker.definition.getAttackAnimation(), UpdatePriority.HIGH);
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}

}
