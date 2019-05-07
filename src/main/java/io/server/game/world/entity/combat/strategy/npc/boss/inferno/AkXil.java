package io.server.game.world.entity.combat.strategy.npc.boss.inferno;

import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

public class AkXil extends MultiStrategy {

    public AkXil() {
        currentStrategy = new Ranged();
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static class Ranged extends NpcRangedStrategy {
        private Ranged() {
            super(getDefinition("jalak xil"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[] { nextRangedHit(attacker, defender, 18) };
        }
    }

}

