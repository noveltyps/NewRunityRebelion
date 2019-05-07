package io.server.game.world.entity.combat;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.server.Config;
import io.server.content.bot.PlayerBot;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.combat.attack.FightStyle;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.attack.listener.CombatListener;
import io.server.game.world.entity.combat.formula.CombatFormula;
import io.server.game.world.entity.combat.hit.CombatData;
import io.server.game.world.entity.combat.hit.CombatHit;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.net.packet.out.SendEntityFeed;
import io.server.net.packet.out.SendMessage;
import io.server.util.Stopwatch;
import io.server.util.Utility;

public class Combat<T extends Mob> {
	private final T attacker;

	private CombatTarget target;
	private Mob lastAggressor, lastVictim;

	private final Stopwatch lastAttacked = Stopwatch.start();
	public final Stopwatch lastBlocked = Stopwatch.start();

	private FightType fightType;
	private FightStyle fightStyle;

	public final CombatFormula<T> formula = new CombatFormula<>();
	private final List<CombatListener<? super T>> listeners = new LinkedList<>();
	private final Deque<CombatListener<? super T>> pendingAddition = new LinkedList<>();
	private final Deque<CombatListener<? super T>> pendingRemoval = new LinkedList<>();

	private final CombatDamage damageCache = new CombatDamage();
	private final Deque<CombatData<T>> combatQueue = new LinkedList<>();
	private final Deque<Hit> damageQueue = new LinkedList<>();

	private final int[] hitsplatCooldowns = new int[4];
	private int cooldown;

	public Combat(T attacker) {
		this.attacker = attacker;
		this.target = new CombatTarget(attacker);
		fightType = FightType.UNARMED_PUNCH;
	}

	public boolean attack(Mob defender) {
		if (!Area.inDuelArena(attacker)) {
			if (attacker.isPlayer()) {
				if (defender.isPlayer()) {
					if (attacker.getPlayer().equipment.containsAny(Config.CUSTOM_ITEMS)
							|| attacker.getPlayer().inventory.containsAny(Config.CUSTOM_ITEMS)) {
						attacker.getPlayer().send(new SendMessage("You can't attack anyone with customs."));
						return false;
					}
				}
			} else if (defender.isPlayer()) {
				if (attacker.isPlayer()) {
					if (defender.getPlayer().equipment.containsAny(Config.CUSTOM_ITEMS)
							|| defender.getPlayer().inventory.containsAny(Config.CUSTOM_ITEMS)) {
						attacker.getPlayer().send(new SendMessage("You can't attack this player at the moment!"));
						return false;
					}
				}
			}
		}

		if (attacker.isPlayer()) {
			Player player = attacker.getPlayer();
			if (!player.interfaceManager.isMainClear() || !player.interfaceManager.isDialogueClear()) {
				player.interfaceManager.close();
			}
		}

		attacker.face(defender.getPosition());
		if (!canAttack(defender, attacker.getStrategy())) {
			attacker.movement.reset();
			target.resetTarget();
			return false;
		}

		target.setTarget(defender);
		attacker.attack(defender);
		return true;
	}

	public void tick() {
		if (!checkDistances(target.getTarget()))
			reset();

		updateListeners();

		if (cooldown > 0) {
			cooldown--;
		}

		if (cooldown == 0 && target.getTarget() != null) {
			CombatStrategy<? super T> strategy = attacker.getStrategy();
			attacker.interact(target.getTarget());
			submitStrategy(target.getTarget(), strategy);
		}

		while (!combatQueue.isEmpty()) {
			CombatData<T> data = combatQueue.poll();
			World.schedule(hitTask(data));
		}

		for (int index = 0, sent = 0; index < hitsplatCooldowns.length; index++) {
			if (hitsplatCooldowns[index] > 0) {
				hitsplatCooldowns[index]--;
			} else if (sent < 2 && sendNextHitsplat()) {
				hitsplatCooldowns[index] = 2;
				sent++;
			}
		}

		if (target.getTarget() != null && isAttacking(target.getTarget()) && attacker.isPlayer()) {
			attacker.getPlayer().send(new SendEntityFeed(target.getTarget().getName(),
					target.getTarget().getCurrentHealth(), target.getTarget().getMaximumHealth()));
		}
	}

