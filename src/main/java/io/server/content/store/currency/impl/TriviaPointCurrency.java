package io.server.content.store.currency.impl;

import io.server.content.store.currency.Currency;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public final class TriviaPointCurrency implements Currency {

	@Override
	public boolean tangible() {
		return false;
	}

	@Override
	public boolean takeCurrency(Player player, long amount) {
		if (player.triviaPoints >= amount) {
			player.triviaPoints -= amount;
			return true;
		} else {
			player.send(new SendMessage("You do not have enough Boss Points."));
			return false;
		}
	}

	@Override
	public void recieveCurrency(Player player, int amount) {
		player.triviaPoints += amount;
	}

	@Override
	public int currencyAmount(Player player) {
		return player.triviaPoints;
	}

	@Override
	public boolean canRecieveCurrency(Player player) {
		return true;
	}
}
