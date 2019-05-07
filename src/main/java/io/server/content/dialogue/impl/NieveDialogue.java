package io.server.content.dialogue.impl;

import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.content.skill.impl.slayer.SlayerOfferings;
import io.server.content.skill.impl.slayer.SlayerTab;
import io.server.game.world.entity.mob.player.Player;

public class NieveDialogue extends Dialogue {

	@Override
	public void sendDialogues(DialogueFactory factory) {
		Player player = factory.getPlayer();

		factory.sendNpcChat(6797, Expression.HAPPY, "Hello adventurer, how may I help you?").sendOption(
				"Offer slayer items", () -> sell(factory), "Open slayer manager",
				() -> factory.onAction(() -> player.slayer.open(SlayerTab.MAIN)), "Nevermind",
				() -> factory.onAction(player.interfaceManager::close));
		factory.execute();
	}

	private void sell(DialogueFactory factory) {
		factory.sendOption("What do I get for my offerings?", () -> {
			factory.sendNpcChat(6797, "You will get slayer experience and points!");
		}, "What items can be offered?", () -> {
			factory.sendStatement("Ensouled heads", "Ancient shards & Dark Totem pieces");
		}, "Offer all items", () -> {
			factory.onAction(() -> SlayerOfferings.offer(factory.getPlayer()));
		}, "Nevermind", factory::clear);
	}

}
