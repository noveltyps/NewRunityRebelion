package plugin.click.object;

import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.skill.impl.agility.Agility;
import io.server.content.skill.impl.agility.obstacle.Obstacle;
import io.server.content.skill.impl.agility.obstacle.ObstacleType;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.Interactable;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;

public class AgilityObjectClickPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		return Agility.clickButton(player, button);
	}

	@Override
	protected boolean firstClickObject(Player player, ObjectClickEvent event) {
		final Obstacle obstacle = Agility.obstacles.get(event.getObject().getPosition());

		if (obstacle == null) {
			return false;
		}

		player.walkTo(Interactable.create(obstacle.getStart(), 0, 0), false, () -> {
			if (player.attributes.get("AGILITY_FLAGS") == null) {
				player.attributes.set("AGILITY_FLAGS", 0);
			}
			if (obstacle.getType() == ObstacleType.ROPE_SWING || obstacle.getType() == ObstacleType.WILDERNESS_COURSE) {
				player.attributes.set("AGILITY_OBJ", event.getObject());
			}
			player.attributes.set("AGILITY_TYPE", obstacle.getType());
			World.schedule(1, () -> {
				player.face(event.getObject());
				obstacle.execute(player);
				RandomEventHandler.trigger(player);
			});
		});
		return true;
	}

}
