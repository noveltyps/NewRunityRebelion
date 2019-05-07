package io.server.content.skill.impl.construction;

import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.position.Position;
import io.server.util.generic.GenericVoid;

public enum BuildableMap implements GenericVoid<Player> {
	SMALL_CAVE("Small cave", 0, 0, new Position(3305, 9833)) {
		@Override
		public void handle(Player player) {
			player.face(Direction.SOUTH);
			new CustomGameObject(4525, new Position(3305, 9832, player.house.getHeight())).register();// Portal
		}
	},
	THRONE_ROOM("Throne room", 90, 15_000000, new Position(2036, 4538)) {
		@Override
		public void handle(Player player) {
			player.face(Direction.SOUTH);
			new CustomGameObject(4525, new Position(2036, 4539, player.house.getHeight())).register();// Portal
		}
	};

	private final String name;
	private final int level;
	private final int cost;
	private final Position position;

	BuildableMap(String name, int level, int cost, Position position) {
		this.name = name;
		this.level = level;
		this.cost = cost;
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getCost() {
		return cost;
	}

	public Position getPosition() {
		return position;
	}
}
