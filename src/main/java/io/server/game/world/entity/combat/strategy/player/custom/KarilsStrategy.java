package io.server.game.world.entity.combat.strategy.player.custom;

import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;

public class KarilsStrategy extends PlayerRangedStrategy {
	
	private static final KarilsStrategy INSTANCE = new KarilsStrategy();

	public String name() {
		return "Karil's crossbow";
	}

	
	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
	
	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 2;
	}
	
	/** Instane's the class to be called upon,and applied to an item. **/
	public static KarilsStrategy get() {
		return INSTANCE;
	}
}
