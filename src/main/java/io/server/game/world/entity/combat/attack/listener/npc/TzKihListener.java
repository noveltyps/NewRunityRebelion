package io.server.game.world.entity.combat.attack.listener.npc;

import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;

/** @author Daniel */
@NpcCombatListenerSignature(npcs = { 2189 })
public class TzKihListener extends SimplifiedListener<Npc> {

	@Override
	public void hit(Npc attacker, Mob defender, Hit hit) {
		if (!defender.isPlayer())
			return;

		Player player = defender.getPlayer();
		int prayer = player.skills.get(Skill.PRAYER).getLevel();

		if (prayer - 1 < 0)
			return;

		player.skills.setLevel(Skill.PRAYER, prayer - 1);
	}
}
