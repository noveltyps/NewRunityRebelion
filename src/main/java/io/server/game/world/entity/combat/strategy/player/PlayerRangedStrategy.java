package io.server.game.world.entity.combat.strategy.player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import io.server.content.prestige.PrestigePerk;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatImpact;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.effect.impl.CombatPoisonEffect;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.ranged.RangedAmmunition;
import io.server.game.world.entity.combat.ranged.RangedWeaponType;
import io.server.game.world.entity.combat.strategy.basic.RangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;

/**
 * @author daniel
 * @edited by Adam_#66723
 *
 */

public class PlayerRangedStrategy extends RangedStrategy<Player> {

	private static final PlayerRangedStrategy INSTANCE = new PlayerRangedStrategy();
	private int hasRecursed = 0;

	protected PlayerRangedStrategy() {
	}

	@Override
	public boolean canAttack(Player attacker, Mob defender) {
		Item weapon = attacker.equipment.get(Equipment.WEAPON_SLOT);

		if (weapon == null) {
			attacker.getCombat().reset();
			return false;
		}

		if (attacker.rangedDefinition == null && hasRecursed == 0) {
			hasRecursed = 1;
			System.out.println("Ranged Equipment is null, Executing Adam's hotfix for it.");
			attacker.equipment.updateRangedEquipment();
			return canAttack(attacker, defender);
		}

		hasRecursed = 0;

		Item ammo = attacker.equipment.get(attacker.rangedDefinition.getSlot());
		if (ammo != null && attacker.rangedAmmo != null && ammo.getAmount() >= attacker.rangedAmmo.getRemoval()) {
			if (attacker.rangedDefinition.isValid(attacker.rangedAmmo)) 
				return true;
			attacker.send(new SendMessage("You can't use this ammunition with this weapon."));
		} else 
			attacker.send(new SendMessage("You need some ammunition to use this weapon!"));

		attacker.getCombat().reset();
		return false;
	}

	protected void sendStuff(Player attacker, Mob defender) {
		int id = attacker.equipment.get(attacker.rangedDefinition.getSlot()).getId();
		Animation animation = attacker.rangedAmmo.getAnimation(id).orElse(getAttackAnimation(attacker, defender));
		attacker.animate(animation);
		attacker.rangedAmmo.getStart(id).ifPresent(attacker::graphic);
		attacker.rangedAmmo.sendProjectile(attacker, defender);
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		if (attacker.isSpecialActivated()) {
			attacker.getCombatSpecial().drain(attacker);
		}

		if (attacker.getCombat().getDefender() == defender) {
			sendStuff(attacker, defender);

			int id = attacker.equipment.get(attacker.rangedDefinition.getSlot()).getId();
			if (attacker.rangedAmmo.getEffect(id).isPresent()) {
				List<Hit> extra = new LinkedList<>();
				for (Hit hit : hits) {
					Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
					Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, extra);
					attacker.rangedAmmo.getEffect(id).filter(filter).ifPresent(execute);
				}
				if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
					if (extra.isEmpty()) {
						Collections.addAll(extra, hits);
						addCombatExperience(attacker, extra.toArray(new Hit[0]));
					} else {
						addCombatExperience(attacker, hits);
					}
				}
			} else if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
				addCombatExperience(attacker, hits);
			}
		}
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		removeAmmunition(attacker, defender, attacker.rangedDefinition.getType());
		if (hit.getDamage() > 1 && attacker.rangedDefinition != null) {
			Item item = attacker.equipment.get(attacker.rangedDefinition.getSlot());
			CombatPoisonEffect.getPoisonType(item).ifPresent(defender::poison);
		}
	}

	@Override
	public void hit(Player attacker, Mob defender, Hit hit) {
		if (attacker.rangedAmmo != null && attacker.rangedDefinition != null) {
			int id = attacker.equipment.retrieve(attacker.rangedDefinition.getSlot()).map(Item::getId).orElse(-1);
			attacker.rangedAmmo.getEnd(id).ifPresent(defender::graphic);
		}
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		int animation = attacker.getCombat().getFightType().getAnimation();

		if (attacker.equipment.hasShield()) {
			Item weapon = attacker.equipment.getShield();
			FightType fightType = attacker.getCombat().getFightType();
			animation = weapon.getAttackAnimation(fightType).orElse(animation);
		}

		if (attacker.equipment.hasWeapon()) {
			Item weapon = attacker.equipment.getWeapon();
			FightType fightType = attacker.getCombat().getFightType();
			animation = weapon.getAttackAnimation(fightType).orElse(animation);
		}

		return new Animation(animation, UpdatePriority.HIGH);
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return attacker.getCombat().getFightType().getDelay();
	}

	@Override
	public int getAttackDistance(Player attacker, FightType fightType) {
		return fightType.getDistance();
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		RangedAmmunition ammo = attacker.rangedAmmo;
		CombatHit[] hits = new CombatHit[ammo.getRemoval()];
		for (int index = 0; index < hits.length; index++) {
			hits[index] = nextRangedHit(attacker, defender);
		}
		return hits;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}

	private void removeAmmunition(Player attacker, Mob defender, RangedWeaponType type) {
		Item next = attacker.equipment.get(type.getSlot());
		if (next == null)
			return;

		if (attacker.rangedAmmo.isDroppable()) {
			Item dropItem = new Item(next.getId());

			boolean canBreak = !dropItem.getName().contains("arrow")
					|| !attacker.prestige.hasPerk(PrestigePerk.ARROWHEAD);

			if (!canBreak || RandomUtils.success(0.75)) {
				if (Equipment.hasAva(attacker) && RandomUtils.success(0.96)) {
					return;
				}

				Position dropPoisition = defender.getPosition();

				if (Area.inKraken(attacker) || Area.inZulrah(attacker)) {
					dropPoisition = attacker.getPosition();
				}

				GroundItem.create(attacker, dropItem, dropPoisition);
			}
		}

		next.decrementAmount();
		if (next.getAmount() == 0) {
			attacker.send(new SendMessage("That was the last of your ammunition!"));
			next = null;
		}
		attacker.equipment.set(type.getSlot(), next, true);
	}

	public static PlayerRangedStrategy get() {
		return INSTANCE;
	}

}
