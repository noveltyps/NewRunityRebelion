package io.server.content.freeforall;

import java.util.HashMap;
import java.util.Map;

import io.server.content.freeforall.impl.FreeForAllEndTask;
import io.server.content.freeforall.impl.FreeForAllInterface;
import io.server.content.freeforall.impl.FreeForAllStartTask;
import io.server.content.freeforall.impl.FreeForAllTask;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class FreeForAll extends Task {

	private int tick;

	public FreeForAll() {
		super(1);
		this.tick = 0;
	}

	@Override
	protected void execute() {
		if (gameStarted) {
			appendTask(new FreeForAllInterface(), FreeForAllType.GAME,
					"" + String.valueOf("--:--") + " " + FreeForAllType.GAME.name() + " " + gameCount + "");
			appendTask(new FreeForAllEndTask(), FreeForAllType.GAME, "");
		}

		switch (tick) {
		case 100:
			if (!gameStarted) {
				gear = getGear();
				World.sendBroadcast(10, "[Free For All] " + gear.getName() + " game will start in 10 Minutes!", true);
			}
			break;
		case 300:
			if (!gameStarted) {
				if (lobbyCount < 1) {
					this.tick = 0;
					message(FreeForAllType.LOBBY, "@red@ [Free For All] Couldn't start to low participant count!");
					return;
				}

				this.tick = 0;
				gameStarted = true;
				appendTask(new FreeForAllStartTask(), FreeForAllType.LOBBY, "");

				break;
			}
		}

		if (!gameStarted) {
			appendTask(new FreeForAllInterface(), FreeForAllType.LOBBY,
					"" + Utility.getTime(300 - tick) + " " + FreeForAllType.LOBBY.name() + " " + lobbyCount + "");
		}

		tick++;

		//System.out.println("Count: " + tick);
	}

	private static void appendTask(FreeForAllTask task, FreeForAllType type, String content) {
		KEY_MAP.keySet().forEach(player -> {
			if (player != null) {
				if (task != null) {
					if (KEY_MAP.get(player).equals(type)) {
						task.execute(player, content);
					}
				}
			}
		});
	}

	private static void message(FreeForAllType type, String content) {
		KEY_MAP.keySet().forEach(player -> {
			if (KEY_MAP.get(player).equals(type)) {
				player.send(new SendMessage(content));
			}
		});
	}
	
	public static FreeForAllType getType(Player player) {
		return KEY_MAP.get(player);
	}

	public static FreeForAllData getGear() {
		return FreeForAllData.values()[Utility.random(FreeForAllData.values().length)];
	}

	public static FreeForAllData gear;
	public static boolean gameStarted;
	public static int lobbyCount, gameCount;
	public static Map<Player, FreeForAllType> KEY_MAP = new HashMap<Player, FreeForAllType>();

}
