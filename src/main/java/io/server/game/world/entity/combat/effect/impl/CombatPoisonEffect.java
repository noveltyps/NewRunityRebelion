package io.server.game.world.entity.combat.effect.impl;

import java.util.Optional;

import io.server.game.world.entity.combat.PoisonType;
import io.server.game.world.entity.combat.effect.CombatEffect;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.HitIcon;
import io.server.game.world.entity.combat.hit.Hitsplat;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendPoison;

/**
 * The combat effect applied when a character needs to be poisoned.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatPoisonEffect extends CombatEffect {

	/** The amount of times this player has been hit. */
	private int amount;

	/** Creates a new {@link CombatPoisonEffect}. */
	public CombatPoisonEffect() {
		super(25);
	}

	@Override
	public boolean apply(Mob mob) {
		if (mob.getPoisonType() == null || mob.isPoisoned() || mob.isVenomed()) {
			return false;
		}

		if (mob.isNpc() && mob.getNpc().definition.hasPoisonImmunity()) {
			return false;
		}

		if (mob.isPlayer() && mob.getPlayer().equipment.retrieve(Equipment.HELM_SLOT)
				.filter(helm -> helm.getId() == 13197 || helm.getId() == 13199 || helm.getId() == 12931).isPresent()) {
			return true;
		} // ADAM INCASE THIS DOESN'T WORK REFER BACK TO HERE.

		if (mob.isPlayer()) {
			Player player = mob.getPlayer();
			if (player.getPoisonImmunity().get() > 0 || mob.isDead())
				return false;
			player.send(new SendMessage("You have been poisoned!"));
			player.send(new SendPoison(SendPoison.PoisonType.REGULAR));
		}
		mob.getPoisonDamage().set(mob.getPoisonType().getDamage());
		amount = 4;
		return true;
	}

	@Override // removed the !mob.isPoisoned(); ADAM DID THIS :d
	public boolean removeOn(Mob mob) {
		boolean remove = mob.isVenomed() || /* !mob.isPoisoned() */!mob.isPoisoned() || mob.isDead();
		if (remove && mob.isPlayer()) {
			Player player = (Player) mob;
			player.send(new SendPoison(SendPoison.PoisonType.NO_POISON));
		}
		return remove;
	}

	@Override
	public void process(Mob mob) {
		amount--;
		mob.damage(new Hit(mob.getPoisonDamage().get(), Hitsplat.POISON, HitIcon.NONE));
		if (amount == 0) {
			amount = 4;
			mob.getPoisonDamage().decrementAndGet();
		}
	}

	@Override
	public boolean onLogin(Mob mob) {
		boolean poisoned = mob.isPoisoned();
		if (poisoned && mob.isPlayer()) {
			mob.getPlayer().send(new SendPoison(SendPoison.PoisonType.REGULAR));
		}
		return poisoned;
	}

	/**
	 * Gets the {@link PoisonType} for {@code item} wrapped in an optional. If a
	 * poison type doesn't exist for the item then an empty optional is returned.
	 *
	 * @param item the item to get the poison type for
	 * @return the poison type for this item wrapped in an optional, or an empty
	 *         optional if no poison type exists
	 */
	public static Optional<PoisonType> getPoisonType(Item item) {
		if (item != null) {
			String name = item.getName();
			if (name.endsWith("(p)")) {
				return Optional.of(PoisonType.DEFAULT_MELEE);
			}
			if (name.endsWith("(p+)")) {
				return Optional.of(PoisonType.STRONG_MELEE);
			}
			if (name.endsWith("(p++)")) {
				return Optional.of(PoisonType.SUPER_MELEE);
			}
			if (name.endsWith("tentacle")) {
				return Optional.of(PoisonType.SUPER_MELEE);
			}

		}
		return Optional.empty();
	}

	/**
	 * Gets the {@link PoisonType} for {@code npc} wrapped in an optional. If a
	 * poison type doesn't exist for the NPC then an empty optional is returned.
	 *
	 * @param npc the NPC to get the poison type for
	 * @return the poison type for this NPC wrapped in an optional, or an empty
	 *         optional if no poison type exists
	 */
	public static Optional<PoisonType> getPoisonType(int npc) {
		NpcDefinition def = NpcDefinition.DEFINITIONS[npc];

		if (def == null || !def.isAttackable() || !def.isPoisonous()) {
			return Optional.empty();
		}

		if (def.getCombatLevel() < 25) {
			return Optional.of(PoisonType.WEAK_NPC);
		}

		if (def.getCombatLevel() < 75) {
			return Optional.of(PoisonType.DEFAULT_NPC);
		}

		if (def.getCombatLevel() < 200) {
			return Optional.of(PoisonType.STRONG_NPC);
		}

		if (def.getCombatLevel() < 225) {
			return Optional.of(PoisonType.SUPER_NPC);
		}

		return Optional.of(PoisonType.EXTRAORDINARY_NPC);
	}
}
