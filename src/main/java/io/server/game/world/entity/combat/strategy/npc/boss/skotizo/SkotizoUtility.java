package io.server.game.world.entity.combat.strategy.npc.boss.skotizo;

import io.server.content.discord.DiscordConstant;
import io.server.content.discord.DiscordManager;
import io.server.game.world.World;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;
import io.server.util.Utility;

/**
 * Created by Daniel on 2017-12-20.
 */
public class SkotizoUtility {

	public static SpawnData spawn;
	public static boolean activated = false;

	public static Npc generateSpawn() {
		activated = true;
		spawn = SpawnData.generate();
		Npc skotizo = new Npc(7286, spawn.position, 10, Direction.NORTH);
		World.sendMessage("<col=8714E6> Skotizo has just spawned! He is located at " + spawn.location + "!",
				"<col=8714E6> First clan to kill him will be rewarded handsomely!");
		World.sendBroadcast(1, "The Skotizo boss has spawned!" + spawn.location + "!", true);
		World.sendSkotizoInformation();
		skotizo.register();
		skotizo.definition.setRespawnTime(-1);
		skotizo.definition.setAggressive(true);
		skotizo.speak("Darkness is here to penetrate your souls!");
		new DiscordManager(DiscordConstant.PUBLIC_BOSS_EVENTS, "Boss Event", " Skotizo has just spawned! He is located at " + spawn.location + "!" +
				" First clan to kill him will be rewarded handsomely!").log1();
		return skotizo;
	}

	public static void defeated(Npc skotizo, Player player) {
		boolean hasClan = player.clanChannel != null;
        
		if (hasClan) {
			player.clanChannel.getDetails().points += 5;
			player.clanChannel.addExperience(10000);
			World.sendMessage("<col=8714E6> Skotizo has been defeated by " + player.getName() + " !");
			player.clanChannel.message("Hell yeah boys! We just killed Skotizo!! We earned 10,000 EXP & 5 CP.");
		} else {
			World.sendMessage("<col=8714E6> Skotizo has been defeated by " + player.getName()
					+ ", a solo individual with balls of steel!");
		}

		skotizo.unregister();
		activated = false;
	}

	public enum SpawnData {
		LEVEL_18("lvl 18 wild near east dragons", new Position(3323, 3669, 0)),
		LEVEL_19("lvl 19 wild near obelisk", new Position(3222, 3658, 0)),
		LEVEL_28("lvl 28 wild near venenatis", new Position(3308, 3737, 0)),
		LEVEL_41("lvl 41 wild near callisto", new Position(3275, 3825, 0)),
		LEVEL_52("lvl 52 wild near gdz", new Position(3313, 3893, 0)),
		LEVEL_53("lvl 53 wild near scorpia's cave entrance", new Position(3211, 3944, 0));

		public final String location;
		public Position position;

		SpawnData(String location, Position position) {
			this.location = location;
			this.position = position;
		}

		public static SpawnData generate() {
			return Utility.randomElement(values());
		}

		public String getLocation() {
			return location;
		}

		public Position getPosition() {
			return position;
		}
	}
}
