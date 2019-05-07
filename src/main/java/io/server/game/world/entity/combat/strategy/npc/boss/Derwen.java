package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.task.impl.ForceMovementTask;
import io.server.game.world.World;
import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.ForceMovement;
import io.server.game.world.pathfinding.path.SimplePathChecker;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Handles Derwen Combat Strategy
 *
 * @author Adam_#6723
 */
public class Derwen extends MultiStrategy {
	private static Magic MAGIC = new Magic();
	private static Melee MELEE = new Melee();
	private static LightingRain LIGHTNING_RAIN = new LightingRain();
	private static TeleGrab TELE_GRAB = new TeleGrab();

	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC,
			TELE_GRAB, LIGHTNING_RAIN);
	private static final CombatStrategy<Npc>[] MAGIC_STRATEGIES = createStrategyArray(MAGIC, MAGIC, MAGIC, TELE_GRAB,
			LIGHTNING_RAIN);
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(MAGIC, MELEE, MELEE, MAGIC, MAGIC,
			TELE_GRAB, LIGHTNING_RAIN);

	/** Constructs a new <code>Derwen</code>. */
	public Derwen() {
		currentStrategy = MAGIC;
		currentStrategy = MELEE;
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(MAGIC_STRATEGIES);
			currentStrategy = randomStrategy(NON_MELEE);
		}
		return currentStrategy.canAttack(attacker, defender);
	}


	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {
		currentStrategy.finishOutgoing(attacker, defender);
		if (NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		} else {
			currentStrategy = randomStrategy(MAGIC_STRATEGIES);
		}
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	private static class Melee extends NpcRangedStrategy {

		public Melee() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {

			attacker.animate(new Animation(7848, UpdatePriority.VERY_HIGH));
			//CombatHit hit = nextMeleeHit(attacker, defender, 21);
			defender.graphic(1176);
			CombatUtil.areaAction(attacker, 64, 18, mob -> {
				mob.damage(nextMagicHit(attacker, defender, 38));
			});

		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextRangedHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}

		@Override
		public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
			return roll + 50_000;
		}

	}

	/** Jisticiar magic strategy. */
	private static class Magic extends NpcMagicStrategy {
		public Magic() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			attacker.animate(new Animation(7849, UpdatePriority.VERY_HIGH));
			Projectile projectile = new Projectile(1379, 50, 80, 85, 25);
			CombatUtil.areaAction(attacker, 64, 18, mob -> {
				projectile.send(attacker, defender);
				defender.graphic(157);
				mob.damage(nextMagicHit(attacker, defender, 35));

			});

			if (Utility.random(0, 25) == 1) {
				attacker.animate(new Animation(7849, UpdatePriority.VERY_HIGH));
				attacker.graphic(new Graphic(1296, UpdatePriority.VERY_HIGH));
				attacker.heal(130);
				attacker.speak("Time To HEAL!");
				defender.getPlayer().send(new SendMessage("Derwen heals himself!"));

			}

		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}

		@Override
		public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
			return roll + 50_000;
		}
	}

	private static class TeleGrab extends NpcMagicStrategy {
		TeleGrab() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}
	}

	private static class LightingRain extends NpcMagicStrategy {
		LightingRain() {
			super(CombatProjectile.getDefinition("Vorkath Frozen Special"));
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			attacker.animate(new Animation(7849, UpdatePriority.VERY_HIGH));
			World.sendProjectile(attacker, defender, new Projectile(1382, 46, 80, 43, 31));
			World.schedule(1, () -> {
				if (defender.isPlayer()) {
					Position current = defender.getPosition();
					Position best = Utility.findBestInside(defender, attacker);
					int dx = current.getX() - best.getX();
					int dy = current.getY() - best.getY();

					Direction opposite = Direction.getFollowDirection(attacker.getPosition(), defender.getPosition());
//                    for (int x = 1; x <= 2; x++) {
					int y = dy / (dx == 0 ? dy : dx);
					Position destination = current.transform(dx, y);
					if (SimplePathChecker.checkLine(defender, destination))
						current = destination;
//                    }
					defender.damage(new Hit(Utility.random(1, 3)));
					defender.interact(attacker);
					defender.getPlayer().send(new SendMessage("Derwen throws you backwards."));

					Position offset = new Position(current.getX() - defender.getX(), current.getY() - defender.getY());
					ForceMovement movement = new ForceMovement(defender.getPosition(), offset, 33, 60,
							Direction.getOppositeDirection(opposite));

					int anim = defender.mobAnimation.getWalk();
					World.schedule(new ForceMovementTask(defender, 3, 0, movement,
							new Animation(3170, UpdatePriority.VERY_HIGH)) {
						@Override
						protected void onSchedule() {
							super.onSchedule();
							defender.mobAnimation.setWalk(3170);
							defender.locking.lock();
						}

						@Override
						protected void onCancel(boolean logout) {
							super.onCancel(logout);
							defender.mobAnimation.setWalk(anim);
							defender.locking.unlock();
						}
					});
				}
			});
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}
	}
}
