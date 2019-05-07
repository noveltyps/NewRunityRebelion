package io.server.content.dialogue.impl;

import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.game.world.entity.mob.player.Player;

/**
 * Handles the prestige dialogue.
 *
 * @author Daniel
 */
public class PrestigeDialogue extends Dialogue {

	/** The dialogue npc identification. */
	private static final int POLLY = 345;

	@Override
	public void sendDialogues(DialogueFactory factory) {
		Player player = factory.getPlayer();
		factory.sendNpcChat(POLLY, Expression.HAPPY, "Hello #username, my friend.", "How may I help you today?");
		factory.sendOption("How do prestiges work?", () -> explainPrestige(factory), "How do perks work?",
				() -> explainPerks(factory), "Open prestige panel", player.prestige::open, "Nevermind", factory::clear);
		factory.execute();
	}

	private void explainPrestige(DialogueFactory factory) {
		factory.sendNpcChat(POLLY, Expression.HAPPY, "The concept of prestiges is very simple!",
				"Once you have achieved level 99 in any non-combat",
				"skill, you will be permitted to prestige that skill.");
		factory.sendNpcChat(POLLY, Expression.HAPPY, "Upon prestiging that skill, it will be reset back to 1 and you ",
				" will receive 1,000,000 coins and 1 prestige point.",
				"Players are only allowed to prestige each skill up to 5 times.");
		factory.sendNpcChat(POLLY, Expression.HAPPY, "Each prestige has a different color. You have the ability",
				"to toggle the colors to display in your skill tab.");
	}

	private void explainPerks(DialogueFactory factory) {
		factory.sendNpcChat(POLLY, Expression.HAPPY, "Perks are very simple to use. Once you have accumulated",
				"prestige points you can access my shop. Within my shops",
				"are different perks which all have different effects.");
		factory.sendNpcChat(POLLY, Expression.HAPPY, "You can talk to me if you would like to see all the",
				"perks and their ability. All perks stack!");
	}
}
