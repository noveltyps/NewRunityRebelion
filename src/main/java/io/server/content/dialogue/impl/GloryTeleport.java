package io.server.content.dialogue.impl;

import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.game.world.entity.mob.player.Player;

/**
 * 
 * @author Teek
 *
 * 9/11/2018 - 08:39am
 */
public class GloryTeleport extends Dialogue {

	//private Player player;
	
	//private int itemId;
	
	//private boolean fromNecklas;
	
	
	
	public GloryTeleport(Player player, int itemId, boolean fromNecklas) {
		//this.player = player;
		//this.itemId = itemId;
		//this.fromNecklas = fromNecklas;
	}

	@Override
	public void sendDialogues(DialogueFactory factory) {
		factory.sendOption("Edgeville", null, "Karamja", null);
	}
	/*
	private void handle() {
		Item neck = player.equipment.get(Equipment.AMULET_SLOT);
		//TODO
		if (fromNecklas) {
		   if (neck != null) {
			   if (neck.getId() > 1704)
			       neck.setId(neck.getId() - 2);
		   }
		}
	}*/
}