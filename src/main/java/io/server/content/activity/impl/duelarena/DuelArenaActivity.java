package io.server.content.activity.impl.duelarena;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.content.consume.FoodData;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.game.world.entity.combat.strategy.player.special.CombatSpecial;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerOption;
import io.server.game.world.entity.mob.player.exchange.duel.StakeSession;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.ItemContainer;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.game.world.items.containers.equipment.EquipmentType;
import io.server.game.world.items.containers.pricechecker.PriceType;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendEntityHintArrow;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendPlayerOption;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

/**
 * The activity that handles players in the duel arena. This activity handles
 * moving players into the arena and handles giving the reward to the winning
 * player.
 *
 * @author nshusa
 * @author Daniel
 */
public class DuelArenaActivity extends Activity {

	private int count;
	private boolean started;
	private StakeSession session;
	private final DuelRules rules = new DuelRules();
	private int winnerId = -1;
	private final DuelArenaListener listener = new DuelArenaListener(this);
	private static final Position[] ARENA = { new Position(3345, 3251), new Position(3376, 3232),
			new Position(3345, 3213) };
	private static final Position[] OBSTACLE_ARENA = { new Position(3376, 3251), new Position(3345, 3231),
			new Position(3376, 3213) };

	private final List<Player> players = new ArrayList<>();

	private DuelArenaActivity() {
		super(2, Mob.DEFAULT_INSTANCE_HEIGHT);
	}

	public static DuelArenaActivity create(StakeSession duelSession) {
		DuelArenaActivity activity = new DuelArenaActivity();
		activity.session = duelSession;
		activity.setPause(true);
		return activity;
	}

	@Override
	public boolean canLogout(Player player) {
		player.send(new SendMessage("You cannot logout in the duel arena!"));
		return false;
	}