	private boolean checkDistances(Mob defender) {
		return defender != null && Utility.withinDistance(attacker, defender, Region.VIEW_DISTANCE);
	}

	private boolean sendNextHitsplat() {
		if (damageQueue.isEmpty()) {
			return false;
		}

		if (attacker.getCurrentHealth() <= 0) {
			damageQueue.clear();
			return false;
		}

		Hit hit = damageQueue.poll();
		attacker.writeDamage(hit);
		return true;
	}

	private void updateListeners() {
		if (!pendingAddition.isEmpty()) {
			for (Iterator<CombatListener<? super T>> iterator = pendingAddition.iterator(); iterator.hasNext();) {
				CombatListener<? super T> next = iterator.next();
				addModifier(next);
				listeners.add(next);
				iterator.remove();
			}
		}

		if (!pendingRemoval.isEmpty()) {
			for (Iterator<CombatListener<? super T>> iterator = pendingRemoval.iterator(); iterator.hasNext();) {
				CombatListener<? super T> next = iterator.next();
				removeModifier(next);
				listeners.remove(next);
				iterator.remove();
			}
		}
	}

	public void submitStrategy(Mob defender, CombatStrategy<? super T> strategy) {
		if (!canAttack(defender, strategy)) {
			return;
		}

		if (!checkWithin(attacker, defender, strategy)) {
			return;
		}

		formula.add(strategy);
		init(defender, strategy);
		cooldown(strategy.getAttackDelay(attacker, defender, fightType));
		submitHits(defender, strategy, strategy.getHits(attacker, defender));
		formula.remove(strategy);

		if (attacker.isPlayer() && defender.isPlayer()) {
			attacker.getPlayer().skulling.checkForSkulling(defender.getPlayer());
		}
	}

	public void submitHits(Mob defender, CombatHit... hits) {
		CombatStrategy<? super T> strategy = attacker.getStrategy();
		addModifier(strategy);
		init(defender, strategy);
		submitHits(defender, strategy, hits);
		removeModifier(strategy);
	}

	private void submitHits(Mob defender, CombatStrategy<? super T> strategy, CombatHit... hits) {
		if (hits == null)
			return;
		start(defender, strategy, hits);
		for (int index = 0; index < hits.length; index++) {
			if (hits[index] == null)
				continue;
			boolean last = index == hits.length - 1;
			CombatHit hit = hits[index];
			CombatData<T> data = new CombatData<>(attacker, defender, hit, strategy, last);
			attack(defender, hit, strategy);
			combatQueue.add(data);
		}
	}

	public void queueDamage(Hit hit) {
		if (attacker.getCurrentHealth() <= 0) {
			return;
		}

		if (damageQueue.size() > 10) {
			attacker.decrementHealth(hit);
			return;
		}

		damageQueue.add(hit);
	}

	public void cooldown(int delay) {
		if (cooldown < delay)
			cooldown = delay;
	}

	public void setCooldown(int delay) {
		cooldown = delay;
	}

	private boolean canAttack(Mob defender, CombatStrategy<? super T> strategy) {
		if (!CombatUtil.validateMobs(attacker, defender)) {
			return false;
		}
		if (!CombatUtil.canAttack(attacker, defender)) {
			return false;
		}
		if (!strategy.canAttack(attacker, defender)) {
			return false;
		}
		for (CombatListener<? super T> listener : listeners) {
			if (!listener.canAttack(attacker, defender)) {
				return false;
			}
		}
		return defender.getCombat().canOtherAttack(attacker);
	}

