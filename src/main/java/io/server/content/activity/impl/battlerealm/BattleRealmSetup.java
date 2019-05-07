package io.server.content.activity.impl.battlerealm;

import static io.server.content.activity.impl.battlerealm.BattleRealmObjects.battleRealmObjects;

import java.util.ArrayList;

import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.position.Position;

public class BattleRealmSetup {

	public void addLobbyObjects(int instance) {
		new CustomGameObject(869, instance, new Position(2547, 3822)).register();
		new CustomGameObject(3680, instance, new Position(2537, 3822)).register();
	}

	public void spawnEveryObject(int instance, ArrayList<CustomGameObject> gameObjects) {
		World.schedule(new Task(1) {
			int count = 0;

			@Override
			protected void execute() {
				if (count == 0) {
					World.sendMessage("BattleRune is loading! You may Experience some lag.");
				}
				loadObjs(count, instance, gameObjects);

				if (count >= 3) {
					World.sendMessage("BattleRune has finished loading!");
					cancel();
				}
				count++;
			}

		});
	}

	public void loadObjs(int count, int instance, ArrayList<CustomGameObject> gameObjects) {
		int quarter = battleRealmObjects.size() / 4;
		for (int i = quarter * count; i < quarter * (count + 1) && i < battleRealmObjects.size(); i++) {
			BattleRealmObjects.ObjectArgs objArgs = battleRealmObjects.get(i);

			CustomGameObject toSpawn = new CustomGameObject(objArgs.id, objArgs.position, objArgs.rotation,
					objArgs.type);
			toSpawn.instance = instance;

			System.out.println("Spawning " + toSpawn.getName() + " at " + objArgs.position + ", instance: "
					+ toSpawn.getInstancedHeight() + "(should=" + toSpawn.instance);
			toSpawn.register();
			gameObjects.add(toSpawn);
		}
		World.sendMessage("BattleRune has loaded " + (count + 1) * 25 + "%");
	}

	public void spawnMonsters(ArrayList<Npc> monsters) {
		/*
		 * for (Npc npc : morphicAttackers) {
		 * 
		 * }
		 */
	}
}
