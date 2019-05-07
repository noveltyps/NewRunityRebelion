package io.server.game.world.entity.combat.strategy.npc.boss.gano;

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
 * Created by Daniel on 2017-12-20.
 */
public class GanoUtility {

	public static Npc generateSpawn() {

		int REPLACE_THIS = 0;
		SpawnData spawn = SpawnData.generate();
		Npc gano = new Npc(REPLACE_THIS, spawn.position, 10, Direction.NORTH);
		World.sendMessage("<col=8714E6> Wr3ckedyouboss has just spawned! He is located at " + spawn.location + "!",
				"<col=8714E6> First clan to kill him will be rewarded handsomely!");
		World.sendMessage("to enter the gano do ::wr3ckedyouboss and rid this beast from the world of Brutal!");
		World.sendBroadcast(1, "The gano boss has spawned enter by doing ::wr3ckedyouboss", true);
		gano.register();
		gano.definition.setRespawnTime(-1);
		gano.definition.setAggressive(true);
		gano.speak("Darkness is here to penetrate your souls!");
		return gano;
	}

	/** Identification of all loot, it selects the loot */
	public static int[] COMMONLOOT = { 6199, 15501, 989, 3140, 4087, 11732, 989 };
	public static int[] RARELOOT = { 20000, 20001, 20002, 15220, 15018, 15020, 15019, 6585, 4151, 11283, 11846, 11848,
			11850, 11852, 11854, 11856 };
	public static int[] SUPERRARELOOT = { 13887, 13893, 13899, 13905, 13884, 13890, 13896, 13902, 13858, 13861, 13864,
			13867, 13870, 13873, 13876 };

	public static void GeneratedLoot(Npc gano, Player player) {

	}

	public static void defeated(Npc gano, Player player) {

		/*
		 * int superrare = SUPERRARELOOT[Utility.random(SUPERRARELOOT.length - 1)]; int
		 * rare = RARELOOT[Utility.random(SUPERRARELOOT.length - 1)]; int common =
		 * COMMONLOOT[Utility.random(SUPERRARELOOT.length - 1)];
		 */

		boolean hasClan = player.clanChannel != null;

		if (hasClan) {
			player.clanChannel.getDetails().points += 5;
			player.clanChannel.addExperience(10000);
			World.sendMessage("<col=8714E6> gano has been defeated by " + player.getName() + "!");
			player.clanChannel.message("Hell yeah boys! We just killed gano!! We earned 10,000 EXP & 5 CP.");
		} else {
			World.sendMessage("<col=8714E6> gano has been defeated by " + player.getName()
					+ ", a solo individual with balls of steel!");

			/** Generates a random item from the int array list. **/

			/** Generates a random item from the integer array list. **/

			Item item = new Item(Utility.randomElement(COMMONLOOT));

			Item item1 = new Item(Utility.randomElement(RARELOOT));

			Item item2 = new Item(Utility.randomElement(SUPERRARELOOT));

			/**
			 * Constructs a new object for the ground item method, uses utility random, to
			 * randomise a number between the upper bound and lower bound of a number.
			 **/

			Position position = new Position(2269 + Utility.random(1, 2), 5342 + Utility.random(2, 3), 0);
			Position position1 = new Position(2264 + Utility.random(1, 4), 5336 + Utility.random(2, 5), 0);
			Position position2 = new Position(2271 + Utility.random(2, 3), 5345 + Utility.random(3, 4), 0);

			Position position3 = new Position(2281 + Utility.random(1, 6), 5354 + Utility.random(2, 7), 0);
			Position position4 = new Position(2278 + Utility.random(2, 7), 5335 + Utility.random(3, 8), 0);
			Position position5 = new Position(2258 + Utility.random(3, 8), 5341 + Utility.random(4, 9), 0);

			Position position6 = new Position(2270 + Utility.random(1, 5), 5342 + Utility.random(2, 10), 0);
			Position position7 = new Position(2256 + Utility.random(2, 8), 5345 + Utility.random(3, 5), 0);
			Position position8 = new Position(2265 + Utility.random(3, 2), 5339 + Utility.random(4, 5), 0);

			GroundItem.createGlobal(player, item, position);
			GroundItem.createGlobal(player, item1, position1);
			GroundItem.createGlobal(player, item2, position2);
			GroundItem.createGlobal(player, item, position3);
			GroundItem.createGlobal(player, item1, position4);
			GroundItem.createGlobal(player, item2, position5);
			GroundItem.createGlobal(player, item2, position6);
			GroundItem.createGlobal(player, item2, position7);
			GroundItem.createGlobal(player, item2, position8);

			player.send(new SendMessage("Glod drop's lootation all over the map.", MessageColor.RED));

		}

		gano.unregister();
	}

	public enum SpawnData {
		LEVEL_18("lvl 18 wild near east dragons", new Position(3307, 3668, 0)),
		LEVEL_19("lvl 19 wild near obelisk", new Position(3222, 3658, 0)),
		LEVEL_28("lvl 28 wild near vennenatis", new Position(3308, 3737, 0)),
		LEVEL_41("lvl 41 wild near callisto", new Position(3270, 3843, 0)),
		LEVEL_52("lvl 52 wild near obelisk", new Position(3304, 3929, 0)),
		LEVEL_53("lvl 53 wild near scorpia's cave entrance", new Position(3211, 3944, 0));

		public final String location;
		public final Position position;

		SpawnData(String location, Position position) {
			this.location = location;
			this.position = position;
		}

		public static SpawnData generate() {
			return Utility.randomElement(values());
		}
	}
}
