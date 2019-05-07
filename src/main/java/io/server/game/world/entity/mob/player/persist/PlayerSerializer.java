package io.server.game.world.entity.mob.player.persist;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.login.LoginResponse;
import io.server.util.Utility;

public final class PlayerSerializer {

	private static final PlayerPersistable perstable = /* Config.FORUM_INTEGRATION ? new PlayerPersistDB() : */new PlayerPersistFile();

	public static void save(Player player) {
		if (player.isBot) {
			return;
		}
		// player save thread
		new Thread(() -> perstable.save(player)).start();
	}

	public static LoginResponse load(Player player, String expectedPassword) {
		if (player.isBot) {
			return LoginResponse.COULD_NOT_COMPLETE_LOGIN;
		}

		return perstable.load(player, expectedPassword);
	}
	
	public static Player loadPlayer(String name) {
		
		final Player other = new Player(name);

		try {
			Path path = PlayerPersistFile.FILE_DIR.resolve(name + ".json");

			if (!Files.exists(path)) 
				return null;
			

			try (Reader reader = new FileReader(path.toFile())) {
				JsonObject jsonReader = (JsonObject) new JsonParser().parse(reader);

				for (PlayerJSONProperty property : PlayerPersistFile.PROPERTIES) {
					if (jsonReader.has(property.label)) {
						if (jsonReader.get(property.label).isJsonNull())
							continue;
						property.read(other, jsonReader.get(property.label));
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return other;
	}
	
	public static boolean saveOffline(Player player) {
		
		new Thread(() -> {
			try {
				JsonObject properties = new JsonObject();

				for (PlayerJSONProperty property : PlayerPersistFile.PROPERTIES) {
					properties.add(property.label, PlayerPersistFile.GSON.toJsonTree(property.write(player)));
				}

				try {
					Files.write(PlayerPersistFile.FILE_DIR.resolve(player.getName() + ".json"), PlayerPersistFile.GSON.toJson(properties).getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				System.out.println("error savin..");
			}

			player.saved.set(true);
		}).start();
		return true;
	}
	
	public static boolean saveExists(String name) {
		return Files.exists(Paths.get("./data/profile/save/"+Utility.formatUsername(name)+".json"));
	}
}
