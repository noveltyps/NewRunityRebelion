package io.server.content.masterminer;

import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.game.world.entity.mob.player.Player;

public class AdventureGUI {
	private final Player player;
	private final MasterMinerData data;

	public AdventureGUI(Player player) {
		this.player = player;
		this.data = player.masterMinerData;
	}

	public void open() {
		if (data.prestigeLevel < 5) {
			DialogueFactory factory = new DialogueFactory(player);
			factory.sendNpcChat(250, Expression.SLIGHTLY_SAD, "Sorry.. This feature isn't available yet.",
					"It will require level 5 on launch, however!");
//            factory.sendNpcChat(250, Expression.SLIGHTLY_SAD, "Sorry, you must be MasterMiner level of at least 5", "to go on an adventure..");
			factory.execute();
		} else {
			player.interfaceManager.open(24600);
		}
	}
}
