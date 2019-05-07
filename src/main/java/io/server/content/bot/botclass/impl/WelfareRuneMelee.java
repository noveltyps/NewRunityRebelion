package io.server.content.bot.botclass.impl;

import static io.server.game.world.entity.combat.attack.FormulaFactory.getMaxHit;

import java.util.concurrent.TimeUnit;

import io.server.content.bot.PlayerBot;
import io.server.content.bot.botclass.BotClass;
import io.server.content.consume.FoodData;
import io.server.content.consume.PotionData;
import io.server.content.skill.impl.magic.spell.impl.Vengeance;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.util.RandomUtils;
import plugin.click.item.EatFoodPlugin;

public class WelfareRuneMelee extends SimplifiedListener<Player> implements BotClass {

	@Override
	public Item[] inventory() {
		return new Item[] { new Item(12695), new Item(3024), new Item(3024), new Item(391), new Item(391),
				new Item(391), new Item(391), new Item(391), new Item(391), new Item(391), new Item(391), new Item(391),
				new Item(391), new Item(391), new Item(391), new Item(391), new Item(3144), new Item(3144),
				new Item(391), new Item(391), new Item(3144), new Item(3144), new Item(391), new Item(391),
				new Item(5698), new Item(557, 500), new Item(560, 500), new Item(9075, 500) };
	}

	@Override
	public Item[] equipment() {
		return new Item[] { new Item(10828), new Item(1127), new Item(1079), new Item(3105), new Item(4587),
				new Item(12954), new Item(1725), new Item(2550), new Item(7462), new Item(6570) };
	}

	@Override
	public int[] skills() {
		return new int[] { 99, 99, 99, 99, 99, 99, 99 };
	}

	@Override
	public void initCombat(Player target, PlayerBot bot) {
		pot(target, bot);
		bot.prayer.toggle(Prayer.PROTECT_ITEM, Prayer.PIETY);
		bot.getCombat().addListener(this);
		bot.spellCasting.cast(new Vengeance(), null);
		bot.getCombat().setFightType(FightType.SCIMITAR_SLASH);
	}

	@Override
	public void handleCombat(Player target, PlayerBot bot) {
		if (!bot.prayer.isActive(Prayer.SMITE) && target.prayer.isActive(Prayer.SMITE)) {
			bot.prayer.toggle(Prayer.SMITE);
			bot.speak("Let's smite then...");
		} else if (bot.prayer.isActive(Prayer.SMITE) && !target.prayer.isActive(Prayer.SMITE)) {
			bot.prayer.toggle(Prayer.SMITE);
		}

		if (bot.isSpecialActivated() && target.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
			bot.speak("That's such bullshit...stop praying and maybe il fight back");
			bot.getCombatSpecial().disable(bot, false);
			bot.endFight();
		}

		if (!bot.prayer.isActive(Prayer.PROTECT_FROM_MELEE) && target.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
			bot.prayer.toggle(Prayer.PROTECT_FROM_MELEE);
		} else if (bot.prayer.isActive(Prayer.PROTECT_FROM_MELEE)
				&& !target.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
			bot.prayer.toggle(Prayer.PROTECT_FROM_MELEE);
		}

		if (bot.spellCasting.vengeanceDelay.elapsedTime(TimeUnit.SECONDS) >= 30) {
			bot.spellCasting.cast(new Vengeance(), null);
		}
	}

	@Override
	public void endFight(PlayerBot bot) {
		bot.prayer.deactivate(Prayer.PROTECT_ITEM, Prayer.SMITE, Prayer.PIETY);
	}

