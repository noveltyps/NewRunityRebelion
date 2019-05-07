package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.util.RandomUtils;

/**
 * @author red
 */
@NpcCombatListenerSignature(npcs = { 1674 })
@ItemCombatListenerSignature(requireAll = true, items = { 4708, 4710, 4712, 4714 })
public class AhrimsListener extends SimplifiedListener<Mob> {

	@Override
	public int modifyDefensive(Mob attacker, Mob defender, int roll) {
		return roll / 2;
	}

	@Override
	public int modifyDefenceLevel(Mob attacker, Mob defender, int level) {
		return level * 2;
	}

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		if (hit.getDamage() == 0) {
			hit.setDamage(RandomUtils.inclusive(0, 20));
		}
	}
}
