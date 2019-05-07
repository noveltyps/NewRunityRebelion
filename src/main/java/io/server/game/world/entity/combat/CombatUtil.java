package io.server.game.world.entity.combat;

import static io.server.content.activity.impl.battlerealm.BattleRealmCallers.canFight;
import static io.server.content.activity.impl.battlerealm.BattleRealmCallers.cantFight;

import java.util.List;
import java.util.function.Consumer;

import io.server.content.activity.ActivityType;
import io.server.content.skill.impl.slayer.SlayerTask;
import io.server.game.Animation;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.world.World;
import io.server.game.world.entity.combat.effect.AntifireDetails;
import io.server.game.world.entity.combat.effect.CombatEffect;
import io.server.game.world.entity.combat.effect.CombatEffectType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.HitIcon;
import io.server.game.world.entity.combat.hit.Hitsplat;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.data.PacketType;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.game.world.position.Area;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * A collection of util methods and constants related to combat.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatUtil {

	/**
	 * The default constructor.
	 *
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private CombatUtil() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}

	/**
	 * Executes an action for mobs within a 3x3 square, including the source
	 * {@code mob}.
	 *
	 * @param mob    the mob to generate an area for
	 * @param action the action to apply to all mobs in the area
	 */
	public static void areaAction(Mob mob, Consumer<Mob> action) {
		action.accept(mob);
		areaAction(mob, 3 * 3, 1, action);
	}

	/**
	 * Sends an action to {@link Mob} instance which is within a {@code
	 * distance}.
	 *
	 * @param action action consumer.
	 */
	public static void areaAction(Mob mob, int max, int distance, Consumer<Mob> action) {
		if (!Area.inMulti(mob)) {
			return;
		}

		int added = 0;
		List<Player> players = World.getRegions().getLocalPlayers(mob);
		players.sort((first, second) -> {
			int firstD = Utility.getDistance(first, mob);
			int secondD = Utility.getDistance(second, mob);
			return firstD - secondD;
		});

		for (Player other : players) {
			if (other == null)
				continue;
			if (other.instance != mob.instance)
				continue;
			if (!Utility.withinViewingDistance(other, mob, distance))
				continue;
			if (other.equals(mob))
				continue;
			if (other.getCurrentHealth() <= 0 || other.isDead())
				continue;
			if (!Area.inMulti(other))
				continue;
			if (mob.isPlayer() && other.isPlayer() && (!Area.inWilderness(mob) || !Area.inWilderness(other)))
				continue;
			action.accept(other);
			if (++added == max)
				return;
		}

		List<Npc> npcs = World.getRegions().getLocalNpcs(mob);
		npcs.sort((first, second) -> {
			int firstD = Utility.getDistance(first, mob);
			int secondD = Utility.getDistance(second, mob);
			return firstD - secondD;
		});

		for (Npc other : npcs) {
			if (other == null)
				continue;
			if (other.instance != mob.instance)
				continue;
			if (!Utility.withinViewingDistance(other, mob, distance))
				continue;
			if (other.equals(mob))
				continue;
			if (other.getCurrentHealth() <= 0 || other.isDead())
				continue;
			if (!Area.inMulti(other))
				continue;
			if (!other.definition.isAttackable())
				continue;
			action.accept(other);
			if (++added == max)
				return;
		}
	}

	/**
	 * Gets the hit delay for the specified {@code type}.
	 *
	 * @param attacker the character doing the hit
	 * @param defender the victim being hit
	 * @param type     the combat type of this hit
	 * @return the delay for the combat type
	 */
	public static int getHitDelay(Mob attacker, Mob defender, CombatType type) {
		if (!type.equals(CombatType.MELEE)) {
			int distance = Utility.getDistance(attacker, defender);

			if (distance > 10) {
				distance = 10;
			}

			if (type.equals(CombatType.MAGIC)) {
				return Projectile.MAGIC_DELAYS[distance] - 1;
			}

			if (type.equals(CombatType.RANGED)) {
				return Projectile.RANGED_DELAYS[distance] - 1;
			}
		}

		/* Otherwise, melee */
		return 0;
	}

	/**
	 * Gets the hitsplat delay for the specified {@code type}.
	 *
	 * @param type the combat type of this hit
	 * @return the delay for the combat type
	 */
	public static int getHitsplatDelay(CombatType type) {
		if (!type.equals(CombatType.MELEE)) {
			return 1;
		}

		return 0;
	}

	static boolean validateMobs(Mob attacker, Mob defender) {
		if (!validate(attacker) || !validate(defender)) {
			attacker.getCombat().reset();
			return false;
		}

		if (attacker.instance != defender.instance) {
			attacker.getCombat().reset();
			return false;
		}

		if (!canAttack(attacker, defender)) {
			attacker.getCombat().reset();
			return false;
		}
		return true;
	}

	/**
	 * Applies the {@code effect} in any context.
	 *
	 * @param effect the effect that must be applied
	 * @return {@code true} if it was successfully applied
	 */
	public static boolean effect(Mob mob, CombatEffectType effect) {
		return CombatEffect.EFFECTS.get(effect).start(mob);
	}

	/**
	 * Cancels the {@code effect} in any context.
	 *
	 * @param effect the effect that must be applied
	 * @return {@code true} if it was successfully applied
	 */
	public static boolean cancelEffect(Mob mob, CombatEffectType effect) {
		return CombatEffect.EFFECTS.get(effect).removeOn(mob);
	}

	public static boolean canAttack(Mob attacker, Mob defender) {
		if (attacker.isPlayer())
			return canAttack(attacker.getPlayer(), defender);
		return canAttack(attacker.getNpc(), defender);
	}

	private static boolean canAttack(Player attacker, Mob defender) {
		if (defender.isNpc() && !SlayerTask.canAttack(attacker, defender.id)) {
			attacker.send(new SendMessage("You do not meet the slayer requirements to attack this npc!"));
			return false;
		}
		if (attacker.equals(defender)) {
			/*attacker.send(new SendMessage("Okay low-key, you're not supposed to be able to attack yourself."));
			attacker.send(new SendMessage("But as a developer (Red Bracket), I'm super curious to know how"));
			attacker.send(new SendMessage("you pulled this off. I'm gonna let you do it, but please don't"));
			attacker.send(new SendMessage("break anything :)"));
			//World.sendMessage(attacker.getName() + " is potentially bug abusing. Someone go check him out!");
			// attacker.send(new SendMessage("You can't attack yourself!"));
			// return false;*/
			return true;
		}

		if (defender.isNpc() && defender.getNpc().owner != null && !attacker.equals(defender.getNpc().owner)) {
			attacker.send(new SendMessage("You can't attack this npc!"));
			return false;
		}

		if (attacker.RebelionXMode && defender.RebelionXMode) {
			return true;
		}
		if (attacker.getCombat().isUnderAttack() && !attacker.getCombat().isUnderAttackBy(defender)) {
			if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
				attacker.send(new SendMessage("You are already under attack!"));
				return false;
			}
		}

		if (defender.getCombat().isUnderAttack() && !defender.getCombat().isUnderAttackBy(attacker)) {
			if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
				if (defender.isPlayer()) {
					attacker.send(
							new SendMessage(defender.getName() + " is currently in combat and can not be attacked."));
				} else {
					attacker.send(new SendMessage("This monster is already under attack!"));
				}
				return false;
			}
		}
		if (defender.isPlayer()) {
			if (attacker.locking.locked(PacketType.COMBAT)) {
				return false;
			}
			
			if (attacker.inActivity(ActivityType.DUEL_ARENA) && defender.inActivity(ActivityType.DUEL_ARENA)) {
				return true;
			}

			// BattleRealm Stuff
			if (canFight(attacker, defender)) {
				return true;
			} else if (cantFight(attacker, defender)) {
				attacker.message("You cannot kill your own team!");
				return false;
			}

			if (Area.inEventArena(attacker) && Area.inEventArena(defender)) {
				return true;
			}

			int difference = (int) Math.abs(attacker.skills.getCombatLevel() - defender.skills.getCombatLevel());

			if (attacker.RebelionXMode && !defender.RebelionXMode) {
				attacker.send(new SendMessage("Sorry, " + defender.getName() + " is not a RebelionX Man."));
				return false;
			}

			if (difference > attacker.wilderness && Area.inWilderness(attacker)) {
				attacker.send(new SendMessage("Your combat level difference is too great!"));
				return false;
			}

			if (!Area.inWilderness(attacker)) {
				attacker.send(new SendMessage(
						"You need to be in the wilderness to attack " + Utility.formatName(defender.getName()) + "."));
				return false;
			}

			if (!Area.inWilderness(defender)) {
				attacker.send(new SendMessage(
						Utility.formatName(defender.getName()) + " must be in the wilderness for you to attack."));
				return false;
			}

		}
		return true;
	}

	private static boolean canAttack(Npc attacker, Mob defender) {
		if (attacker.equals(defender)) {
			return false;
		}
		if (defender.isNpc() && defender.getNpc().owner != null && !defender.getNpc().owner.equals(attacker)) {
			return false;
		}
		if (attacker.getCombat().isUnderAttack() && !attacker.getCombat().isUnderAttackBy(defender)) {
			if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
				return false;
			}
		}
		if (defender.getCombat().isUnderAttack() && !defender.getCombat().isUnderAttackBy(attacker)) {
			if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
				return false;
			}
		}
		return true;
	}

	public static boolean canBasicAttack(Mob attacker, Mob defender) {
		if (attacker.equals(defender)) {
			return false;
		}
		if (defender.isNpc() && defender.getNpc().owner != null && !attacker.equals(defender.getNpc().owner)) {
			return false;
		}
		if (attacker.getCombat().isUnderAttack() && !attacker.getCombat().isUnderAttackBy(defender)) {
			if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
				return false;
			}
		}
		if (defender.getCombat().isUnderAttack() && !defender.getCombat().isUnderAttackBy(attacker)) {
			if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
				return false;
			}
		}
		if (attacker.isPlayer() && defender.isPlayer()) {
			int difference = (int) Math.abs(attacker.skills.getCombatLevel() - defender.skills.getCombatLevel());
			if (difference > attacker.getPlayer().wilderness) {
				return false;
			}
			if (!Area.inWilderness(attacker)) {
				return false;
			}
			if (!Area.inWilderness(defender)) {
				return false;
			}
		}
		return true;
	}

	private static boolean validate(Mob mob) {
		return mob != null && !mob.isDead() && mob.isVisible() && mob.isValid() && !mob.teleporting && !mob.inTeleport;
	}

	static Animation getBlockAnimation(Mob mob) {
		int animation = 404;
		if (mob.isPlayer()) {
			if (mob.getPlayer().equipment.hasShield()) {
				Item shield = mob.getPlayer().equipment.getShield();
				animation = shield.getBlockAnimation().orElse(424);
			} else if (mob.getPlayer().equipment.hasWeapon()) {
				animation = 404;// TODO
			}
		} else {
			Npc npc = mob.getNpc();
			animation = npc.definition.getBlockAnimation();
		}
		int delay = (int) mob.getCombat().lastBlocked.elapsedTime();
		if (delay < 600)
			return new Animation(animation, delay / 50, UpdatePriority.LOW);
		return new Animation(animation, UpdatePriority.LOW);
	}

	public static CombatHit generateDragonfire(Mob attacker, Mob defender, int max, boolean prayer) {
		int hitDelay = getHitDelay(attacker, defender, CombatType.MAGIC);
		int hitsplatDelay = 1;
		return generateDragonfire(attacker, defender, max, hitDelay, hitsplatDelay, prayer);
	}

	public static CombatHit generateDragonfire(Mob attacker, Mob defender, int max, int hitDelay, int hitsplatDelay,
			boolean prayer) {
		int damage;

		if (defender.isPlayer()) {
			Player player = defender.getPlayer();

			if (Equipment.isWearingDFS(player) && player.dragonfireCharges < 50 && Utility.random(1, 8) == 2) {
				if (player.equipment.getShield().getId() == 11284) {
					player.equipment.set(Equipment.SHIELD_SLOT, new Item(11283, 1), true);
					player.equipment.refresh();
				}
				player.animate(6695);
				player.graphic(1164);
				player.dragonfireCharges++;
				player.getCombat().setCooldown(5);
				player.send(new SendMessage("Your dragonfire Shield Absorbs the Dragon breath."));
				player.face(attacker);
				damage = 0;
			} else {
				prayer &= player.prayer.isActive(Prayer.PROTECT_FROM_MAGIC);
				boolean shield = player.equipment.containsAny(1540, 11283, 1);
				boolean potion = player.getAntifireDetails().isPresent();

				if (shield && potion) {
					max = 0;
				} else if (potion) {
					AntifireDetails.AntifireType type = player.getAntifireDetails().get().getType();
					max -= type.getReduction();
					if (max <= 0) {
						max = 0;
					}
				} else if (shield) {
					max -= 50;
				} else if (prayer) {
					max -= 45;
				}

				damage = max <= 0 ? 0 : RandomUtils.inclusive(max);
				if (damage >= 15) {
					player.send(new SendMessage("You are horribly burned by the dragonfire!"));
				} else if (!shield && !potion && !prayer && damage < 9 && damage > 0) {
					player.send(new SendMessage("You manage to resist some of the dragonfire."));
				}
			}
		} else {
			damage = max == 0 ? 0 : RandomUtils.inclusive(max);
		}

		Hit hit = new Hit(damage, Hitsplat.NORMAL, HitIcon.NONE, true);
		return new CombatHit(hit, hitDelay, hitsplatDelay);
	}

	public static CombatStrategy<Npc> randomStrategy(CombatStrategy<Npc>[] strategies) {
		return RandomUtils.random(strategies);
	}

	@SafeVarargs
	public static CombatStrategy<Npc>[] createStrategyArray(CombatStrategy<Npc>... strategies) {
		return strategies;
	}

}
