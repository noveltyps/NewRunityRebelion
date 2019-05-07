package io.server.content.activity.impl.battlerealm;

import java.util.ArrayList;

import io.server.game.world.object.ObjectDirection;
import io.server.game.world.object.ObjectType;
import io.server.game.world.position.Position;

public class BattleRealmObjects {
	public static ArrayList<ObjectArgs> battleRealmObjects = new ArrayList<>();

	public static class ObjectArgs {
		public final int id;
		public final Position position;
		public final ObjectDirection rotation;
		public final ObjectType type;

		public ObjectArgs(int id, Position position, ObjectDirection rotation, ObjectType type) {
//            System.out.println("Added a new objectArg: id=" + id + "pos="+position + "rot=" + rotation + "type="+type);
			this.id = id;
			this.position = position;
			this.rotation = rotation;
			this.type = type;
		}
	}
}
