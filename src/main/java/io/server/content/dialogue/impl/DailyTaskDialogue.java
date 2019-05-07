package io.server.content.dialogue.impl;

import io.server.content.dailyTasks.DailyTaskHandler;
import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.game.world.entity.mob.player.Player;

public class DailyTaskDialogue extends Dialogue {

	@Override
	public void sendDialogues(DialogueFactory factory) {
		
		Player player = factory.getPlayer();
		
		factory.sendOption("Daily Boss Task", new Runnable() {
			
			@Override
			public void run() { DailyTaskHandler.assign(player, 0); }
			
		}, "Daily Skill Task", new Runnable() {
			
			@Override
			public void run() { DailyTaskHandler.assign(player, 1); }
			
		}, "Daily PKing Task", new Runnable() {
			
			@Override
			public void run() { DailyTaskHandler.assign(player, 2); }
		},
			"Daily Blood Task", new Runnable() {
			
			@Override
			public void run() { DailyTaskHandler.assign(player, 3); }
			
		
		}).execute();
		
	}

}
