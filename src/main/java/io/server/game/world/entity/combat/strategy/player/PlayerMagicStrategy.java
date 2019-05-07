package io.server.game.world.entity.combat.strategy.player;

import static io.server.game.world.items.containers.equipment.Equipment.WEAPON_SLOT;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatImpact;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.magic.CombatSpell;
import io.server.game.world.entity.combat.magic.MagicRune;
import io.server.game.world.entity.combat.strategy.basic.MagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;
import io.server.util.Utility;

public class PlayerMagicStrategy extends MagicStrategy<Player> {

	/** The magic spell definition. */
	private final CombatSpell spell;

	/** The spell splash graphic. */
	public static final Graphic SPLASH = new Graphic(85);

	/** Constructs a new {@code SpellStrategy} from a {@link CombatSpell}. */
	public PlayerMagicStrategy(CombatSpell spell) {
		this.spell = spell;
	}

	@Override
	public boolean canAttack(Player attacker, Mob defender) {
		if (defender.isPlayer() && defender.getPlayer().isBot) {
			attacker.message("You can't attack bots with magic.");
			return false;
		}

		if (spell == CombatSpell.TELE_BLOCK) {
			if (defender.isPlayer()) {
				if (defender.getPlayer().isTeleblocked()) {
					attacker.message("This player is already affected by this spell!");
					return false;
				}
			} else if (defender.isNpc()) {
				attacker.message("You can't teleblock a npc!");
				return false;
			}
		}

		if (/* PlayerRight.isDeveloper(attacker) || */ spell.canCast(attacker, defender)) {
			return true;
		}

		attacker.send(new SendMessage("You need some runes to cast this spell."));
		attacker.getCombat().reset();
		return false;
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		if (attacker.getCombat().getDefender() == defender) {
			Animation animation = spell.getAnimation().orElse(getAttackAnimation(attacker, defender));
			attacker.animate(animation);
			spell.getStart().ifPresent(attacker::graphic);
			spell.sendProjectile(attacker, defender);

			if (spell.getEffect().isPresent()) {
				List<Hit> extra = new LinkedList<>();
				for (Hit hit : hits) {
					Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
					Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, extra);
					spell.getEffect().filter(filter).ifPresent(execute);
				}

				if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
					if (extra.isEmpty()) {
						Collections.addAll(extra, hits);
						addCombatExperience(attacker, spell.getBaseExperience(), extra.toArray(new Hit[0]));
					} else {
						addCombatExperience(attacker, spell.getBaseExperience(), hits);
					}
				} else {
					attacker.skills.addExperience(Skill.MAGIC, spell.getBaseExperience());
				}
			} else if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
				addCombatExperience(attacker, spell.getBaseExperience(), hits);
			} else {
				attacker.skills.addExperience(Skill.MAGIC, spell.getBaseExperience());
			}
		}
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		if (defender.equals(attacker.getCombat().getDefender())) {
			MagicRune.remove(attacker, spell.getRunes());
		}

		if (attacker.isSingleCast()) {
			attacker.face(defender.getPosition());
			attacker.setSingleCast(null);
			attacker.getCombat().reset();
		}
	}

	@Override
	public void hit(Player attacker, Mob defender, Hit hit) {
		if (!hit.isAccurate()) {
			defender.graphic(SPLASH);
		} else {
			if (attacker.equipment.retrieve(WEAPON_SLOT).filter(item -> item.getId() == 12904).isPresent()
					&& RandomUtils.success(0.25)) {
				defender.venom();
			}
			spell.getEnd().ifPresent(defender::graphic);
		}
	}

	@Override
	public void hitsplat(Player attacker, Mob defender, Hit hit) {
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[] { nextMagicHit(attacker, defender, spell.getCombatProjectile()) };
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		if (Utility.getDistance(attacker, defender) >= 8) {
			return 6;
		}
		return 5;
	}

	@Override
	public int getAttackDistance(Player attacker, FightType fightType) {
		return 10;
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		FightType fightType = attacker.getCombat().getFightType();
		int animation = fightType.getAnimation();

		if (attacker.equipment.hasShield()) {
			Item weapon = attacker.equipment.getShield();
			animation = weapon.getAttackAnimation(fightType).orElse(animation);
		}

		if (attacker.equipment.hasWeapon()) {
			Item weapon = attacker.equipment.getWeapon();
			animation = weapon.getAttackAnimation(fightType).orElse(animation);
		}

		return new Animation(animation, UpdatePriority.HIGH);
	}

	/*
	 * @Override public int modifyAccuracy(Player attacker, Mob defender, int roll)
	 * { if (CombatUtil.isFullVoid(attacker)) { if
	 * (attacker.equipment.contains(11663)) { roll *= 1.45; } } return roll; }
	 */

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

	public CombatSpell getSpell() {
		return spell;
	}
}