package io.server.game.world.region.dynamic.boss.impl;

import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.GameObject;
import io.server.game.world.region.dynamic.boss.DynamicRegionHandler;
import io.server.util.Utility;

/**
 * 
 * @author Teek
 *
 */
public class ZulrahRegionController extends DynamicRegionHandler {
	
	private long startTime, finishTime;

	@Override
	public void onStart(Player player) {
		System.out.println("here test..");
		startTime = System.currentTimeMillis();
		player.sendMessage("<col=ff0000>testing.. - Index="+player.getIndex()+" RegionIndex="+(4 * player.getIndex())+"");
		
	}

	@Override
	public boolean metRequirements(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void process(Player player) {
		System.out.println("time elapsed="+Utility.convertMsToTime(System.currentTimeMillis() - startTime));
		
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
		// TODO Auto-generated method stub
	}

	@Override
	public boolean allowTeleportation(Player player) {
		player.getDynamicRegion().destroyInstance(false);
		return true;
	}

	@Override
	public void onExit(Player player) {
		finishTime = System.currentTimeMillis();
		player.sendMessage("Time Elapsed: "+Utility.convertMsToTime(finishTime - startTime));
	}


}