	@Override
	public void hit(Player attacker, Mob defender, Hit hit) {
		int max = getMaxHit(attacker, defender, CombatType.MELEE);
		max = attacker.getCombat().modifyDamage(defender, max);

		boolean hasSpec = attacker.getSpecialPercentage().intValue() >= 25;
		boolean lowHp = defender.getCurrentHealth() <= defender.getMaximumHealth() * RandomUtils.inclusive(0.30, 0.35);
		boolean combo = defender.getCurrentHealth() <= defender.getMaximumHealth() * RandomUtils.inclusive(0.50, 0.60)
				&& hit.getDamage() >= max * RandomUtils.inclusive(0.65, 0.70);

		if (!hasSpec || (!combo && !lowHp))
			return;

		PlayerBot bot = ((PlayerBot) attacker);
		bot.schedule(4, () -> {
			if (bot.equipment.getWeapon().matchesId(4587)) {
				int index = bot.inventory.computeIndexForId(5698);
				bot.equipment.equip(index);
				bot.getCombat().setFightType(FightType.DRAGON_DAGGER_LUNGE);
			}
			bot.getCombatSpecial().enable(bot);
			bot.schedule(4, () -> {
				if (!bot.isSpecialActivated() && bot.equipment.getWeapon().matchesId(5698)) {
					int idx = bot.inventory.computeIndexForId(4587);
					bot.equipment.equip(idx);
					bot.getCombat().setFightType(FightType.SCIMITAR_SLASH);
				}
			});
		});
	}

	@Override
	public void pot(Player target, PlayerBot bot) {
		if (target.getCurrentHealth() <= Math.floor(target.getMaximumHealth() * 0.10))
			return;

		if (!bot.potionDelay.elapsed(1250)) {
			return;
		}

		PotionData potion;
		ItemClickEvent event;

		if (checkSkill(bot, Skill.PRAYER, 40)) {
			int index = bot.inventory.computeIndexForId(3024);
			if (index >= 0) {
				event = new ItemClickEvent(0, bot.inventory.get(index), index);
				potion = PotionData.SUPER_RESTORE_POTIONS;
				bot.pot(target, event, potion);
			}
		} else if (checkSkill(bot, Skill.ATTACK, 115) || checkSkill(bot, Skill.STRENGTH, 115)
				|| checkSkill(bot, Skill.DEFENCE, 115)) {
			int index = bot.inventory.computeIndexForId(12695);
			if (index >= 0) {
				event = new ItemClickEvent(0, bot.inventory.get(index), index);
				potion = PotionData.SUPER_COMBAT_POTION;
				bot.pot(target, event, potion);
			}
		}
	}

	@Override
	public void eat(Player target, PlayerBot bot) {
		int max = target.playerAssistant.getMaxHit(bot, target.getStrategy().getCombatType());
		if (bot.getCurrentHealth() > bot.getMaximumHealth() * 0.45 && max < bot.getCurrentHealth())
			return;

		if (target.getCurrentHealth() <= Math.floor(target.getMaximumHealth() * 0.10))
			return;

		int index = bot.inventory.computeIndexForId(391);
		if (index >= 0) {
			EatFoodPlugin.eat(bot, bot.inventory.get(index), index, FoodData.MANTA);
			bot.foodRemaining--;
		}

		if (bot.getCurrentHealth() >= bot.getMaximumHealth() * 0.10)
			return;

		index = bot.inventory.computeIndexForId(3144);
		if (index >= 0) {
			EatFoodPlugin.eat(bot, bot.inventory.get(index), index, FoodData.COOKED_KARAMBWAN);
			bot.foodRemaining--;
		}
	}

	@Override
	public boolean canOtherAttack(Mob attacker, Player defender) {
		if (defender.getCombat().isAttacking() && !defender.getCombat().isAttacking(attacker)) {
			attacker.getPlayer().message("You cannot attack a bot while they are attacking another player.");
			return false;
		}
		return true;
	}

	@Override
	public void block(Mob attacker, Player defender, Hit hit, CombatType combatType) {
		((PlayerBot) defender).consumableDelay = RandomUtils.inclusive(1, 3);
	}

	private boolean checkSkill(PlayerBot bot, int id, int minimum) {
		return bot.skills.getLevel(id) < minimum;
	}

}
