package io.server.game.world.region.dynamic.boss;

import java.util.HashMap;
import java.util.Map;

import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.GameObject;
import io.server.game.world.region.dynamic.DynamicRegion.RegionType;
import io.server.game.world.region.dynamic.boss.impl.ZulrahRegionController;
import io.server.game.world.region.dynamic.minigames.DuoInferno4Session;
import io.server.game.world.region.dynamic.minigames.Raids14Session;
import io.server.game.world.region.dynamic.minigames.Raids24Session;
import io.server.game.world.region.dynamic.minigames.Raids34Session;
import io.server.game.world.region.dynamic.minigames.Raids44Session;
import io.server.game.world.region.dynamic.minigames.Raids54Session;

/**
 * 
 * @author Teek
 * 
 * Creation Date: 10/16/2018 - 08:06am
 *
 */
public abstract class DynamicRegionHandler {
	
	
	public static Map<RegionType, DynamicRegionHandler> controllers = new HashMap<RegionType, DynamicRegionHandler>();

	public abstract void onStart(Player player);
	
	public abstract boolean metRequirements(Player player);
	
	public abstract void process(Player player);
	
	public abstract boolean handleObjectInteraction(int objectOption, GameObject object);
	
	public abstract boolean handleNpcAttack(Npc npc);//TODO
	
	public abstract void onNPCDeath(Npc npc);
	
	public abstract void onPlayerDeath(Player player);
	
	public abstract boolean allowTeleportation(Player player);
	
	public abstract void onExit(Player player);//TODO
	
	static {
		controllers.put(RegionType.ZULRAH, new ZulrahRegionController());
		controllers.put(RegionType.DUOINFERNO, new DuoInferno4Session());
		controllers.put(RegionType.RAIDS2, new Raids24Session());
		controllers.put(RegionType.RAIDS, new Raids14Session());
		controllers.put(RegionType.RAIDS3, new Raids34Session());
		controllers.put(RegionType.RAIDS4, new Raids44Session());
		controllers.put(RegionType.RAIDS5, new Raids54Session());
		
		
	}
}
