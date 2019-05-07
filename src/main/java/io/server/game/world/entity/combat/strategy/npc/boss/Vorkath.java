package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.task.TickableTask;
import io.server.game.task.impl.CeillingCollapseTask;
import io.server.game.world.World;
import io.server.game.world.entity.combat.CombatType;
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
import io.server.game.world.entity.combat.strategy.npc.impl.DragonfireStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * The combat definition for Vorkath.
 *
 * @author Adam_#6723
 */
public class Vorkath extends MultiStrategy {
	private static final NpcMeleeStrategy MELEE = NpcMeleeStrategy.get();
	private static final MagicAttack MAGIC = new MagicAttack();
	private static final RangedAttack RANGED = new RangedAttack();
	private static final FireballAttack FIREBALL = new FireballAttack();

	private static final DragonfireStrategy DRAGONFIRE = new DragonfireAttack();
	private static final DragonfireStrategy PINK_DRAGONFIRE = new PinkDragonfireAttack();
	private static final DragonfireStrategy VENOM_DRAGONFIRE = new VenomDragonfireAttack();

	private static final VenomSpecial VENOM_SPECIAL = new VenomSpecial();
	private static final FrozenSpecial FROZEN_SPECIAL = new FrozenSpecial();

	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(MELEE, DRAGONFIRE, MAGIC, FIREBALL,
			RANGED, VENOM_DRAGONFIRE, PINK_DRAGONFIRE);
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(FIREBALL, DRAGONFIRE, MAGIC, RANGED,
			VENOM_DRAGONFIRE, PINK_DRAGONFIRE);
	private final CombatStrategy<Npc>[] SPECIALS = createStrategyArray(VENOM_SPECIAL, FROZEN_SPECIAL);

	private final Deque<CombatStrategy<Npc>> strategyQueue = new LinkedList<>();
	private int specialIndex;

	public Vorkath() {
		currentStrategy = MELEE;
	}

