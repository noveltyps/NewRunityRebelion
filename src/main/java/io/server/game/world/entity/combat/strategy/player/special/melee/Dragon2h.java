package io.server.game.world.entity.combat.strategy.player.special.melee;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;

/** @author Daniel | Obey */
public class Dragon2h extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(3157, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(559, UpdatePriority.HIGH);
	private static final Dragon2h INSTANCE = new Dragon2h();

	private Dragon2h() {
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		attacker.getCombatSpecial().drain(attacker);
		attacker.animate(getAttackAnimation(attacker, defender));

		List<Hit> extra = new LinkedList<>();
		CombatUtil.areaAction(attacker, 11, 1, other -> hitEvent(attacker, defender, other, extra));

		if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
			extra.addAll(Arrays.asList(hits));
			addCombatExperience(attacker, extra.toArray(new Hit[extra.size()]));
		}

		attacker.graphic(GRAPHIC);
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	public static Dragon2h get() {
		return INSTANCE;
	}

	private void hitEvent(Player attacker, Mob defender, Mob other, List<Hit> extra) {
		if (!CombatUtil.canBasicAttack(attacker, other)) {
			return;
		}

		if (attacker.equals(other) || defender.equals(other)) {
			return;
		}

		CombatHit hit = nextMeleeHit(attacker, defender);
		attacker.getCombat().submitHits(other, hit);
		if (extra != null)
			extra.add(hit);
	}

}