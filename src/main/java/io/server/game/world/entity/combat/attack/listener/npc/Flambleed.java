package io.server.game.world.entity.combat.attack.listener.npc;

import static io.server.game.world.entity.combat.CombatUtil.createStrategyArray;
import static io.server.game.world.entity.combat.CombatUtil.randomStrategy;
import static io.server.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import io.server.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = { 4881 })
public class Flambleed extends SimplifiedListener<Npc> {

	private static MagicAttack MAGIC;
	private static CombatStrategy<Npc>[] STRATEGIES;

	static {
		try {
			MAGIC = new MagicAttack();
			STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(MAGIC);
		}
		return attacker.getStrategy().canAttack(attacker, defender);
	}

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(MAGIC);
		} else {
			attacker.setStrategy(randomStrategy(STRATEGIES));
		}
	}

	@Override
	public void hit(Npc attacker, Mob defender, Hit hit) {
		if (!defender.isPlayer()) {
			return;
		}
		Player player = defender.getPlayer();
		if (!player.equipment.hasWeapon()) {
			return;
		}
		if (player.equipment.contains(1580)) {
			return;
		}
		Item weapon = player.equipment.getWeapon();
		player.equipment.unEquip(weapon);
		player.send(new SendMessage("Flambeeds has removed your weapon since you are not wearing ice gloves."));
	}

	private static class MagicAttack extends NpcMagicStrategy {
		private MagicAttack() {
			super(getDefinition("Flames Of Zamorak"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return new Animation(1750, UpdatePriority.HIGH);
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, combatProjectile) };
		}
	}
}
