package plugin.click.button;

import io.server.Config;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.combat.strategy.npc.boss.arena.ArenaUtility;
import io.server.game.world.entity.combat.strategy.npc.boss.galvek.GalvekUtility;
import io.server.game.world.entity.combat.strategy.npc.boss.galvek.GalvekUtility.SpawnData1;
import io.server.game.world.entity.combat.strategy.npc.boss.magearena.PorazdirUtility;
import io.server.game.world.entity.combat.strategy.npc.boss.skotizo.SkotizoUtility;
import io.server.game.world.entity.combat.strategy.npc.boss.skotizo.SkotizoUtility.SpawnData;
import io.server.game.world.entity.mob.player.Player;

/**
 * 
 * @author Adam_#6723
 *
 */

public class BossEventButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (button == -19935) {
			
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
	            player.message("@red@You can no longer take custom's into the wilderness!");
	            return true;
	        }
				
			if (GalvekUtility.activated == true) {
				SpawnData1 galvekpos = GalvekUtility.spawn;
				Teleportation.teleport(player, galvekpos.getPosition());
				player.message("You have teleported to Galvek");
				player.hideTeleportButton();
				return true;
			}
		}
		if (button == -18935) {
			if (ArenaUtility.activated == true) {
				Teleportation.teleport(player, Config.ARENA_ZONE);
				player.message("Teleporting you to Glod");
				player.hideTeleportButton1();
				return true;
			}
		}
		if (button == -17935) {
			
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
	            player.message("@red@You can no longer take custom's into the wilderness!");
	            return true;
	        }
			
			if (PorazdirUtility.activated == true) {
				Teleportation.teleport(player, Config.PORAZDIR_ZONE);
				player.message("You have teleported to Porazdir");
				player.hideTeleportButton2();
				return true;
			}
			return false;
		}
		if (button == -16935) {
			
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
	            player.message("@red@You can no longer take custom's into the wilderness!");
	            return true;
	        }
			
			if (SkotizoUtility.activated == true) {
		
				SpawnData skotizo = SkotizoUtility.spawn;
				Teleportation.teleport(player, skotizo.getPosition());
				player.message("You have teleported to Skotizo");
				player.hideTeleportButton3();
				return true;
			}
			return false;
		}
		player.hideTeleportButton();
		player.hideTeleportButton1();
		player.hideTeleportButton2();
		player.hideTeleportButton3();
		return false;
	}
}
