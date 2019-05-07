package io.server.content.bot;

import java.util.concurrent.atomic.AtomicInteger;

import io.server.Config;
import io.server.content.bot.botclass.BotClass;
import io.server.content.bot.objective.BotObjective;
import io.server.content.consume.PotionData;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.teleport.TeleportTablet;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.world.World;
import io.server.game.world.entity.combat.strategy.player.special.CombatSpecial;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.util.Utility;

/**
 * The player bot entity.
 *
 * @author Daniel.
 */
public class PlayerBot extends Player {
	
	public static final AtomicInteger BOT_COUNT = new AtomicInteger(0);

	private int ticks;
	private Runnable action;
	private boolean loop;
	public int foodRemaining;
	public int statBoostersRemaining;
	public BotClass botClass;
	public Player opponent;

	public int consumableDelay;

	public PlayerBot() {
		this(BotUtility.nameGenerator());
	}

	public PlayerBot(String name) {
		super(name);
		setVisible(true);
		isBot = true;
		headIcon = -1;
		clanTag = "";
		clanTagColor = "";
		newPlayer = false;
		regionChange = true;
		positionChange = true;
		settings.acceptAid = false;
		settings.autoRetaliate = true;
		playerTitle = BotUtility.TITLE;
		appearance = BotUtility.APPEARANCE;
		spellbook = Spellbook.LUNAR;
		prayer.reset();
		mobAnimation.reset();
		playerAssistant.setPrayer();
		movement.setRunningToggled(true);
		skills.setCombatLevel();
		settings.lockExperience = true;
		CombatSpecial.restore(this, 100);
	}

	@Override
	public void register() {
		if (!isRegistered() && !World.getPlayers().contains(this)) {
			setRegistered(World.getPlayers().add(this));
			setPosition(Config.BOTSPAWN);
			// ClanChannelHandler.connect(getPlayer(), "help");
			// super(Config.LUMBRIDGE, false);
			BOT_COUNT.incrementAndGet();
		}
	}

	@Override
	public void unregister() {
		if (!isRegistered()) {
			return;
		}

		if (!World.getPlayers().contains(this)) {
			return;
		}

		World.cancelTask(this, true);
		World.getPlayers().remove((Player) destroy());
		BOT_COUNT.decrementAndGet();
	}

	@Override
	public void sequence() {
		super.sequence();

		if (ticks > 0) {
			ticks--;
		}

		if (ticks == 0 && action != null) {
			Runnable tmp = action;
			action.run();
			if (!loop && action == tmp)
				action = null;
		}

		if (opponent != null) {
			loopCombat();
		}
	}

	public void schedule(int ticks, Runnable action) {
		this.ticks = ticks;
		this.action = action;
		loop = false;
	}

	public void loop(int ticks, Runnable action) {
		this.ticks = ticks;
		this.action = action;
		loop = true;
	}

	public void pause(int ticks) {
		this.ticks += ticks;
	}

	public void stopLoop() {
		loop = false;
	}

	public void pot(Mob opponent, ItemClickEvent event, PotionData potion) {
		if (!potion.canDrink(this)) {
			return;
		}

		animate(new Animation(829, UpdatePriority.LOW));
		potionDelay.reset();

		Item replace = PotionData.getReplacementItem(event.getItem());

		if (replace.getId() == 229) {
			inventory.remove(event.getItem());
		} else {
			inventory.replace(event.getItem().getId(), replace.getId(), event.getSlot(), true);
		}

		potion.onEffect(this);
		getCombat().attack(opponent);
	}

	public void postDeath() {
		move(Config.DEFAULT_POSITION);
		skills.restoreAll();
		equipment.login(); 
		playerAssistant.reset();//do you not lsoe your items on death ? only in wildy 
		//little backstory, basically when i first worked on osroyale
		//there was a bug that would double your stats on death.
		//to fix it i added this
		//i added the login() method on equipment.java i don't think it accounts for maxhits for range and mage and melee
		setSpecialActivated(false);
		getCombat().getDamageCache().clear();
		CombatSpecial.restore(this, 100);
		movement.reset();
		teleblockTimer.set(0);
		schedule(2, () -> BotObjective.WALK_TO_BANK.init(this));
	}

	public void retaliate(Player defender) {
		if (!getCombat().isAttacking(defender)) {
			if (getCombat().isAttacking()) {
				endFight();
				return;
			}
			BotObjective.COMBAT.init(this);
		}
	}

	private void loopCombat() {
		if (opponent.isDead()) {
			schedule(4, this::endFight);
			return;
		}

		if (foodRemaining == 0 || skills.getLevel(Skill.PRAYER) == 0) {
			endFight();
			return;
		}

		if (isDead() || getCurrentHealth() <= 0) {
			speak(Utility.randomElement(BotUtility.DEATH_MESSAGES));
			schedule(7, () -> BotObjective.WALK_TO_BANK.init(this));
			opponent = null;
			getCombat().reset();
			return;
		}

		if (!getCombat().inCombat()
				|| (opponent.getCombat().isUnderAttack() && !opponent.getCombat().isUnderAttackBy(this))) {
			endFight();
			return;
		}

		getCombat().attack(opponent);

		if (consumableDelay > 0) {
			consumableDelay--;
		}

		if (consumableDelay == 0) {
			botClass.pot(opponent, this);
			botClass.eat(opponent, this);
		}

		botClass.handleCombat(opponent, this);
	}

	public void endFight() {
		unpoison();
		opponent = null;
		botClass.endFight(this);
		getCombat().reset();
		if (!isDead() && getCurrentHealth() > 0) {
			Teleportation.teleport(this, TeleportTablet.HOME_TABLET.getPosition(),
					Teleportation.TeleportationData.MODERN, () -> BotObjective.WALK_TO_BANK.init(this));
		}
	}

}
