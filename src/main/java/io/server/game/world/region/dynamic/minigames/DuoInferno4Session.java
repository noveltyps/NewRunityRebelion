package io.server.game.world.region.dynamic.minigames;

import io.server.content.activity.impl.duoinferno.DuoInferno;
import io.server.content.dialogue.DialogueFactory;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.GameObject;
import io.server.game.world.region.dynamic.boss.DynamicRegionHandler;
import io.server.net.packet.out.SendFadeScreen;
import io.server.util.Utility;


public class DuoInferno4Session extends DynamicRegionHandler {
	
	private long startTime, finishTime;
	
	private Player player;
	
	private Player partner;

	@Override
	public void onStart(Player player) {
		this.player = player;
		this.partner = player.allForOnePartner;
		startTime = System.currentTimeMillis();
		partner.locking.lock();
		player.locking.lock();
		player.send(new SendFadeScreen("Welcome to the Duo Inferno!", 1, 3));
		World.sendMessage(player.getName() + " Was Litty enough to take on Duo Inferno!");
		World.schedule(5, () -> {
			DuoInferno.create(player, partner);
			player.locking.unlock();
			partner.locking.unlock();
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
		partner.setAllForOnePartner(null);
		player.setAllForOnePartner(null);
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
		partner.setAllForOnePartner(null);
		player.setAllForOnePartner(null);
	}
	
	public void resetPartner() {
		partner.setAllForOnePartner(null);
		player.setAllForOnePartner(null);
		player.message("You have reset your double threat partner!");
		partner.message("You have reset your double threat partner!");
	}
	
	private void sendPartnerMessage(String message) {
		if (message == null || partner == null)
			return;
		player.sendMessage(message);
		partner.sendMessage(message);
	}

	public static void sendPartnerRequest(Player player, Player other) {
		if (other == null) {
			player.sendMessage("Unable to send request.. other person is offline.");
			return;
		}
		player.sendMessage("Sending partner request to "+other.getUsername()+"...");
		player.lastPartnerRequest += System.currentTimeMillis() + 15000;
		DialogueFactory f = other.dialogueFactory;
		f.sendOption("Accept "+player.getUsername()+"'s Double Threat partner request", () -> f.onAction(() -> {
			f.clear();
			player.setAllForOnePartner(player, other);
			player.sendPartnerMessage(player, other, "You have been partnered! make your way over to the Duo Inferno Portal!");
			player.sendMessage(other.getUsername()+" has accepted your partner request.");
		}), "Decline Request", () -> f.onAction(() -> {
			f.clear();
			other.sendMessage("You have declined "+player.getUsername()+"'s partner request");
			player.sendMessage(other.getUsername()+" has declined your partner request.");
		})).execute();
	}


}
