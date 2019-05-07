package io.server.content.masterminer;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class MobData {
	private String name;
	private long price;
	private long GPS;
	private int level;
	private long baseGPS;

	private final static double EXPONENT = 1.02;
	private long amountItCosted;
	private long newPrice;

	public MobData(String name, long price, long GPS) {
		this.name = name;
		this.price = price;
		this.GPS = GPS;
		this.baseGPS = GPS;
		level = 0;
	}

	public MobData(String name, long price, long GPS, int level, long baseGPS) {
		this.name = name;
		this.price = price;
		this.GPS = GPS;
		this.level = level;
		this.baseGPS = baseGPS;
	}

	public MobData(String asString) {
		String parsed[] = asString.split(">");
		this.name = parsed[0];
		this.price = Long.parseLong(parsed[1]);
		this.GPS = Long.parseLong(parsed[2]);
		this.level = Integer.parseInt(parsed[3]);
		this.baseGPS = Long.parseLong(parsed[4]);
	}

	public void buy(Player player) {
		player.dialogueFactory.clear();
		int amount = player.masterMinerData.amountToBuy;

		if (amount == -1) // Max
		{
			System.out.println("Buying max " + name);
			openMaxConfirmationChat(player);
		} else {
			long finalPrice = totalPrice(amount);
			if (finalPrice < 0) {
				player.dialogueFactory.sendStatement("Sorry, this action would cost way too much money!").execute();
				return;
			}
			System.out.println("Total gold: " + player.masterMinerData.totalGold + "Final price: " + finalPrice);
			if (player.masterMinerData.totalGold >= finalPrice) {
				openConfirmationChat(player, finalPrice, amount);
			} else {
				player.dialogueFactory.sendStatement(
						"Sorry, this action would cost " + Util.toNiceString(finalPrice) + " gold at your level.")
						.execute();
				player.send(new SendMessage("You cannot afford this!"));
			}
		}
	}

	private void openConfirmationChat(Player player, long finalPrice, int amount) {
		player.dialogueFactory.sendStatement("Are you sure you wish to purchase " + amount + " " + name + "s?",
				"This action will cost you " + Util.toNiceString(finalPrice) + " gold.",
				name + "s" + " give " + GPS + " gold per second each.").sendOption("Yes!", () -> {
					player.masterMinerData.totalGold -= finalPrice;
					level += amount;
					player.masterMiner.updateGoldPerSecond();
					this.price = this.newPrice;
					player.masterMiner.updateMobDetails();

					player.send(new SendMessage("Successfully purchased " + amount + " " + name + "s."));
				}, "No, I was just checking!", () -> {
					// Do nothing
				}).execute();
	}

	private void openMaxConfirmationChat(Player player) {
		player.dialogueFactory.sendStatement("Are you sure you wish to purchase as many " + name + "s as you can?",
				name + "s" + " give " + GPS + " gold per second each.").sendOption("Yes!", () -> {
					int bought = maxPurchase(player.masterMinerData.totalGold);
					level += bought;
					player.masterMinerData.totalGold -= amountItCosted;
					player.masterMiner.updateGoldPerSecond();
					this.price = this.newPrice;
					player.masterMiner.updateMobDetails();

					if (bought == 0) {
						player.send(new SendMessage("Sorry, you couldn't afford any."));
					} else {
						player.send(new SendMessage("Successfully purchased " + bought + " " + name + "s."));
					}
				}, "No, I was just checking!", () -> {
					// Do nothing
				}).execute();
	}

	@Override
	public String toString() {
		return name + ">" + price + ">" + GPS + ">" + level + ">" + baseGPS;
	}

	public static MobData fromString(String toParse) {
		String parsed[] = toParse.split(">");
		return new MobData(parsed[0], Long.parseLong(parsed[1]), Long.parseLong(parsed[2]), Integer.parseInt(parsed[3]),
				Long.parseLong(parsed[4]));
	}

	public String getName() {
		return this.name;
	}

	public long getPrice() {
		return this.price;
	}

	public long getGPS() {
		return this.GPS;
	}

	public long getBaseGPS() {
		return this.baseGPS;
	}

	public int getLevel() {
		return level;
	}

	private int maxPurchase(long gold) {
		this.amountItCosted = 0;
		return maxPurchaseCalc(gold, this.price, 0);
	}

	private int maxPurchaseCalc(long goldLeft, long latestPrice, int soFar) {
		if (goldLeft < latestPrice) {
			this.newPrice = latestPrice;
			return soFar;
		}
		this.amountItCosted += latestPrice;
		return maxPurchaseCalc(goldLeft - latestPrice, (long) (Math.pow(latestPrice, EXPONENT)), soFar + 1);
	}

	private long totalPrice(int amount) {
		return totalPrice(this.price, amount);
	}

	private long totalPrice(long latestPrice, int left) {
		if (left == 1) {
			this.newPrice = (long) Math.pow(latestPrice, EXPONENT);
			return latestPrice;
		}
		return latestPrice + totalPrice((long) Math.pow(latestPrice, EXPONENT), left - 1);
	}
}
