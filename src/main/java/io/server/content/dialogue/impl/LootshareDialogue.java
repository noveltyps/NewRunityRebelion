package io.server.content.dialogue.impl;

import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.content.store.Store;

/**
 * 
 * @author Teek
 * 
 * 9/11/2018 - 08:38am
 *
 */
public class LootshareDialogue extends Dialogue {

	public LootshareDialogue(int index) {
	}

	@Override
	public void sendDialogues(DialogueFactory factory) {

		factory.getPlayer();
		factory.sendNpcChat(5608, Expression.HAPPY, "Hello adventurer, how may I help you?");
		factory.sendOption("Open Store", () -> store(factory), "Nevermind", factory::clear);
		factory.execute();
	}

	/*
	 * private void claim(DialogueFactory factory) { //factory.onAction(() ->
	 * DonationService.claimDonation(factory.getPlayer())); }
	 */

	private void store(DialogueFactory factory) {
		factory.sendOption("Open Boss Point Store",
				() -> Store.STORES.get("Boss Point Store").open(factory.getPlayer()), "Open Trivia Point Store",
				() -> Store.STORES.get("Trivia Point Store").open(factory.getPlayer()), "Nevermind", factory::clear);
	}
}
