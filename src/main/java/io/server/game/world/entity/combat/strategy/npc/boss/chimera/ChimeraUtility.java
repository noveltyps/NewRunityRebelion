package io.server.game.world.entity.combat.strategy.npc.boss.chimera;

import io.server.game.world.World;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.MessageColor;
import io.server.util.Utility;

/**
 * Created by Adam_#6723 Chimera Utility Class, handles the spawning & generate
 */
public class ChimeraUtility {

	public static SpawnData6 spawn;

	public static boolean activated = false;

	public static boolean Chimerabutton;

	public static Npc generateSpawn() {
		activated = true;
		spawn = SpawnData6.generate();
		Npc Chimera = new Npc(4708, spawn.position, 10, Direction.NORTH);
		 World.sendBroadcast(1, "Chimera has just spawned! Do ::chimera to join the Event!  " +
		 spawn.location + "!", true);
		Chimera.register();
		Chimera.definition.setRespawnTime(-1);
		Chimera.definition.setAggressive(true);
		Chimera.speak("Darkness is here to penetrate your souls!");
		return Chimera;
	}

	/** Identification of all loot, it selects the loot */

	public static int[] ALWAYSLOOT = { 1, 1, 1, 1, 1, 2, 3, 4, 5 };
	public static int[] COMMONLOOT = { 6199, 989, 3140, 4087, 11732, 989, 12878, 6585, 4675 };
	public static int[] RARELOOT = { 11834, 11832, 11828, 11830, 11836, 11773, 13239, 13237, 13235, 11772, 11771, 11770,
			20143, 20002 };
	public static int[] SUPERRARELOOT = { 11862, 21225, 12817, 12825, 12821, 20997, 13652, 11802, 13576, 11785, 19481,
			11791, 12904, };

	public static void defeated(Npc Chimera, Player player) {

		World.sendMessage("<col=8714E6>Chimera has been defeated by " + player.getName() + ", the legend.");

		/**
		 * Constructs a new object for the ground item method, uses utility random, to
		 * randomise a number between the upper bound and lower bound of a number.
		 * 
		 * 
		 **/

		int random = Utility.random(100);

		if (random <= 50) {
			for (int i = 0; i < COMMONLOOT.length; i++) {
				GroundItem.createGlobal(player, new Item(COMMONLOOT[i], 1),
						new Position(spawn.getPosition().getX() + Utility.random(10),
								spawn.getPosition().getY() + Utility.random(10), 0));
			}
		}
		if (random <= 25) {
			for (int i = 0; i < RARELOOT.length; i++) {
				GroundItem.createGlobal(player, new Item(RARELOOT[i], 1),
						new Position(spawn.getPosition().getX() + Utility.random(10),
								spawn.getPosition().getY() + Utility.random(10), 0));
			}
		}
		if (random <= 5) {
			for (int i = 0; i < SUPERRARELOOT.length; i++) {
				GroundItem.createGlobal(player, new Item(SUPERRARELOOT[i], 1),
						new Position(spawn.getPosition().getX() + Utility.random(10),
								spawn.getPosition().getY() + Utility.random(10), 0));
				World.sendMessage("<img=10><col=FF0000>Chimera has dropped Bank Loot!");
			}
		}

		for (int i = 0; i < ALWAYSLOOT.length; i++) {
			GroundItem.createGlobal(player, new Item(ALWAYSLOOT[i], 1),
					new Position(spawn.getPosition().getX() + Utility.random(10),
							spawn.getPosition().getY() + Utility.random(10), 0));
		}

		player.send(new SendMessage("Chimera drop's lootation all over the map.", MessageColor.RED));
	}

	public enum SpawnData6 {
		LEVEL_46("", new Position(2410, 4678, 0), new Position(2407, 4680, 0));
		public final String location;
		public final Position position;
		public final Position tsunami;

		SpawnData6(String location, Position position, Position tsunami) {
			this.location = location;
			this.position = position;
			this.tsunami = tsunami;
		}

		public static SpawnData6 generate() {
			return Utility.randomElement(values());
		}

		public String getLocation() {
			return location;
		}

		public Position getPosition() {
			return position;
		}

		public Position getTsunami() {
			return tsunami;
		}

	}

}