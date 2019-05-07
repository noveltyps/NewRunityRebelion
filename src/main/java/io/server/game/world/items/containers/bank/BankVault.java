package io.server.game.world.items.containers.bank;

import static io.server.Config.CURRENCY;

import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;
import io.server.util.Utility;

/**
 * Handles the bank vault system.
 *
 * @author Daniel
 * @since 12-1-2017.
 */
public class BankVault {

	/** The player instance. */
	private final Player player;

	/** The bank vault container value. */
	public long container;

	/** Constructs a new <code>BankVault</code>. */
	public BankVault(Player player) {
		this.player = player;
	}

	/** Sends message to player about their container value. */
	public void value() {
		player.send(new SendMessage("You currently have " + Utility.formatDigits(container) + " coin"
				+ (container == 1 ? "" : "s") + " stored inside your bank vault.", MessageColor.DARK_RED));
		player.speak("<icon=15> I currently have " + Utility.formatDigits(container) + " coin"
				+ (container == 1 ? "" : "s") + " stored inside your bank vault.");
	}

	/** Checks if player's bank container contains a certain amount. */
	public boolean contains(int amount) {
		return container >= amount;
	}

	/** Removes a certain amount from the player's bank vault container. */
	public void remove(int amount) {
		container -= amount;
	}

	/**
	 * Adds an amount into the player's bank vault with no checks. Careful how you
	 * use this!
	 */
	public boolean add(long amount) {
		return add(amount, false);
	}

	/**
	 * Adds an amount into the player's bank vault with no checks. Careful how you
	 * use this!
	 */
	public boolean add(long amount, boolean message) {
		if (player.right == PlayerRight.ULTIMATE_IRONMAN) {
			player.inventory.addOrDrop(new Item(995, (int) amount));
			return true;
		}

		if (container + amount >= Long.MAX_VALUE) {
			return false;
		}
		container += amount;
		if (message) {
			player.send(new SendMessage(Utility.formatDigits(amount) + " coins have been added into your bank vault.",
					MessageColor.DARK_BLUE));
		}
		return true;
	}

	/** Deposits an an amount into the player's bank vaut. */
	public void deposit(int amount) {
		if ((long) amount + container >= Long.MAX_VALUE) {
			player.send(new SendMessage("The bankers don't want to handle that much money!", MessageColor.RED));
			return;
		}
		if (Long.MAX_VALUE == container) {
			player.send(new SendMessage("Your vault is currently full and can no longer hold any more coins!",
					MessageColor.RED));
			return;
		}

		int invAmount = player.inventory.computeAmountForId(CURRENCY);
		int bankAmount = 0;
		if (invAmount < amount) {
			bankAmount = player.bank.computeAmountForId(CURRENCY);
			if (invAmount + bankAmount <= 0) {
				player.send(new SendMessage("You don't have any coins you noob!", MessageColor.RED));
				return;
			}
		}
		if (invAmount > amount) {
			invAmount = amount;
		}
		if (bankAmount > amount - invAmount) {
			bankAmount = amount - invAmount;
		}
		amount = invAmount + bankAmount;
		if (invAmount > 0)
			player.inventory.remove(CURRENCY, invAmount);
		if (bankAmount > 0)
			player.bank.remove(CURRENCY, bankAmount);
		container += amount;
		player.bank.refresh();
		player.send(new SendMessage(
				"You now have " + Utility.formatDigits(container) + " coins stored inside your bank vault.",
				MessageColor.DARK_RED));
	}

	/** Withdraws a certain amount into the player's bank vault container. */
	public void withdraw(long amount) {
		if (amount < 1) {
			player.message("You can't withdraw zero coins you fool.");
			return;
		}
		if (container < 0) {
			World.sendMessage(
					player.getName() + " HAS POTENTIALLY FOUND AND ABUSED A DUPE. STAFF INVESTIGATE IMMEDIATELY.");
			World.sendMessage(
					player.getName() + " HAS POTENTIALLY FOUND AND ABUSED A DUPE. STAFF INVESTIGATE IMMEDIATELY.");
			World.sendMessage(
					player.getName() + " HAS POTENTIALLY FOUND AND ABUSED A DUPE. STAFF INVESTIGATE IMMEDIATELY.");
			World.sendMessage(
					player.getName() + " HAS POTENTIALLY FOUND AND ABUSED A DUPE. STAFF INVESTIGATE IMMEDIATELY.");
		}
		if (container == 0) {
			player.send(new SendMessage("Your bank vault is currently empty.", MessageColor.RED));
			return;
		}
		if (amount > container) {
			amount = container;
		}
		if (amount > Integer.MAX_VALUE) {
			player.send(new SendMessage("You can not hold that many coins!", MessageColor.RED));
			return;
		}

		int contain = player.inventory.computeAmountForId(CURRENCY);
		if (contain + amount > Integer.MAX_VALUE) {
			withdraw(Integer.MAX_VALUE - contain); // So it performs all the safety checks again..
			return;
		}

		if (player.inventory.add(new Item(CURRENCY, (int) amount))) {
			container -= amount;
			player.send(new SendMessage("You have successfully withdrawn " + Utility.formatDigits(amount) + " coin"
					+ (amount == 1 ? "" : "s") + "."));
			player.bank.refresh();
		}
	}
}
