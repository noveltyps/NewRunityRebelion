package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;

import io.server.Config;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;

/** @author Daniel */
public class CorporealBeast extends MultiStrategy {
	private static final MagicAttack MAGIC = new MagicAttack();
	private static final CrushMelee MELEE = new CrushMelee();
	private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(MAGIC, MELEE);

	public CorporealBeast() {
		currentStrategy = randomStrategy(STRATEGIES);
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		return currentStrategy.canAttack(attacker, defender);
	}
	
	@Override
	public boolean withinDistance(Npc attacker, Mob defender) {
		return currentStrategy.canAttack(attacker, defender);
	}

	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {
		currentStrategy.finishOutgoing(attacker, defender);
		currentStrategy = randomStrategy(STRATEGIES);
	}

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		if (attacker.getPlayer().equipment.containsAny(Config.SPEARS)) {
			hit.modifyDamage(damage -> hit.getDamage() * 2);
			// attacker.getPlayer().message("Your attack is " + hit.getDamage());
		}
//        if (hit.getDamage() > 32) {
		// 1/8 chance that the dark core will spawn
//        }
		defender.getCombat().attack(attacker);
		super.block(attacker, defender, hit, combatType);
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	@Override
	public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
		return (int) (roll * 5.05);
	}

	private static final class CrushMelee extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(1682, UpdatePriority.HIGH);

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
			return new CombatHit[] { nextMeleeHit(attacker, defender) };
		}
	}
//no it reloads it on unban or on any punishment command 
	private static class MagicAttack extends NpcMagicStrategy {
		private static final Animation ANIMATION = new Animation(1679, UpdatePriority.HIGH);

		private MagicAttack() {
			super(CombatProjectile.getDefinition("Corporeal Beast Magic"));
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			super.start(attacker, defender, hits);
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
			super.hit(attacker, defender, hit);
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, 65) };
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}
	}
}
