package io.server.content.referral;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;


/**
 * 
 * @author Adam_#6723
 *
 */
public class ReferralHandler {

	public static final int[] DEFAULT = { 3080 };

	public static void open(Player player) {
		getPlayerList();
		drawStartList(player);
		player.send(new SendString(null, 59108, true));
		player.interfaceManager.open(59100);
	}

	private static File charDir = new File("./data/profile/save/");

	private static Map<Integer, String> names = new HashMap<Integer, String>();

	private static int buttonId = -6420, startId = -6420;

	public static void getPlayerList() {
		if (charDir.exists() && charDir.isDirectory()) {
			File[] charFiles = charDir.listFiles();
			for (int i = 0; i < charFiles.length; i++) {
				if (charFiles[i].getName().replaceAll(".json", "") == null
						|| names.containsValue(charFiles[i].getName().replaceAll(".json", ""))) {
					continue;
				}
				names.put(buttonId++, charFiles[i].getName().replaceAll(".json", ""));
			}
		}
	}

	public static void drawStartList(Player player) {
		int size = names.size() < 14 ? 14 : names.size();
		for (int index = 0, string = 59516; index < size; index++, string++) {
			String name = index >= names.size() ? "" : names.get(startId + index);
			player.send(new SendString(name, string));
		}
	}

	public static void drawList(Player player, String context) {
		for (int i = -6420; i < names.size(); i++) {
			if (names.get(i) == null || !names.get(i).toLowerCase().contains(context.toLowerCase())) {
				return;
			}
		}
		for (int index = 0, string = 59516; index < names.size(); index++, string++) {
			String name = index >= names.size() ? "" : names.get(index);
			player.send(new SendString(name, string));
		}
	}

}
