package io.server.game.world.entity.combat.effect.impl;

import io.server.game.world.entity.combat.effect.CombatEffect;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.HitIcon;
import io.server.game.world.entity.combat.hit.Hitsplat;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendPoison;

/**
 * The combat effect applied when a character needs to be venomed.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatVenomEffect extends CombatEffect {

	/** Creates a new {@link CombatVenomEffect}. */
	public CombatVenomEffect() {
		super(30);
	}

	@Override
	public boolean apply(Mob mob) {
		if (mob.isVenomed()) {
			return false;
		}

		if (mob.isNpc() && mob.getNpc().definition.hasVenomImmunity()) {
			return false;
		}

		if (mob.isPlayer() && mob.getPlayer().equipment.retrieve(Equipment.HELM_SLOT)
				.filter(helm -> helm.getId() == 13197 || helm.getId() == 13199 || helm.getId() == 12931).isPresent()) {
			return false;
		}

		if (mob.isPlayer()) {
			Player player = mob.getPlayer();
			if (player.getVenomImmunity().get() > 0 || mob.isDead())
				return false;
			player.send(new SendMessage("You have been venomed!"));
			player.send(new SendPoison(SendPoison.PoisonType.VENOM));
		}
		mob.getVenomDamage().set(6);
		return true;
	}

	@Override
	public boolean removeOn(Mob mob) {
		boolean remove = !mob.isVenomed() || mob.isDead();
		if (remove && mob.isPlayer()) {
			Player player = (Player) mob;
			player.send(new SendPoison(SendPoison.PoisonType.NO_POISON));
		}
		return remove;
	}

	@Override
	public void process(Mob mob) {
		if (mob.getVenomDamage().get() < 20)
			mob.damage(new Hit(mob.getVenomDamage().getAndIncrement(2), Hitsplat.DISEASE, HitIcon.NONE));
	}

	@Override
	public boolean onLogin(Mob mob) {
		if (mob.isVenomed() && mob.isPlayer()) {
			mob.getPlayer().send(new SendPoison(SendPoison.PoisonType.REGULAR));
		}
		return mob.isVenomed();
	}

	public static boolean isVenomous(Npc npc) {
		return npc.id == 2042 || npc.id == 2043 || npc.id == 2044;
	}

	/**
	 * Added a quick null check as the weapon was throwing errors and nulling out.
	 **/
	public static boolean isVenomous(Item weapon) {

		if (weapon == null) {
			return true;
		}

		return weapon.matchesId(12_926) || weapon.matchesId(12_904) || weapon.matchesId(12_899);

	}

}
