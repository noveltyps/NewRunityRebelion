package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.game.task.impl.ObjectPlacementEvent;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.object.ObjectDirection;
import io.server.game.world.object.ObjectType;
import io.server.net.packet.out.SendMessage;

/**
 * @author Adam_#6723
 */

public class ObjectCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		int id = Integer.parseInt(parts[1]);
		int rotation = 0;
		CustomGameObject gameObject = new CustomGameObject(id, player.getPosition().copy(),
				ObjectDirection.valueOf(rotation).orElse(ObjectDirection.WEST), ObjectType.INTERACTABLE);
		World.schedule(new ObjectPlacementEvent(gameObject, 50));
		player.send(new SendMessage("Spawned temporary object " + id + "."));

	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Spawns the specified object.";
	}
}