	@Override
	public boolean canDrinkPotions(Player player) {
		if (rules.contains(DuelRule.NO_DRINKS)) {
			player.send(new SendMessage("Potions have been disabled!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canUsePrayer(Player player) {
		if (rules.contains(DuelRule.NO_PRAYER)) {
			player.send(new SendMessage("Prayer has been disabled!"));
			return false;
		}

		return true;
	}

	@Override
	public boolean canEat(Player player, FoodData foodType) {
		if (rules.contains(DuelRule.NO_FOOD)) {
			player.send(new SendMessage("Food has been disabled!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canUseSpecial(Player player) {
		if (rules.contains(DuelRule.NO_SPECIAL_ATTACK)) {
			player.send(new SendMessage("Special attacks have been disabled!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canEquipItem(Player player, Item item, EquipmentType type) {
		if (type == null) {
			return false;
		}

		if (rules.contains(DuelRule.LOCK_ITEMS)) {
			player.send(new SendMessage("Item switching has been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.ONLY_WHIP_DDS)) {
			final String name = item.getName() == null ? "null" : item.getName().toLowerCase();
			if (!name.contains("dragon dagger") && !name.contains("abyssal whip") && !name.contains("abyssal tentacle")
					&& !name.contains("lime whip")) {
				player.send(new SendMessage("You can only use a whip or dragon dagger!"));//
				return false;
			}
		}

		if (rules.contains(DuelRule.ONLY_FUN_WEAPONS) && !DuelUtils.hasFunWeapon(player, item)) {
			player.send(new SendMessage("You can only use fun weapons!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_WEAPON) && type.getSlot() == Equipment.WEAPON_SLOT) {
			player.send(new SendMessage("Weapons have been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_SHIELD) && type.getSlot() == Equipment.SHIELD_SLOT) {
			player.send(new SendMessage("Shields have been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_AMMO) && type.getSlot() == Equipment.ARROWS_SLOT) {
			player.send(new SendMessage("Ammo has been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_HEAD) && type.getSlot() == Equipment.HEAD_SLOT) {
			player.send(new SendMessage("Helms have been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_CAPE) && type.getSlot() == Equipment.CAPE_SLOT) {
			player.send(new SendMessage("Capes have been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_NECKLACE) && type.getSlot() == Equipment.AMULET_SLOT) {
			player.send(new SendMessage("Amulets hae been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_BODY) && type.getSlot() == Equipment.CHEST_SLOT) {
			player.send(new SendMessage("Chest items have been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_LEGS) && type.getSlot() == Equipment.LEGS_SLOT) {
			player.send(new SendMessage("Leg items have been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_GLOVES) && type.getSlot() == Equipment.HANDS_SLOT) {
			player.send(new SendMessage("Gloves have been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_BOOTS) && type.getSlot() == Equipment.FEET_SLOT) {
			player.send(new SendMessage("Boots have been disabled!"));
			return false;
		}

		if (rules.contains(DuelRule.NO_RINGS) && type.getSlot() == Equipment.RING_SLOT) {
			player.send(new SendMessage("Rings have been disabled!"));
			return false;
		}

		return true;
	}

	public void addPlayer(Player player) {
		if (this.players.contains(player)) {
			return;
		}
		this.players.add(player);
		this.add(player);
	}

	@Override
	protected void start() {
		if (winnerId != -1) {
			finish();
			return;
		}

		if (count == 0) {
			initialize();
		} else if (count > 0 && count < 4) {
			players.forEach(it -> it.speak(Integer.toString(4 - count)));
		} else if (count == 4) {
			started = true;
			players.forEach(it -> it.speak("FIGHT!"));
			DuelArenaActivity duel = new DuelArenaActivity();
		}

		count++;
		resetCooldown();
	}

	@Override
	public void onLogout(Player player) {
		getOther(player).ifPresent(it -> winnerId = it.getListIndex());
	}

	@Override
	public void finish() {
		started = false;
		// move players
		players.forEach(it -> it.move(DuelConstants.DUEL_RESPAWN));

		final Optional<Player> winnerResult = getWinner();

		if (winnerResult.isPresent()) {
			final Player winner = winnerResult.get();

			final Optional<Player> loserResult = getOther(winner);

			if (loserResult.isPresent()) {
				final Player loser = loserResult.get();

				// remove entity arrow
				winner.send(new SendEntityHintArrow(loser, true));
				loser.send(new SendEntityHintArrow(winner, true));

				showReward(winner, loser, true);

				winner.send(new SendMessage("You are victorious!"));
				loser.send(new SendMessage("You lost!"));
			}

		}

		// reset player
		players.forEach(it -> {
			it.venged = false;
			CombatSpecial.restore(it, 100);
			it.unpoison();
			it.playerAssistant.reset();
			it.skills.restoreAll();
			it.send(new SendPlayerOption(PlayerOption.ATTACK, true, true));
			it.send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, false));
			remove(it);
		});

	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		switch (event.getObject().getId()) {
		case 3111:
		case 3113:
		case 3203:
			if (rules.contains(DuelRule.NO_FORFEIT)) {
				player.send(new SendMessage("Forfeiting has been disabled!"));
				return true;
			}

			if (count < 4) {
				player.send(new SendMessage("You can't forfeit yet!"));
				return true;
			}
			getOther(player).ifPresent(it -> winnerId = it.getListIndex());
			return true;

		}
		return false;
	}

	private void unequipItems(Player player) {
		if (rules.contains(DuelRule.NO_WEAPON) && player.equipment.hasWeapon()) {
			player.equipment.unequip(Equipment.WEAPON_SLOT);
		}

		if (rules.contains(DuelRule.NO_HEAD) && player.equipment.hasHead()) {
			player.equipment.unequip(Equipment.HEAD_SLOT);
		}

		if (rules.contains(DuelRule.NO_CAPE) && player.equipment.hasCape()) {
			player.equipment.unequip(Equipment.CAPE_SLOT);
		}

		if (rules.contains(DuelRule.NO_NECKLACE) && player.equipment.hasAmulet()) {
			player.equipment.unequip(Equipment.AMULET_SLOT);
		}

		if (rules.contains(DuelRule.NO_AMMO) && player.equipment.hasAmmo()) {
			player.equipment.unequip(Equipment.ARROWS_SLOT);
		}

		if (rules.contains(DuelRule.NO_BODY) && player.equipment.hasChest()) {
			player.equipment.unequip(Equipment.CHEST_SLOT);
		}

		if (rules.contains(DuelRule.NO_SHIELD)) {
			if (player.equipment.hasWeapon()) {
				Item weapon = player.equipment.getWeapon();
				if (weapon.isTwoHanded()) {
					player.equipment.unequip(Equipment.WEAPON_SLOT);
				}
			}

			if (player.equipment.hasShield()) {
				player.equipment.unequip(Equipment.SHIELD_SLOT);
			}
		}

		if (rules.contains(DuelRule.NO_LEGS) && player.equipment.hasLegs()) {
			player.equipment.unequip(Equipment.LEGS_SLOT);
		}

		if (rules.contains(DuelRule.NO_GLOVES) && player.equipment.hasHands()) {
			player.equipment.unequip(Equipment.HANDS_SLOT);
		}

		if (rules.contains(DuelRule.NO_BOOTS) && player.equipment.hasFeet()) {
			player.equipment.unequip(Equipment.FEET_SLOT);
		}

		if (rules.contains(DuelRule.NO_RINGS) && player.equipment.hasRing()) {
			player.equipment.unequip(Equipment.RING_SLOT);
		}

	}

	private void showReward(Player winner, Player loser, boolean giveReward) {
		Objects.requireNonNull(winner);
		Objects.requireNonNull(loser);

		final ItemContainer winnerContainer = session.item_containers.get(winner);
		Objects.requireNonNull(winnerContainer);

		final ItemContainer loserContainer = session.item_containers.get(loser);
		Objects.requireNonNull(loserContainer);

		long value = 0;

		final ItemContainer rewardContainer = new ItemContainer(28, ItemContainer.StackPolicy.STANDARD);

		rewardContainer.addAll(winnerContainer.toArray());
		value += winnerContainer.containerValue(PriceType.VALUE);

		// give reward
		if (giveReward) {
			rewardContainer.addAll(loserContainer.toArray());
			value += loserContainer.containerValue(PriceType.VALUE);

			Item[] reward = rewardContainer.toArray();
			winner.inventory.addOrDrop(reward);
			showReward(winner, loser, reward, value);
		} else {
			winner.inventory.addAll(winnerContainer);
			showReward(winner, loser, winnerContainer.toArray(), value);

			loser.inventory.addAll(loserContainer);
			showReward(loser, winner, loserContainer.toArray(), value);
		}

	}

	private void showReward(Player winner, Player loser, Item[] reward, long value) {
		winner.send(new SendString("<col=E1981F>Total Value: " + Utility.formatDigits(value), 31709));
		winner.send(new SendString(loser.getName(), 31706));
		winner.send(new SendString(loser.skills.getCombatLevel(), 31707));
		winner.send(new SendItemOnInterface(31708, reward));
		winner.send(new SendString("You are victorious!", 31705));
		winner.interfaceManager.open(31700, false);
	}

	@Override
	public void cleanup() {
		// dont put anything in here
	}

	@Override
	public boolean canTeleport(Player player) {
		player.send(new SendMessage("You cannot teleport in the duel arena!"));
		return false;
	}

	@Override
	public void onDeath(Mob mob) {
		if (winnerId == -1) {
			Optional<Player> result = players.stream().filter(it -> it.getListIndex() != mob.getListIndex())
					.findFirst();
			result.ifPresent(it -> winnerId = it.getListIndex());
		}
	}

	private void initialize() {
		final boolean obstacles = rules.contains(DuelRule.OBSTACLES);
		final boolean noMovement = rules.contains(DuelRule.NO_MOVEMENT);

		Player player = players.get(0);
		Player other = players.get(1);

		if (noMovement) {
			Position position = getPosition(obstacles, false, true);
			player.move(position);
			other.move(new Position(position.getX() - 1, position.getY(), 0));
		} else {
			player.move(getPosition(obstacles, false, false));
			other.move(getPosition(obstacles, true, false));
		}

		players.forEach(it -> {
			unequipItems(it);
			CombatSpecial.restore(it, 100);
			it.venged = false;
			it.unpoison();
			it.skulling.unskull();
			it.playerAssistant.reset();
			it.skills.restoreAll();
			it.interfaceManager.close();
		});

		player.send(new SendEntityHintArrow(other));
		other.send(new SendEntityHintArrow(player));
	}

	private static Position getPosition(boolean obstacles, boolean second, boolean noMovement) {
		if (noMovement) {
			Position p = ARENA[0];
			return new Position(p.getX() + (5 + Utility.random(5)) * (Utility.random(1) == 0 ? 1 : -1),
					p.getY() + Utility.random(6) * (Utility.random(1) == 0 ? 1 : -1));
		}
		if (obstacles) {
			Position p = OBSTACLE_ARENA[0];
			int x = p.getX() + (5 + Utility.random(5)) * (second ? 1 : -1);
			int y = p.getY() + Utility.random(5) * (Utility.random(1) == 0 ? 1 : -1);
			while (!TraversalMap.isTraversable(new Position(x, y, 0), Direction.NORTH, false)) {
				x = p.getX() + (5 + Utility.random(5)) * (second ? 1 : -1);
				y = p.getY() + Utility.random(5) * (Utility.random(1) == 0 ? 1 : -1);
			}
			return new Position(x, y);
		}
		Position p = ARENA[0];
		return new Position(p.getX() + (5 + Utility.random(5)) * (second ? 1 : -1),
				p.getY() + Utility.random(5) * (Utility.random(1) == 0 ? 1 : -1));
	}

	@Override
	public boolean canSpellCast(Player player) {
		if (rules.contains(DuelRule.NO_MAGIC)) {
			player.send(new SendMessage("You can not spell cast in this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public ActivityType getType() {
		return ActivityType.DUEL_ARENA;
	}

	@Override
	protected Optional<DuelArenaListener> getListener() {
		return Optional.of(listener);
	}

	public Optional<Player> getPlayer() {
		if (players.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(players.get(0));
	}

	public Optional<Player> getOther(Player player) {
		return players.stream().filter(it -> !it.equals(player)).findFirst();
	}

	public DuelRules getRules() {
		return rules;
	}

	public Optional<Player> getWinner() {
		if (winnerId <= 0) {
			return Optional.empty();
		}
		return players.stream().filter(it -> it.getListIndex() == winnerId).findFirst();
	}

	public boolean hasDuelStarted() {
		return started;
	}
}