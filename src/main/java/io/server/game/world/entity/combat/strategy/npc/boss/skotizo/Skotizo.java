package io.server.game.world.entity.combat.strategy.npc.boss.skotizo;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.world.World;
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
import io.server.game.world.position.Position;
import io.server.util.Utility;

/**
 * Handles Skotizo's stategy.
 *
 * @author Ad
 */
public class Skotizo extends MultiStrategy {
	private static Magic MAGIC = new Magic();
	private static TeleGrab TELE_GRAB = new TeleGrab();

	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC,
			TELE_GRAB);
	private static final CombatStrategy<Npc>[] MAGIC_STRATEGIES = createStrategyArray(MAGIC, MAGIC, MAGIC, TELE_GRAB);

	private static final String[] SHOUTS = { "Feel the wrath of Skotizo!", "The dark times have come for you all!" };

	/** Constructs a new <code>Skotizo</code>. */
	public Skotizo() {
		currentStrategy = MAGIC;
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(MAGIC_STRATEGIES);
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		currentStrategy.block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);

		if (!defender.getCombat().isAttacking()) {
			defender.animate(new Animation(69, UpdatePriority.VERY_HIGH));
			defender.graphic(481);
			defender.speak("ARHHHH! TIME TO SWITCH IT UP!!");



				World.schedule(2, () -> {
					Position destination = Utility.randomElement(defender.boundaries);
					World.sendGraphic(new Graphic(481), destination);
					attacker.move(destination);
					if (attacker.isPlayer())
						attacker.getPlayer().message("Skotizo has moved you around!");
				});
		}
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

	/** Skotizo's magic strategy. */
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
			Projectile projectile = new Projectile(1242, 50, 80, 85, 25);
			attacker.animate(new Animation(69, UpdatePriority.VERY_HIGH));	
			World.schedule(2, () -> attacker.damage(nextMagicHit(attacker, attacker, 38)));
		    projectile.send(defender, attacker);
       
			if (Utility.random(0, 2) == 1)
				attacker.speak(Utility.randomElement(SHOUTS));
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
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			attacker.animate(new Animation(69, UpdatePriority.VERY_HIGH));
			attacker.graphic(481);
			attacker.speak("ARHHHH! TIME TO SWITCH IT UP!!");
if(Utility.random(1, 25) == 1) {
		//	RegionManager.forNearbyPlayer(attacker, 16, other -> World.schedule(1, () -> {
				Position destination = Utility.randomElement(attacker.boundaries);
				World.sendGraphic(new Graphic(481), destination);
				

				if (attacker.isPlayer()) {
					attacker.move(destination);
					attacker.getPlayer().message("Skotizo has moved you around!");
				}	
			}
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit hit = nextMagicHit(attacker, defender, 38);
			hit.setAccurate(false);
			return new CombatHit[] { hit };
		}
	}
}