	private boolean canOtherAttack(Mob attacker) {
		T defender = this.attacker;
		for (CombatListener<? super T> listener : listeners) {
			if (!listener.canOtherAttack(attacker, defender)) {
				return false;
			}
		}
		return defender.getStrategy().canOtherAttack(attacker, defender);
	}

	private void init(Mob defender, CombatStrategy<? super T> strategy) {
		strategy.init(attacker, defender);
		listeners.forEach(listener -> listener.init(attacker, defender));
	}

	private void start(Mob defender, CombatStrategy<? super T> strategy, Hit... hits) {
		if (!CombatUtil.validateMobs(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			if (defender.inTeleport)
				defender.getCombat().damageQueue.clear();
			reset();
			return;
		}

		defender.getCombat().lastBlocked.reset();
		defender.getCombat().lastAggressor = attacker;
		strategy.start(attacker, defender, hits);
		listeners.forEach(listener -> listener.start(attacker, defender, hits));
	}

	private void attack(Mob defender, Hit hit, CombatStrategy<? super T> strategy) {
		if (!CombatUtil.validateMobs(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			if (defender.inTeleport)
				defender.getCombat().damageQueue.clear();
			reset();
			return;
		}

		lastVictim = defender;
		lastAttacked.reset();
		attacker.action.reset();

		strategy.attack(attacker, defender, hit);
		listeners.forEach(listener -> listener.attack(attacker, defender, hit));
	}

	private void block(Mob attacker, Hit hit, CombatType combatType) {
		T defender = this.attacker;
		lastBlocked.reset();
		lastAggressor = attacker;
		defender.action.reset();
		listeners.forEach(listener -> listener.block(attacker, defender, hit, combatType));
		defender.getStrategy().block(attacker, defender, hit, combatType);
	}

	private void hit(Mob defender, Hit hit, CombatStrategy<? super T> strategy) {
		if (!CombatUtil.validateMobs(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			if (defender.inTeleport)
				defender.getCombat().damageQueue.clear();
			reset();
			return;
		}

		strategy.hit(attacker, defender, hit);
		if (attacker.isPlayer()) {
			if (attacker.inActivity())
				attacker.activityDamage += hit.getDamage();
		}
		listeners.forEach(listener -> listener.hit(attacker, defender, hit));

		if (!strategy.getCombatType().equals(CombatType.MAGIC)) {
			defender.animate(CombatUtil.getBlockAnimation(defender));
		}

		if (defender.getCurrentHealth() - hit.getDamage() > 0) {
			defender.getCombat().block(attacker, hit, strategy.getCombatType());
		}
	}

	private void hitsplat(Mob defender, Hit hit, CombatStrategy<? super T> strategy) {
		if (!CombatUtil.validateMobs(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			if (defender.inTeleport)
				defender.getCombat().damageQueue.clear();
			reset();
			return;
		}

		strategy.hitsplat(attacker, defender, hit);
		listeners.forEach(listener -> listener.hitsplat(attacker, defender, hit));

		if (!strategy.getCombatType().equals(CombatType.MAGIC) || hit.isAccurate()) {
			if (defender.getCurrentHealth() > 0 && hit.getDamage() >= 0) {
				defender.getCombat().queueDamage(hit);
				defender.getCombat().damageCache.add(attacker, hit);
			}
		}
	}

	private void retaliate(Mob attacker) {
		T defender = this.attacker;

		if (attacker.isPlayer()) {
			Player player = attacker.getPlayer();

			if (!player.interfaceManager.isMainClear() || !player.interfaceManager.isDialogueClear()) {
				player.interfaceManager.close();
			}

			if (defender.isPlayer() && defender.getPlayer().isBot) {
				((PlayerBot) defender.getPlayer()).retaliate(player);
			}
		}

		if (target.getTarget() != null && !target.isTarget(attacker)) {
			return;
		}

		if (defender.isPlayer() && defender.movement.isMoving())
			return;

		if (!CombatUtil.canAttack(defender, attacker)) {
			return;
		}

		if (defender.isAutoRetaliate() && !defender.movement.isMoving()) {
			attack(attacker);
		}
	}

	public void preDeath(Mob attacker, Hit hit) {
		if (attacker != null) {
			T defender = this.attacker;
			defender.getStrategy().preDeath(attacker, defender, hit);
			listeners.forEach(listener -> listener.preDeath(attacker, defender, hit));
			reset();
		}
	}

	public void preKill(Mob defender, Hit hit) {
		if (attacker != null) {
			listeners.forEach(listener -> listener.preKill(attacker, defender, hit));
		}
	}

	public void onKill(Mob defender, Hit hit) {
		if (attacker != null) {
			listeners.forEach(listener -> listener.onKill(attacker, defender, hit));
		}
	}

	public void onDeath(Mob attacker, Hit hit) {
		if (attacker != null) {
			T defender = this.attacker;
			listeners.forEach(listener -> listener.onDeath(attacker, defender, hit));
			defender.movement.reset();
			defender.resetWaypoint();
			reset();
		}
	}

	private void finishIncoming(Mob attacker) {
		T defender = this.attacker;
		defender.getStrategy().finishIncoming(attacker, defender);
		listeners.forEach(listener -> listener.finishIncoming(attacker, defender));
		defender.getCombat().retaliate(attacker);
	}

	private void finishOutgoing(Mob defender, CombatStrategy<? super T> strategy) {
		strategy.finishOutgoing(attacker, defender);
		listeners.forEach(listener -> listener.finishOutgoing(attacker, defender));
		defender.getCombat().finishIncoming(attacker);
	}

	public void reset() {
		if (target.getTarget() != null) {
			target.resetTarget();
			attacker.resetFace();
			attacker.movement.reset();
			attacker.resetWaypoint();
		}
	}

	public void addModifier(FormulaModifier<? super T> modifier) {
		formula.add(modifier);
	}

	public void removeModifier(FormulaModifier<? super T> modifier) {
		formula.remove(modifier);
	}

	public void addFirst(FormulaModifier<? super T> modifier) {
		formula.addFirst(modifier);
	}

	public void removeFirst() {
		formula.removeFirst();
	}

	public void addListener(CombatListener<? super T> listener) {
		if (listeners.contains(listener) || pendingAddition.contains(listener)) {
			return;
		}
//        System.out.println("[@Combat] added listener: " + listener.getClass().getSimpleName());
		pendingAddition.add(listener);
	}

	public void removeListener(CombatListener<? super T> listener) {
		if (!listeners.contains(listener) || pendingRemoval.contains(listener)) {
			return;
		}
//        System.out.println("[@Combat] removed listener: " + listener.getClass().getSimpleName());
		pendingRemoval.add(listener);
	}

	public void clearIncoming() {
		combatQueue.clear();
	}

	public boolean inCombat() {
		return isAttacking() || isUnderAttack();
	}

	public boolean inCombatWith(Mob mob) {
		return isAttacking(mob) || isUnderAttackBy(mob);
	}

	public boolean isAttacking() {
		return target.getTarget() != null && !hasPassed(lastAttacked, CombatConstants.COMBAT_TIMER_COOLDOWN);
	}

	public boolean isUnderAttack() {
		return lastAggressor != null && !hasPassed(lastBlocked, CombatConstants.COMBAT_TIMER_COOLDOWN);
	}

	public boolean isAttacking(Mob defender) {
		return defender != null && defender.equals(lastVictim)
				&& !hasPassed(lastAttacked, CombatConstants.COMBAT_TIMER_COOLDOWN);
	}

	public boolean isUnderAttackBy(Mob attacker) {
		return attacker != null && attacker.equals(lastAggressor)
				&& !hasPassed(lastBlocked, CombatConstants.COMBAT_TIMER_COOLDOWN);
	}

	public int getCooldown() {
		return cooldown;
	}

	public FightType getFightType() {
		
		return fightType;
	}

	public void setFightType(FightType type) {
		this.fightType = type;
		this.fightStyle = type.getStyle();
	}

	public FightStyle getFightStyle() {
		return fightStyle;
	}

	public Mob getDefender() {
		return target.getTarget();
	}

	public CombatDamage getDamageCache() {
		return damageCache;
	}

	public void compare(Mob mob) {
		target.compare(mob);
	}

	public void checkAggression(int level, Position spawn) {
		target.checkAggression(level, spawn);
	}

	public boolean isLastAggressor(Mob mob) {
		return mob.equals(lastAggressor);
	}

	public Mob getLastVictim() {
		return lastVictim;
	}

	public Mob getLastAggressor() {
		return lastAggressor;
	}

	private Task hitTask(CombatData<T> data) {
		return new Task(data.getHitDelay()) {
			@Override
			public void execute() {
				hit(data.getDefender(), data.getHit(), data.getStrategy());
				World.schedule(hitsplatTask(data));
				cancel();
			}
		};
	}

	private Task hitsplatTask(CombatData<T> data) {
		return new Task(data.getHitsplatDelay()) {
			@Override
			public void execute() {
				hitsplat(data.getDefender(), data.getHit(), data.getStrategy());
				if (data.isLastHit())
					finishOutgoing(data.getDefender(), data.getStrategy());
				cancel();
			}
		};
	}

	private boolean hasPassed(Stopwatch timer, int delay) {
		return timer.elapsed(delay, TimeUnit.MILLISECONDS);
	}

	public boolean hasPassed(int delay) {
		return elapsedTime() - delay >= 0;
	}

	public long elapsedTime() {
		long attacked = lastAttacked.elapsedTime();
		long blocked = lastBlocked.elapsedTime();
		return Math.max(attacked, blocked);
	}

	public void resetTimers(int millis) {
		lastAttacked.reset(millis, TimeUnit.MILLISECONDS);
		lastBlocked.reset(millis, TimeUnit.MILLISECONDS);
	}

	private boolean checkWithin(T attacker, Mob defender, CombatStrategy<? super T> strategy) {
		if (strategy == null || Utility.inside(attacker, defender)) {
			return false;
		}
		if (!strategy.withinDistance(attacker, defender)) {
			return false;
		}
		for (CombatListener<? super T> listener : listeners) {
			if (!listener.withinDistance(attacker, defender))
				return false;
		}
		return true;
	}

	public int modifyAttackLevel(Mob defender, int level) {
		return formula.modifyAttackLevel(attacker, defender, level);
	}

	public int modifyStrengthLevel(Mob defender, int level) {
		return formula.modifyStrengthLevel(attacker, defender, level);
	}

	public int modifyDefenceLevel(Mob attacker, int level) {
		return formula.modifyDefenceLevel(attacker, this.attacker, level);
	}

	public int modifyRangedLevel(Mob defender, int level) {
		return formula.modifyRangedLevel(attacker, defender, level);
	}

	public int modifyMagicLevel(Mob defender, int level) {
		return formula.modifyMagicLevel(attacker, defender, level);
	}

	public int modifyAccuracy(Mob defender, int roll) {
		return formula.modifyAccuracy(attacker, defender, roll);
	}

	public int modifyDefensive(Mob attacker, int roll) {
		return formula.modifyDefensive(attacker, this.attacker, roll);
	}

	public int modifyDamage(Mob defender, int damage) {
		return formula.modifyDamage(attacker, defender, damage);
	}

	public int modifyOffensiveBonus(Mob defender, int bonus) {
		return formula.modifyOffensiveBonus(attacker, defender, bonus);
	}

	public int modifyAggresiveBonus(Mob defender, int bonus) {
		return formula.modifyAggressiveBonus(attacker, defender, bonus);
	}

	public int modifyDefensiveBonus(Mob attacker, int bonus) {
		return formula.modifyDefensiveBonus(attacker, this.attacker, bonus);
	}

}
