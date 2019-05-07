package io.server.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.writer.InterfaceWriter;
import io.server.content.writer.impl.ObeliskWriter;
import io.server.game.task.Task;
import io.server.game.task.impl.ObjectReplacementEvent;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Boundary;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

/**
 * Created by Daniel on 2017-11-24.
 */
public class Obelisks {

	private static Map<Integer, Boolean> state = new HashMap<>();
	private static Obelisks INSTANCE = new Obelisks();

	public static Obelisks get() {
		return INSTANCE;
	}

	static {
		for (ObeliskData location : ObeliskData.values()) {
			state.put(location.objectId, false);
		}
	}

	public void open(Player player, int obj) {
		if (!PlayerRight.isSuper(player)) {
			player.dialogueFactory.sendStatement("You need to be a super donator to use this feature!").execute();
			return;
		}
		player.attributes.set("OBELISK", obj);
		InterfaceWriter.write(new ObeliskWriter(player));
		player.send(new SendString("Wilderness Obelisk", 51002));
		player.send(new SendString("Click on the obelisk you would like to teleport too", 51003));
		player.interfaceManager.open(51000);
	}

	public boolean activate(Player player, int objectId) {
		ObeliskData location = ObeliskData.forObject(objectId);
		return location != null && activate(player, objectId, ObeliskData.getRandom(location));
	}

	public boolean activate(Player player, int objectId, ObeliskData destination) {
		ObeliskData location = ObeliskData.forObject(objectId);

		if (location == null)
			return false;

		boolean active = state.get(objectId);

		if (active) {
			player.send(new SendMessage("The obelisk is already active, please wait."));
			return true;
		}

		state.put(objectId, true);
		int x = location.getBoundaries().getMinimumX();
		int y = location.getBoundaries().getMinimumY();
		GameObject one = World.getRegions().getRegion(player.getPosition()).getGameObject(objectId,
				new Position(x, y, player.getHeight()));
		GameObject two = World.getRegions().getRegion(player.getPosition()).getGameObject(objectId,
				new Position(x + 4, y, player.getHeight()));
		GameObject three = World.getRegions().getRegion(player.getPosition()).getGameObject(objectId,
				new Position(x, y + 4, player.getHeight()));
		GameObject four = World.getRegions().getRegion(player.getPosition()).getGameObject(objectId,
				new Position(x + 4, y + 4, player.getHeight()));
		World.schedule(new ObjectReplacementEvent(one, 14825, 15));
		World.schedule(new ObjectReplacementEvent(two, 14825, 15));
		World.schedule(new ObjectReplacementEvent(three, 14825, 15));
		World.schedule(new ObjectReplacementEvent(four, 14825, 15));
		player.attributes.set("OBELISK", -1);

		World.schedule(new Task(14) {
			@Override
			protected void execute() {
				state.put(location.objectId, false);
				Boundary boundary = new Boundary(location.boundary.getMinimumX() + 1,
						location.boundary.getMinimumY() + 1, location.boundary.getMinimumX() + 3,
						location.boundary.getMinimumY() + 3);
				List<Player> players = Boundary.getPlayers(boundary);

				if (players.size() > 0) {
					for (Player p : players) {
						if (p.isDead())
							continue;
						int x = destination.getBoundaries().getMinimumX() + 1;
						int y = destination.getBoundaries().getMinimumY() + 1;
						Position position = new Position(x + Utility.random(2), y + Utility.random(2),
								player.getHeight());
						Teleportation.teleport(p, position, Teleportation.TeleportationData.OBELISK, () -> {
						});
					}
				}
				cancel();
			}
		});
		return true;
	}

	public enum ObeliskData {
		LEVEL_13(14829, new Boundary(3154, 3618, 3158, 3622)), LEVEL_19(14830, new Boundary(3225, 3665, 3229, 3669)),
		LEVEL_27(14827, new Boundary(3033, 3730, 3037, 3734)), LEVEL_35(14828, new Boundary(3104, 3792, 3108, 3796)),
		LEVEL_44(14826, new Boundary(2978, 3864, 2982, 3868)), LEVEL_50(14831, new Boundary(3305, 3914, 3309, 3918));

		private final int objectId;
		private final Boundary boundary;

		ObeliskData(int objectId, Boundary boundary) {
			this.objectId = objectId;
			this.boundary = boundary;
		}

		public Boundary getBoundaries() {
			return boundary;
		}

		static ObeliskData forObject(int objectId) {
			for (ObeliskData l : values()) {
				if (l.objectId == objectId) {
					return l;
				}
			}
			return null;
		}

		static ObeliskData getRandom(ObeliskData exclude) {
			ArrayList<ObeliskData> locations = new ArrayList<>(Arrays.asList(values()));
			locations.remove(exclude);
			return locations.get(Utility.random(locations.size() - 1));
		}
	}
}
