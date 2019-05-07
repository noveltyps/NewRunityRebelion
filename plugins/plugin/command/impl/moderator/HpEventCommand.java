package plugin.command.impl.moderator;

import io.server.content.command.Command;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Position;
import io.server.util.Utility;

/**
 * 
 * @author Adam_#6723
 *
 */

public class HpEventCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		player.skills.setLevel(3, 1500);
		player.message("You have executed a Hitpoint Event! stay within this area!");
       World.sendMessage(player.getName() + " Has initiated the HP Event! to ::hp to teleport to him!");
		World.sendBroadcast(1, "Has initiated the HP Event! to ::hp to teleport to him! " + "!", true);
		for(SpawnLocation spawn : SpawnLocation.values()) {
			World.sendBroadcast(1, "Has initiated the HP Event! to ::hp to teleport to him! "
		+ spawn.getLocation() + "!", true);
			player.move((spawn.getPosition()));
		}
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isPriviledged(player);
		}
	
	public enum SpawnLocation {
		LEVEL_46("lvl 46 wild near Spider Hill", new Position(3135, 3888, 0)),
		LEVEL_16("lvl 16 wild near Bone Yard", new Position(3273, 3648, 0)),
		LEVEL_51("lvl 51 wild near Rogues Castle", new Position(3266, 3924, 0)),
		LEVEL_41("lvl 19 wild near graveyard of shadows", new Position(3197, 3670, 0)),
		LEVEL_47("lvl 47 wild near obelisk", new Position(3308, 3892, 0)),
		LEVEL_53("lvl 53 wild near scorpia's cave entrance", new Position(3211, 3944, 0));

		public final String location;
		public final Position position;

		SpawnLocation(String location, Position position) {
			this.location = location;
			this.position = position;
		}

		public static SpawnLocation generate() {
			return Utility.randomElement(values());
		}

		public String getLocation() {
			return location;
		}

		public Position getPosition() {
			return position;
		}

   }
	
	@Override
	public String description() {
		return "Starts an HP event.";
	}
}
