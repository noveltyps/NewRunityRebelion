package io.server.content.dialogue.impl;

import io.server.content.activity.impl.battlerealm.BattleRealm;
import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;

public class RealmLordDialogue extends Dialogue {

	@Override
	public void sendDialogues(DialogueFactory factory) {
		factory.sendNpcChat(15, Expression.HAPPY, "Hello adventurer, how may I help you?").sendOption(
				"Ask about the Battle Realm", () -> explain(factory), "Join the Battle Realm",
				() -> enterBattleRealm(factory), "Never mind", factory::clear);
		factory.execute();
	}

	private void explain(DialogueFactory factory) {
		factory.sendNpcChat(15, "Here's an explanation on the game!", "Here's an explanation on the game!",
				"Here's an explanation on the game!").sendNpcChat(15, "Would you like to play?")
				.sendOption("Yes", () -> {
					enterBattleRealm(factory);
				}, "Maybe another time.", factory::clear);
	}

	public static void enterBattleRealm(DialogueFactory factory) {
		if (factory.getPlayer().inventory.isEmpty() && factory.getPlayer().equipment.isNaked()) {
			factory.getPlayer().message("Entering you to the BattleRealm");
			BattleRealm.enter(factory.getPlayer());
		} else {
			factory.sendNpcChat(15, "Sorry! You can't bring anything with you", "into the Battle Realm.");
		}
	}

}
