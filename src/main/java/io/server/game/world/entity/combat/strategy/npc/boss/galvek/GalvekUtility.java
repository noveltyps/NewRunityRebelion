package io.server.game.world.entity.combat.strategy.npc.boss.galvek;

import io.server.content.discord.DiscordConstant;
import io.server.content.discord.DiscordManager;
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
 * Created by Adam_#6723 Galvek Utility Class, handles the spawning & generate
 */
public class GalvekUtility {

	public static SpawnData1 spawn;

	public static boolean activated = false;

	public static boolean galvekbutton;

	public static Npc generateSpawn() {
		activated = true;
		spawn = SpawnData1.generate();
		Npc Galvek = new Npc(8095, spawn.position, 10, Direction.NORTH);//...
		System.out.println("spawnX="+spawn.position.getX());//rerun
		World.sendBroadcast(1, "Galvek has just spawned! He is located at " + spawn.location + "!", true);
		World.sendGalvekInformation();
		Galvek.register();
		Galvek.definition.setRespawnTime(-1);
		Galvek.definition.setAggressive(true);
		Galvek.speak("Darkness is here to penetrate your souls!");
		new DiscordManager(DiscordConstant.PUBLIC_BOSS_EVENTS, "Boss Event", "Galvek has just spawned! He is located at " + spawn.location + "!").log1();
		return Galvek;
	}

	/** Identification of all loot, it selects the loot */

	public static int[] ALWAYSLOOT = {  6889, 4675, 11770, 6585, 1305,  4587, 5698, 1434, 1704, 3751, 10828, 4153, 2550 };
	public static int[] COMMONLOOT = { 989, 3140, 4087, 11732, 989, 4675, 11840, 11836,12877 };
	public static int[] RARELOOT = { 4153, 7158, 1305, 4587, 11840,  6585, 12875, 12873, 11771, 11772, 4151};
	public static int[] SUPERRARELOOT = { 6199, 11802, 11283, 13652, 12904, 11791, 12691, 12692, 12877};
	public static int[] ULTRA = {22280, 13702, 22317, 13701, 13700,13699, 13698, 13697, 10860,};

	public static void defeated(Npc Galvek, Player player) {
		
		int deathX = Galvek.getPosition().getX();
		int deathY = Galvek.getPosition().getY();
		
		if(spawn == null) {
			System.out.println("DIDN'T EXECUTE BECAUSE.. 1 " + player.getName() + " is in an instanced minigame.... 1");
			return;
		}

		boolean hasClan = player.clanChannel != null;

		if (hasClan) {
			player.clanChannel.getDetails().points += 5;
			player.clanChannel.addExperience(10000);
			World.sendMessage("<col=8714E6>Galvek has been defeated by " + player.getName() + "!");
			player.clanChannel.message("Hell yeah boys! We just killed Galvek!! We earned 10,000 EXP & 5 CP.");
		}
		World.sendMessage("<col=8714E6>Galvek has been defeated by " + player.getName() + ", the legend.");

		/**
		 * Constructs a new object for the ground item method, uses utility random, to
		 * randomise a number between the upper bound and lower bound of a number.
		 * 
		 * 
		 **/

		int random = Utility.random(750);
		

		if (random <= 50) {
				GroundItem.createGlobal(player, new Item(COMMONLOOT[Utility.random(COMMONLOOT.length)], 1),
						new Position(deathX + Utility.random(10), deathY + Utility.random(10), 0));
		}
		if (random <= 25) {
			GroundItem.createGlobal(player, new Item(RARELOOT[Utility.random(RARELOOT.length)], 1),
						new Position(deathX + Utility.random(10),
								deathY + Utility.random(10), 0));
			
		}
		if (random <= 5) {
			GroundItem.createGlobal(player, new Item(SUPERRARELOOT[Utility.random(SUPERRARELOOT.length)], 1),
						new Position(deathX + Utility.random(10),
								deathY + Utility.random(10), 0));
				World.sendMessage("<img=10><col=FF0000>Galvek has dropped Bank Loot!");
			
		}
		
		if (random <= 1 && Utility.random(1, 5) == 4) {
			GroundItem.createGlobal(player, new Item(ULTRA[Utility.random(ULTRA.length)], 1),
						new Position(deathX + Utility.random(10),
								deathY + Utility.random(10), 0));
				World.sendMessage("<img=10><col=FF0000>Galvek has dropped Ultra Rare Bank Loot!");
			
		}

		for (int i = 0; i < 
				ALWAYSLOOT.length; i++) {//rerun and try again for debug
			GroundItem.createGlobal(player, new Item(
					ALWAYSLOOT[i], 1),
					new Position(deathX + 
							Utility.//debug again
				 			random(10),
				 			deathY + Utility.random(10), 0));
		}//test

		player.send(new SendMessage("Galvek drop's lootation all over the map.", MessageColor.RED));
		Galvek.unregister();
		activated = false;
	}

	public enum SpawnData1 {
		LEVEL_46("lvl 46 wild near Spider Hill", new Position(3135, 3888, 0), new Position(3132, 3881, 0)),
		LEVEL_16("lvl 16 wild near Bone Yard", new Position(3273, 3648, 0), new Position(3267, 3654, 0)),
		LEVEL_51("lvl 51 wild near Rogues Castle", new Position(3266, 3924, 0), new Position(3266, 3927, 0)),
		LEVEL_41("lvl 19 wild near graveyard of shadows", new Position(3197, 3670, 0), new Position(3194, 3666, 0)),
		LEVEL_47("lvl 47 wild near obelisk", new Position(3308, 3892, 0), new Position(3305, 3888, 0)),
		LEVEL_53("lvl 53 wild near scorpia's cave entrance", new Position(3211, 3944, 0), new Position(3208, 3940, 0));

		public final String location;
		public final Position position;
		public final Position tsunami;

		SpawnData1(String location, Position position, Position tsunami) {
			this.location = location;
			this.position = position;
			this.tsunami = tsunami;
		}

		public static SpawnData1 generate() {
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