	@Override
	public void init(Npc attacker, Mob defender) {
		if (strategyQueue.isEmpty()) {
			for (int index = 0; index < 6; index++) {
				strategyQueue.add(RandomUtils.random(FULL_STRATEGIES));
			}
			strategyQueue.add(SPECIALS[specialIndex++ % SPECIALS.length]);
		}
		currentStrategy = strategyQueue.poll();
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (currentStrategy == MELEE && !MELEE.canAttack(attacker, defender)) {
			currentStrategy = RandomUtils.random(NON_MELEE);
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	@Override
	public boolean withinDistance(Npc attacker, Mob defender) {
		if (currentStrategy == MELEE && !MELEE.withinDistance(attacker, defender)) {
			currentStrategy = RandomUtils.random(NON_MELEE);
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	private static class VenomSpecial extends NpcMagicStrategy {
		VenomSpecial() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
			return 30;
		}
//TODO FIX THIS METHOD MAKE IT CLEANER
		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			World.schedule(2, () -> {
				attacker.animate(new Animation(7957, UpdatePriority.HIGH));
				List<Position> boundaries = TraversalMap.getTraversableTiles(attacker.getPosition().transform(-7, -7),
						30, 30);
				Collections.shuffle(boundaries);

				World.schedule(1, () -> {

					Projectile projectile = new Projectile(1483, 10, 85, 26, 25);
					projectile.send(attacker, defender.getPosition());
					World.schedule(AcidTask(defender, defender.getPosition()));

					for (int index = 0; index < 40; index++) {
						if (attacker == null || attacker.isDead() || !Area.inVorkath(attacker)) {
							return;
						}
						Position position = boundaries.get(index);
						projectile.send(attacker, position);
						World.schedule(AcidTask(defender, position));
					}

					final Projectile projectile2 = new Projectile(1482, 15, 95, 26, 25);

					World.schedule(2, () -> World.schedule(DragonFire(attacker, defender, projectile2)));
					if (attacker == null || attacker.isDead() || !Area.inVorkath(attacker)) {
						return;
					}
				});
			});
		}

		private TickableTask DragonFire(Mob attacker, Mob defender, Projectile projectile) {
			return new TickableTask(false, 1) {

				@Override
				protected void tick() {
					if (attacker == null || attacker.isDead() || !Area.inVorkath(attacker)) {
						cancel();
						return;
					}

					if (tick > 24) {
						cancel();
						return;
					}

					Position position = defender.getPosition().copy();
					projectile.send(attacker, position);

					World.schedule(3, () -> {
						World.sendGraphic(new Graphic(131, UpdatePriority.HIGH), position, attacker.instance);

						if (defender.getPosition().equals(position)) {
							defender.writeDamage(new Hit(Utility.random(30)));
						}
					});
				}
			};
		}

		private TickableTask AcidTask(Mob defender, Position position) {
			return new TickableTask(false, 2) {
				private CustomGameObject object;

				@Override
				protected void tick() {
					if (defender == null || defender.isDead() || !Area.inVorkath(defender)) {
						return;
					}
					if (tick == 1) {
						object = new CustomGameObject(32000, defender.instance, position);
						object.register();
					} else if (tick == 13) {
						object.unregister();
						cancel();
						return;
					}

					if (defender.getPosition().equals(position) && Area.inVorkath(defender)) {
						defender.writeDamage(new Hit(Utility.random(1, 3)));
					}
				}
			};
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit combatHit = nextMagicHit(attacker, defender, 0);
			combatHit.setAccurate(true);
			combatHit.setDamage(-1);
			return new CombatHit[] { combatHit };
		}
	}

	private static class RangedAttack extends NpcRangedStrategy {

		RangedAttack() {
			super(CombatProjectile.getDefinition("Vorkath Ranged"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(7952, UpdatePriority.HIGH);
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 23) };
		}
	}

	private static class MagicAttack extends NpcMagicStrategy {

		MagicAttack() {
			super(CombatProjectile.getDefinition("Vorkath Magic"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(7952, UpdatePriority.HIGH);
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 23) };
		}
	}

	private static class FrozenSpecial extends NpcMagicStrategy {
		private final Projectile PROJECTILE = new Projectile(1470, 5, 85, 26, 25);

		FrozenSpecial() {
			super(CombatProjectile.getDefinition("Vorkath Frozen Special"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(7952, UpdatePriority.HIGH);
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
			defender.graphic(new Graphic(369));
			// defender.locking.lock(LockType.FREEZE);

			// attacker.blockInteract = true;
			attacker.resetFace();
			attacker.face(new Position(2277, 4057));
			attacker.animate(new Animation(7960, UpdatePriority.HIGH));
			PROJECTILE.send(attacker, new Position(2277, 4057));

			World.schedule(4, () -> {
				// attacker.blockInteract = false;
				Npc zombie = new Npc(8063, new Position(2277, 4057), defender.instance) {
					@Override
					public void appendDeath() {
						super.appendDeath();
						// defender.locking.unlock();
						// defender.getPlayer().send(new SendWidget(SendWidget.WidgetType.FROZEN, 0));
					}
				};
				zombie.instance = defender.instance;
				zombie.register();
				zombie.walkTo(defender, () -> {
					World.sendGraphic(new Graphic(1460, true), zombie.getPosition(), defender.instance);
					defender.damage(new Hit(30 * zombie.getCurrentHealth() / zombie.getMaximumHealth()));
					// defender.locking.unlock();
					// defender.getPlayer().send(new SendWidget(SendWidget.WidgetType.FROZEN, 0));
					World.schedule(new CeillingCollapseTask(defender.getPlayer()));
					for (int i = 0; i < 5; i++) {
						defender.graphic(60);
						defender.speak("Ouch!");
						defender.damage(new Hit(Utility.random(2, 4)));
						defender.getPlayer().message("Some rocks fall from the ceiling and hit you.");
					}
					zombie.unregister();
				});
			});
		}

		@Override
		public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
			return 15;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit combatHit = nextMagicHit(attacker, defender, -1);
			combatHit.setAccurate(false);
			return new CombatHit[] { combatHit };
		}
	}

	private static class DragonfireAttack extends DragonfireStrategy {
		DragonfireAttack() {
			super(CombatProjectile.getDefinition("Metalic dragonfire"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(7952, UpdatePriority.HIGH);
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 40, true) };
		}
	}

	private static class VenomDragonfireAttack extends DragonfireStrategy {
		VenomDragonfireAttack() {
			super(CombatProjectile.getDefinition("Vorkath Venom Dragonfire"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(7952, UpdatePriority.HIGH);
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
			super.hit(attacker, defender, hit);
			if (hit.isAccurate()) {
				defender.venom();
			}
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit combatHit = CombatUtil.generateDragonfire(attacker, defender, 35, true);
			// combatHit.setHitsplat(Hitsplat.VENOM);
			return new CombatHit[] { combatHit };
		}
	}

	private static class PinkDragonfireAttack extends DragonfireStrategy {
		PinkDragonfireAttack() {
			super(CombatProjectile.getDefinition("Vorkath Pink Dragonfire"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(7952, UpdatePriority.HIGH);
		}

		@Override
		public void hitsplat(Npc attacker, Mob defender, Hit hit) {
			super.hitsplat(attacker, defender, hit);

			if (defender.isPlayer()) {
				defender.prayer.reset();
			}
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 40, true) };
		}
	}

	private static class FireballAttack extends DragonfireStrategy {
		private final Projectile PROJECTILE = new Projectile(1481, 5, 105, 26, 25);
		private final Graphic GRAPHIC = new Graphic(1466, true);
		private Position position;

		FireballAttack() {
			super(CombatProjectile.getDefinition("EMPTY"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(7960, UpdatePriority.HIGH);
		}

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			super.start(attacker, defender, hits);
			position = defender.getPosition();

		}

		@Override
		public void attack(Npc attacker, Mob defender, Hit hit) {
			PROJECTILE.send(attacker, position);

		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
			super.hitsplat(attacker, defender, hit);
			World.sendGraphic(GRAPHIC, position, defender.instance);
			hit.setAccurate(false);

			if (defender.getPosition().equals(position)) {
				hit.setAs(CombatUtil.generateDragonfire(attacker, defender, 60, true));
				hit.setAccurate(true);
			} else if (Utility.withinOctal(defender.getPosition(), defender.width(), defender.length(), position, 1, 1,
					1)) {
				hit.setAs(CombatUtil.generateDragonfire(attacker, defender, 50, true));
				hit.modifyDamage(damage -> damage / 2);
				hit.setAccurate(true);
			}
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, -1,
					CombatUtil.getHitDelay(attacker, defender, CombatType.MAGIC) + 1, 1) };
		}
	}

}
