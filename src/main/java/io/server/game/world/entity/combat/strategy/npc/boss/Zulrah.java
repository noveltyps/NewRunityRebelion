package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.World;
import io.server.game.world.entity.combat.FormulaModifier;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.data.LockType;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.game.world.pathfinding.path.SimplePathChecker;
import io.server.game.world.position.Position;
import io.server.util.Utility;

/** @author Daniel */
public class Zulrah extends MultiStrategy {
	
	private static final MagicAttack MAGIC = new MagicAttack();
	private static final RangedAttack RANGED = new RangedAttack();
	private static final MeleeAttack MELEE = new MeleeAttack();

	private static final FormulaModifier<Npc> MODIFIER = new FormulaModifier<Npc>() {
		@Override
		public int modifyAttackLevel(Npc attacker, Mob defender, int level) {
			return 1;
		}
	};

	public Zulrah() {
		setRanged();
	}

	public void setMelee() {
		currentStrategy = MELEE;
	}

	public void setMagic() {
		currentStrategy = MAGIC;
	}

	public void setRanged() {
		currentStrategy = RANGED;
	}

	private static class MeleeAttack extends NpcMagicStrategy {

		MeleeAttack() {
			super(getDefinition("EMPTY"));
		}

		@Override
		public boolean withinDistance(Npc attacker, Mob defender) {
			return Utility.within(attacker, defender, getAttackDistance(attacker, attacker.getCombat().getFightType()))
					&& SimplePathChecker.checkProjectile(attacker, defender);
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			Position end = defender.getPosition();
			attacker.face(end);
			attacker.animate(new Animation(5807, UpdatePriority.HIGH));
			World.schedule(6, () -> {
				attacker.face(end);
				attacker.animate(new Animation(5806, UpdatePriority.HIGH));
				if (defender.getPosition().equals(end)) {
					attacker.getCombat().addModifier(MODIFIER);
					Hit hit = nextMeleeHit(attacker, defender, 41);
					attacker.getCombat().removeModifier(MODIFIER);
					defender.damage(hit);
					defender.getCombat().getDamageCache().add(attacker, hit);
					defender.locking.lock(2, LockType.STUN);
				}
			});
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
			/* No super call because of splash gfx */
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 0);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}

		@Override
		public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
			return 8;
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 3;
		}

		@Override
		public int modifyOffensiveBonus(Npc attacker, Mob defender, int bonus) {
			FightType fightType = attacker.getCombat().getFightType();
			bonus = attacker.getBonus(fightType.getBonus());
			return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
		}

		@Override
		public int modifyAggressiveBonus(Npc attacker, Mob defender, int bonus) {
			bonus = attacker.getBonus(Equipment.STRENGTH_BONUS);
			return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
		}

		@Override
		public int modifyDefensiveBonus(Mob attacker, Npc defender, int bonus) {
			FightType fightType = attacker.getCombat().getFightType();
			bonus = defender.getBonus(fightType.getCorrespondingBonus());
			return defender.getCombat().modifyDefensiveBonus(attacker, bonus);
		}

	}

	private static class RangedAttack extends NpcRangedStrategy {
		private RangedAttack() {
			super(getDefinition("Zulrah Ranged"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 41) };
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}

	}

	private static class MagicAttack extends NpcMagicStrategy {
		private MagicAttack() {
			super(getDefinition("Zulrah Magic"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit combatHit = nextMagicHit(attacker, defender, 41);
			combatHit.setAccurate(true);
			return new CombatHit[] { combatHit };
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}

	}

}
