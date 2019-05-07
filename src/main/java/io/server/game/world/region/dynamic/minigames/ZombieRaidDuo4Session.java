package io.server.game.world.region.dynamic.minigames;

import io.server.content.activity.impl.raids5.Raids5;
import io.server.content.activity.impl.zombieraidduo.ZombieRaidDuo;
import io.server.content.dialogue.DialogueFactory;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.GameObject;
import io.server.game.world.region.dynamic.boss.DynamicRegionHandler;
import io.server.net.packet.out.SendFadeScreen;
import io.server.util.Utility;


public class ZombieRaidDuo4Session extends DynamicRegionHandler {
	
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
		player.send(new SendFadeScreen("Welcome to the Zombie Raid Duo!", 1, 3));
		World.sendMessage(player.getName() + " Was Litty enough to take on Zombie Raid Duo!");
		World.schedule(5, () -> {
			ZombieRaidDuo.create(player, partner);
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
	
	/**
	 * add null checks 
	 */
	
	public void resetPartner() {
		if(player.getAllForOnePartner() != null) {
		partner.setAllForOnePartner(null);
		player.setAllForOnePartner(null);
		player.message("You have reset your Zombie Raid Duo partner!");
		partner.message("You have reset your Zombie Raid Duo partner!");
		} else {
			player.message("stfu");
		}
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
		f.sendOption("Accept "+player.getUsername()+"'s Zombie Raid Duo partner request", () -> f.onAction(() -> {
			f.clear();
			player.setAllForOnePartner(player, other);
			player.sendPartnerMessage(player, other, "You have been partnered! make your way over to the Zombie Raid Duo Portal!");
			player.sendMessage(other.getUsername()+" has accepted your partner request.");
		}), "Decline Request", () -> f.onAction(() -> {
			f.clear();
			other.sendMessage("You have declined "+player.getUsername()+"'s partner request");
			player.sendMessage(other.getUsername()+" has declined your partner request.");
		})).execute();
	}


}
