package io.server.game.world.entity.combat.strategy.npc.boss.inferno;

import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

public class JaxXil extends MultiStrategy {

    private static final Melee MELEE = new Melee();
    private static final Ranged RANGED = new Ranged();

    //private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(RANGED, MELEE);

    public JaxXil() {
        currentStrategy = RANGED;
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            currentStrategy = MELEE;
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    private static class Melee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(7604, UpdatePriority.HIGH);

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 2;
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }

    private static class Ranged extends NpcRangedStrategy {

        private static final Animation ANIMATION = new Animation(7605 , UpdatePriority.HIGH);

        private Ranged() {
            super(getDefinition("xil"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
           return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMagicHit(attacker, defender, 46)};
        }
    }
}