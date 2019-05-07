package io.server.game.world.entity.combat.strategy.player.special.melee;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.net.packet.out.SendMessage;

/**
 * @author Michael | Chex
 */
public class BandosGodsword extends PlayerMeleeStrategy {

	// BGS(normal): 7642, BGS(OR): 7643
	private static final Animation ANIMATION = new Animation(7642, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1212);

	private static final BandosGodsword INSTANCE = new BandosGodsword();

	private BandosGodsword() {
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit h) {
		super.attack(attacker, defender, h);
		attacker.graphic(GRAPHIC);

		if (defender.isPlayer() && h.isAccurate()) {
			Player victim = defender.getPlayer();
			int damage = h.getDamage();
			int[] skillOrder = { Skill.DEFENCE, Skill.STRENGTH, Skill.ATTACK, Skill.PRAYER, Skill.MAGIC, Skill.RANGED };

			for (int s : skillOrder) {

				// Getting the skill value to decrease.
				int removeFromSkill;

				if (h.getDamage() > victim.skills.getLevel(s)) {
					int difference = damage - victim.skills.getLevel(s);
					removeFromSkill = damage - difference;
				} else
					removeFromSkill = damage;
				if (removeFromSkill <= 0) {
					return; // ADAM ADDED THIS INCASE IT CAUSES ISSUES
				}

				// Decreasing the skill.
				victim.skills.get(s).removeLevel(removeFromSkill);
				victim.skills.refresh(s);

				// Changing the damage left to decrease.
				damage -= removeFromSkill;
				String skill = Skill.getName(s);
				attacker.send(new SendMessage("You've drained " + victim.getUsername() + "'s " + skill + " level by "
						+ removeFromSkill + "."));
				victim.send(new SendMessage("Your " + skill + " level has been drained."));
			}
		}
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 4;
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return 2 * roll;
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int damage) {
		return (int) (damage * 1.21);
	}

	public static BandosGodsword get() {
		return INSTANCE;
	}

}
