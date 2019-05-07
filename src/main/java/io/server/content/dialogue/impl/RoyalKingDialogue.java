package io.server.content.dialogue.impl;

import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.content.store.Store;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendURL;

/**
 * The royal king dialogue.
 *
 * @author Daniel
 */
public class RoyalKingDialogue extends Dialogue {

	private int index;

	public RoyalKingDialogue(int index) {
		this.index = index;
	}

	@Override
	public void sendDialogues(DialogueFactory factory) {
		if (index == 1) {
			factory.execute();
			return;
		}
		if (index == 2) {
			store(factory);
			factory.execute();
			return;
		}
		Player player = factory.getPlayer();
		factory.sendNpcChat(5523, Expression.HAPPY, "Hello adventurer, how may I help you?");
		factory.sendOption("Donator Information",
				() -> player.send(new SendURL("www.RebelionX.org/forums")), "Open Store", () -> store(factory), "Nevermind",
				factory::clear);
		factory.execute();
	}


	private void store(DialogueFactory factory) {
		factory.sendOption("Open Donator Store", () -> Store.STORES.get("Donator Store").open(factory.getPlayer()),
				"Misc Donator Store", () -> Store.STORES.get("Misc Donator Store").open(factory.getPlayer()),
				"Rare Donator Store", () -> Store.STORES.get("Rare Donator Store").open(factory.getPlayer()),
				"Nevermind", factory::clear);
	}
}
