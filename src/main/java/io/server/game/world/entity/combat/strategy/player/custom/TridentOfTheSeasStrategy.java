package io.server.game.world.entity.combat.strategy.player.custom;

import io.server.Config;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.game.world.entity.combat.strategy.basic.MagicStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.net.packet.out.SendMessage;

public class TridentOfTheSeasStrategy extends MagicStrategy<Player> {
	private static final TridentOfTheSeasStrategy INSTANCE = new TridentOfTheSeasStrategy();
	private static CombatProjectile PROJECTILE;

	static {
		try {
			PROJECTILE = CombatProjectile.getDefinition("Trident of the Seas");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canAttack(Player attacker, Mob defender) {
		if (defender.isPlayer()) {
			attacker.send(new SendMessage("You can't attack players with this weapon."));
			return false;
		}

		Item weapon = attacker.equipment.get(Equipment.WEAPON_SLOT);

		if (weapon == null) {
			attacker.getCombat().reset();
			return false;
		}

		if (weapon.getId() == 11907 && attacker.tridentSeasCharges < 1) {
			attacker.send(new SendMessage("Your trident is out of charges!"));
			attacker.getCombat().reset();
			return false;
		}

		return true;
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		PROJECTILE.getAnimation().ifPresent(attacker::animate);
		PROJECTILE.getStart().ifPresent(attacker::graphic);
		PROJECTILE.getProjectile().ifPresent(projectile -> projectile.send(attacker, defender));

		if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
			for (Hit hit : hits) {
				int exp = 2 * hit.getDamage();
				attacker.skills.addExperience(Skill.MAGIC, exp * Config.COMBAT_MODIFICATION);
				attacker.skills.addExperience(Skill.HITPOINTS, exp / 3 * Config.COMBAT_MODIFICATION);
			}
		}
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		attacker.tridentSeasCharges--;
	}

	@Override
	public void hit(Player attacker, Mob defender, Hit hit) {
		PROJECTILE.getEnd().ifPresent(defender::graphic);
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		int animation = attacker.getCombat().getFightType().getAnimation();
		return new Animation(animation, UpdatePriority.HIGH);
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return attacker.getCombat().getFightType().getDelay();
	}

	@Override
	public int getAttackDistance(Player attacker, FightType fightType) {
		return 10;
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		int max = 23;
		int level = attacker.skills.getLevel(Skill.MAGIC);
		if (level - 75 > 0) {
			max += (level - 75) / 3;
		}
		return new CombatHit[] { nextMagicHit(attacker, defender, max, PROJECTILE) };
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

	public static TridentOfTheSeasStrategy get() {
		return INSTANCE;
	}

}
