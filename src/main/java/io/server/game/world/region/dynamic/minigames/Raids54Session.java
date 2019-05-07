package io.server.game.world.region.dynamic.minigames;

import java.util.ArrayList;
import java.util.List;

import io.server.content.activity.impl.raids4.Raids4;
import io.server.content.activity.impl.raids5.Raids5;
import io.server.content.dialogue.DialogueFactory;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.GameObject;
import io.server.game.world.region.dynamic.boss.DynamicRegionHandler;
import io.server.net.packet.out.SendFadeScreen;
import io.server.util.Utility;


public class Raids54Session extends DynamicRegionHandler {
	
	private long startTime, finishTime;
	
	private Player player;
	
	private Player partner;
	
	private List<Player> partners = new ArrayList<>();

	@Override
	public void onStart(Player player) {
		this.player = player;
		this.partners = player.getRaidPartners();
		startTime = System.currentTimeMillis();
		player.locking.lock();
		for (Player p : partners)
			p.locking.lock();
		player.send(new SendFadeScreen("Welcome to the Raids 5!", 1, 3));
		World.sendMessage(player.getName() + " Was Litty enough to take on Raids 5!");
		World.schedule(5, () -> {
			Raids5.create(player, partners);
			player.locking.unlock();
			for (Player p : partners)
				p.locking.unlock();
		});		
	}

	@Override
	public boolean metRequirements(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void process(Player player) {
		
	}

	@Override
	public boolean handleObjectInteraction(int objectOption, GameObject object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleNpcAttack(Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onNPCDeath(Npc npc) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerDeath(Player player) {
		
		if (this.player.equals(player)) {
			this.player = null;
			player.getRaidPartners().clear();
			for (Player p : partners)
				p.removeRaidPartner(player);
		}
		
		if (partners.contains(player)) {
			partners.remove(player);
			this.player.removeRaidPartner(player);
			for (Player p : partners)
				p.removeRaidPartner(player);
		}
		
//		partner.setAllForOnePartner(null);
//		partner.removeRaidPartner(player);
//		player.setAllForOnePartner(null);
//		player.getRaidPartners().clear();
		// TODO Auto-generated method stub
	}

	@Override
	public boolean allowTeleportation(Player player) {
		//player.getDynamicRegion().destroyInstance(false);
		return true;
	}

	@Override
	public void onExit(Player player) {
		finishTime = System.currentTimeMillis();
		sendPartnerMessage("Time Elapsed: "+Utility.convertMsToTime(finishTime - startTime));
//		partner.setAllForOnePartner(null);
//		player.setAllForOnePartner(null);
//		partner.removeRaidPartner(player);
//		player.setAllForOnePartner(null);
//		player.getRaidPartners().clear();
		
		if (this.player.equals(player)) {
			this.player = null;
			player.getRaidPartners().clear();
			for (Player p : partners)
				p.removeRaidPartner(player);
		}
		
		if (partners.contains(player)) {
			partners.remove(player);
			this.player.removeRaidPartner(player);
			for (Player p : partners)
				p.removeRaidPartner(player);
		}
		
	}
	
	/**
	 * add null checks 
	 */
	
	public void resetPartner() {
		
		player.getRaidPartners().clear();
		player.message("You have reset your Raids 5 partner!");
		
		for (Player p : partners) {
			p.getRaidPartners().clear();
			p.sendMessage("Your Raids 5 partners have been reset!");
		}
		
//		if(player.getAllForOnePartner() != null) {
//		partner.setAllForOnePartner(null);
//		player.setAllForOnePartner(null);
//		player.message("You have reset your Raids 5 partner!");
//		partner.message("You have reset your Raids 5 partner!");
//		} else {
//			player.message("stfu");
//		}
	}
	
	private void sendPartnerMessage(String message) {
		if (message == null)
			return;
		player.sendMessage(message);
		for (Player p : partners)
			p.sendMessage(message);
//		partner.sendMessage(message);
	}

	public static void sendPartnerRequest(Player player, Player other) {
		if (other == null) {
			player.sendMessage("Unable to send request.. other person is offline.");
			return;
		}
		player.sendMessage("Sending partner request to "+other.getUsername()+"...");
		player.lastPartnerRequest = System.currentTimeMillis() + 15000;
		DialogueFactory f = other.dialogueFactory;
		f.sendOption("Accept "+player.getUsername()+"'s Raids 5 partner request", () -> f.onAction(() -> {
			f.clear();
//			player.setAllForOnePartner(player, other);
			player.partyLeader = player;
			other.partyLeader = other;
			player.addRaidPartner(other);
			other.addRaidPartner(player);
			player.sendPartyMessage(Utility.highlightGreenText(other.getUsername() + " has joined your party!"), false);
			player.sendPartyMessage(Utility.highlightBlueText("There are currently " + (player.getRaidPartners().size() + 1) + " players in your party."), false);
//			player.sendPartnerMessage(player, other, "You have been partnered! Make your way over to the Raids 5 Portal!");
			player.sendMessage(Utility.highlightGreenText(other.getUsername() + " has accepted your partner request!"));
			player.sendMessage(Utility.highlightBlueText("You currently have " + (player.getRaidPartners().size() + 1) + " players in your party."));
		}), "Decline Request", () -> f.onAction(() -> {
			f.clear();
			other.sendMessage(Utility.highlightText("You have declined "+player.getUsername()+"'s partner request"));
			player.sendMessage(Utility.highlightText(other.getUsername()+" has declined your partner request."));
		})).execute();
	}


}
