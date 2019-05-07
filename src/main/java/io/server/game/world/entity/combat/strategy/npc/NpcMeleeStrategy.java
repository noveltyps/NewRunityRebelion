package io.server.game.world.entity.combat.strategy.npc;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.effect.impl.CombatPoisonEffect;
import io.server.game.world.entity.combat.effect.impl.CombatVenomEffect;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.basic.MeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.util.RandomUtils;

public class NpcMeleeStrategy extends MeleeStrategy<Npc> {

	private static final NpcMeleeStrategy INSTANCE = new NpcMeleeStrategy();

	protected NpcMeleeStrategy() {
	}

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		attacker.animate(getAttackAnimation(attacker, defender));
	}

	@Override
    public void attack(Npc attacker, Mob defender, Hit hit) {
		if((defender != null && !defender.isNpc() && defender.getPlayer() != null) && defender.getPlayer().prayer.isActive(Prayer.PROTECT_FROM_MELEE))
            hit.setDamage(attacker.isNpc() ? 0 : hit.getDamage() / 2);
		
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
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	@Override
	public int getAttackDistance(Npc attacker, FightType fightType) {
		return 1;
	}

	@Override
	public CombatHit[] getHits(Npc attacker, Mob defender) {
		return new CombatHit[] { nextMeleeHit(attacker, defender) };
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
		return CombatType.MELEE;
	}

	public static NpcMeleeStrategy get() {
		return INSTANCE;
	}

}
