package io.server.game.world.entity.combat.strategy.npc.boss.inferno;

import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

public class AkMej extends MultiStrategy {

    public AkMej() {
        currentStrategy = new Mage();
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static class Mage extends NpcMagicStrategy {

        private Mage() { super(getDefinition("jalak mej"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[] { nextRangedHit(attacker, defender, 18) };
        }
    }

}

