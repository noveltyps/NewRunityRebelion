package io.server.content.activity.impl.duelarena;

import java.util.Optional;

import io.server.content.activity.ActivityListener;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.position.Area;
import io.server.net.packet.out.SendMessage;

/**
 * Created by Daniel on 2017-09-17.
 */
public class DuelArenaListener extends ActivityListener<DuelArenaActivity> {

	DuelArenaListener(DuelArenaActivity activity) {
		super(activity);
	}

	@Override
	public boolean canAttack(Mob attacker, Mob defender) {

		// duel arena is player vs player
		if (attacker.isNpc() || defender.isNpc()) {
			if (!Area.inDuelArena(attacker) && !Area.inDuelArenaLobby(attacker))
				return super.canAttack(attacker, defender);
			else
				return false;
		}

		Optional<Player> result = activity.getOther(attacker.getPlayer());

		Player player = attacker.getPlayer();

		if (!result.isPresent()) {
			player.send(new SendMessage("Other player does not exist."));
			return false;
		}

		Player opponent = result.get();

		// player can only attack his opponent
		if (!defender.getPlayer().equals(opponent)) {
			player.send(new SendMessage("You cannot attack other players!"));
			return false;
		}
		//yea i already found this, it seems to be programmed correctly.. i could attempt to fix it though
		if (!activity.hasDuelStarted()) {
			player.send(new SendMessage("The duel hasn't started yet!"));
			return false;
		}

		if (activity.getRules().contains(DuelRule.ONLY_FUN_WEAPONS)
				&& !DuelUtils.hasFunWeapon(player, player.equipment.getWeapon())) {
			player.send(new SendMessage("You can only use fun weapons!"));
			return false;
		}

		if (activity.getRules().contains(DuelRule.ONLY_WHIP_DDS)) {
			if (player.equipment.hasWeapon()) {
				Item weapon = player.equipment.getWeapon();
				String name = weapon.getName().toLowerCase();

				if (!name.contains("dragon dagger") && !name.contains("abyssal whip")
						&& !name.contains("abyssal tentacle") && !name.contains("lime whip")) {
					player.send(new SendMessage("You can only use a whip or dragon dagger!"));
					return false;
				}
			}
		}

		CombatType combatType = player.getStrategy().getCombatType();

		if (combatType == CombatType.MELEE && activity.getRules().contains(DuelRule.NO_MELEE)) {
			player.send(new SendMessage("You cannot use melee in the duel arena."));
			return false;
		}

		if (combatType == CombatType.RANGED && activity.getRules().contains(DuelRule.NO_RANGED)) {
			player.send(new SendMessage("You cannot use ranged in the duel arena."));
			return false;
		}

		if (combatType == CombatType.MAGIC && activity.getRules().contains(DuelRule.NO_MAGIC)) {
			player.send(new SendMessage("You cannot use magic in the duel arena."));
			return false;
		}

		return super.canAttack(attacker, defender);
	}
}