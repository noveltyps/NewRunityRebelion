package io.server.content.store.currency.impl;

import io.server.content.store.currency.Currency;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public final class BloodPointCurrency implements Currency {

	@Override
	public boolean tangible() {
		return false;
	}

	@Override
	public boolean takeCurrency(Player player, long amount) {
		if (player.bloodPoints >= amount) {
			player.bloodPoints -= amount;
			return true;
		} else {
			player.send(new SendMessage("You do not have enough blood points."));
			return false;
		}
	}

	@Override
	public void recieveCurrency(Player player, int amount) {
		player.bloodPoints += amount;
	}

	@Override
	public int currencyAmount(Player player) {
		return player.bloodPoints;
	}

	@Override
	public boolean canRecieveCurrency(Player player) {
		return true;
	}
}