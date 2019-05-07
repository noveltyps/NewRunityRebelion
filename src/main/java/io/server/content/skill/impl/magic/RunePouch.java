package io.server.content.skill.impl.magic;

import java.util.LinkedList;
import java.util.List;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.position.Area;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.util.Utility;

/**
 * Handles the rune pouch.
 *
 * @author Daniel
 */
public class RunePouch {

	/**
	 * The maximum amount of total runes the player can carry in their rune pouch.
	 */
	private static final int MAXIMUM_RUNE_CAPACITY = 16_000;

	/** Array of all runes allowed to be inside the rune pouch. */
	private final Item[] ALLOWED_RUNES = { new Item(554), new Item(555), new Item(556), new Item(557), new Item(558),
			new Item(559), new Item(560), new Item(561), new Item(562), new Item(563), new Item(564), new Item(565),
			new Item(566), new Item(9075) };

	/** The player instance. */
	private final Player player;

	/** The runes stores in the rune pouch; */
	public List<Item> runes = new LinkedList<>();

	/** Constructs a new <code>RunePouch</code>. */
	public RunePouch(Player player) {
		this.player = player;
	}

	public void open() {
		refresh();
		player.interfaceManager.open(41700);
	}

	public void clear() {
		runes.forEach(player.inventory::add);
		runes.clear();
		player.interfaceManager.open(41700);
		refresh();
	}

	public void refresh() {
		player.send(new SendItemOnInterface(41710, runes.toArray(new Item[runes.size()])));
		player.send(new SendItemOnInterface(41711, player.inventory.getItems()));
	}

	public void withdraw(int item, int amount) {
		for (Item rune : runes) {
			if (rune.getId() == item) {
				int current = player.inventory.computeAmountForId(item);
				if (rune.getAmount() - amount < 0)
					amount = rune.getAmount();
				player.inventory.add(item, amount);
				int newAm = player.inventory.computeAmountForId(item);
				if (newAm - current < amount)
					amount = newAm - current;
				rune.decrementAmountBy(amount);
				if (rune.getAmount() == 0)
					runes.remove(rune);
				refresh();
				return;
			}
		}
	}

	public void deposit(Item item, int amount) {
		boolean allowed = false;
		for (Item rune : ALLOWED_RUNES) {
			if (rune.getId() == item.getId()) {
				allowed = true;
				break;
			}
		}
		if (!allowed) {
			player.message("You can only deposit runes into the rune pouch!");
			return;
		}
		int runeAmount = getRuneAmount();
		if (runeAmount >= MAXIMUM_RUNE_CAPACITY) {
			player.message("You can only have a total of " + Utility.formatDigits(MAXIMUM_RUNE_CAPACITY)
					+ " runes in your rune pouch.");
			return;
		}
		if (amount > item.getAmount()) {
			amount = item.getAmount();
		}
		if (MAXIMUM_RUNE_CAPACITY - runeAmount < amount) {
			amount = MAXIMUM_RUNE_CAPACITY - runeAmount;
		}

		for (Item rune : runes) {
			if (item.getId() == rune.getId()) {
				player.inventory.remove(item.getId(), amount);
				rune.incrementAmountBy(amount);
				refresh();
				return;
			}
		}

		if (runes.size() >= 3) {
			player.message("Your rune pouch is currently full and can not hold any more runes!");
			return;
		}

		player.inventory.remove(item.getId(), amount);
		runes.add(new Item(item.getId(), amount));
		refresh();
	}

	public int getRuneAmount() {
		int amount = 0;
		for (Item rune : runes) {
			amount += rune.getAmount();
		}
		return amount;
	}

	public int getRuneAmount(int id) {
		int amount = 0;
		for (Item rune : runes) {
			if (rune.getId() == id)
				amount += rune.getAmount();
		}
		return amount;
	}

	public boolean contains(Item item) {
		for (Item rune : runes) {
			if (rune.getId() == item.getId() && rune.getAmount() >= item.getAmount())
				return true;
		}
		return false;
	}

	public boolean containsId(int item) {
		for (Item rune : runes) {
			if (rune.getId() == item)
				return true;
		}
		return false;
	}

	public void remove(Item item) {
		for (Item rune : runes) {
			if (rune.equalIds(item)) {
				rune.decrementAmountBy(item.getAmount());
				if (rune.getAmount() == 0)
					runes.remove(rune);
				return;
			}
		}
	}

	public boolean death(Item item) {
		if (item.getId() == 12971 && Area.inWilderness(player)) {
			runes.clear();
			return true;
		}

		return false;
	}
}
