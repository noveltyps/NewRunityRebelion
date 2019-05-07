package io.server.game.world.entity.combat.strategy.npc.boss;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import java.util.HashSet;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.MultiStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.position.Position;
import io.server.util.RandomGen;

/**
 * 
 * @author Adam_#6723
 *
 */

public class Cerberus extends MultiStrategy {

	private static final RangedAttack RANGED = new RangedAttack();
	private static final MagicAttack MAGIC = new MagicAttack();
	private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(RANGED, MAGIC, NpcMeleeStrategy.get());

	public final HashSet<Position> lavaPoolPositions = new HashSet<>();

	public Cerberus() {
		currentStrategy = MAGIC;
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(STRATEGIES);
		}
		if (attacker.isDead()) {
			return false;
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		currentStrategy.block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);
	}

	@Override
	public void hit(Npc attacker, Mob defender, Hit hit) {
		super.hit(attacker, defender, hit);

		if (!defender.isPlayer())
			return;
		Player player = defender.getPlayer();

		if (currentStrategy.getCombatType().equals(CombatType.MELEE)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
			hit.setDamage(0);
		} else if (currentStrategy.getCombatType().equals(CombatType.RANGED)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_RANGE)) {
			hit.setDamage(0);
		} else if (currentStrategy.getCombatType().equals(CombatType.MAGIC)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
			hit.setDamage(0);
		}

		if (attacker.getCurrentHealth() < 200 && new RandomGen().inclusive(10) == 0) {
			executeSpecial(attacker, defender.getPlayer(), CerberusSpecial.LAVA);
		}
		/*
		 * if (new RandomGen().inclusive(10) == 0) { boolean ghostsPresent =
		 * !((CerberusActivity) attacker.activity).ghosts.isEmpty(); if (!ghostsPresent)
		 * { executeSpecial(attacker, defender.getPlayer(), CerberusSpecial.GHOSTS); } }
		 */

	}

	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {
		currentStrategy.finishOutgoing(attacker, defender);
		if (NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(STRATEGIES);
		} else {
			currentStrategy = randomStrategy(STRATEGIES);
		}
	}

	@Override
	public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
		return roll;
	}

	private static class RangedAttack extends NpcRangedStrategy {
		private static final Animation ANIMATION = new Animation(4490, UpdatePriority.HIGH);

		RangedAttack() {
			super(CombatProjectile.getDefinition("Cerb Range"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 23, 4, 0) };
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}

	private static class MagicAttack extends NpcMagicStrategy {
		private static final Animation ANIMATION = new Animation(4490, UpdatePriority.HIGH);

		public MagicAttack() {
			super(getDefinition("Cerb Magic"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 23, 4, 0);
			return new CombatHit[] { hit };
		}

		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}

	private void executeSpecial(Npc cerberus, Player target, CerberusSpecial special) {
		cerberus.speak(special.forceChat);
		if (special == CerberusSpecial.LAVA) {
			cerberus.animate(new Animation(4494, UpdatePriority.VERY_HIGH));
			for (int i = 0; i < 3; i++) {
				int offsetX = target.getX() - cerberus.getX();
				int offsetY = target.getY() - cerberus.getY();
				if (i == 0 || i == 2) {
					offsetX += i == 0 ? -1 : 1;
					offsetY++;
				}
				Position pool = new Position(cerberus.getX() + offsetX, cerberus.getY() + offsetY,
						target.playerAssistant.instance());
				lavaPoolPositions.add(pool);
				World.sendGraphic(new Graphic(1246, UpdatePriority.HIGH), pool);
				World.schedule(new Task(9) {
					protected void execute() {
						lavaPoolPositions.clear();
					}
/*
					protected void onLoop() {
						if (target.getPosition().equals(pool)) {
							target.damage(new Hit(15));
						}
						if (lavaPoolPositions.isEmpty()) {
							this.cancel();
						}
					}*/
				});
			}
		} else {
			Npc meleeGhost = new Npc(5869, new Position(1239, 1256, target.playerAssistant.instance()));
			Npc mageGhost = new Npc(5868, new Position(1240, 1256, target.playerAssistant.instance()));
			Npc rangeGhost = new Npc(5867, new Position(1241, 1256, target.playerAssistant.instance()));
			meleeGhost.register();
			mageGhost.register();
			rangeGhost.register();
			target.activity.add(meleeGhost);
			target.activity.add(mageGhost);
			target.activity.add(rangeGhost);
			World.schedule(2, () -> {
				if (!cerberus.isDead()) {
					new Projectile(1248, 52, 125, 43, 31).send(meleeGhost, target);
					World.schedule(3, () -> {
						if (!target.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
							target.damage(new Hit(30));
						}
					});
					World.schedule(2, () -> meleeGhost.movement.walk(new Position(1239, 1265)));
				}
			});
			World.schedule(6, () -> {
				if (!cerberus.isDead()) {
					new Projectile(100, 52, 125, 43, 31).send(mageGhost, target);
					World.schedule(3, () -> {
						if (!target.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
							target.damage(new Hit(30));
						}
					});
					World.schedule(2, () -> mageGhost.movement.walk(new Position(1240, 1265)));
				}
			});
			World.schedule(10, () -> {
				if (!cerberus.isDead()) {
					new Projectile(24, 52, 125, 43, 31).send(rangeGhost, target);
					World.schedule(3, () -> {
						if (!target.prayer.isActive(Prayer.PROTECT_FROM_RANGE)) {
							target.damage(new Hit(30));
						}
					});
					World.schedule(2, () -> rangeGhost.movement.walk(new Position(1241, 1265)));
				}
			});
			World.schedule(15, () -> {
				meleeGhost.unregister();
				mageGhost.unregister();
				rangeGhost.unregister();
			});
		}
	}

	private static enum CerberusSpecial {
		LAVA("Grrrrrrrrrrrrrr"), GHOSTS("Aaarrroooooooo");

		private final String forceChat;

		CerberusSpecial(String forceChat) {
			this.forceChat = forceChat;
		}
	}
}