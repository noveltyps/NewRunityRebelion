package io.server.content.eventboss;

import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;

/**
 * 
 * @author Adam_#6723
 * 
 */

public class EventBossHandler {

	public static void openGalvek(Player player) {
		sendStringsGalvek(player);
	}

	public static void sendStringsGalvek(Player player) {
		World.sendTeleportButtonNpc(8095);
		World.sendTeleportButton();
		player.send(new SendString("@whi@Galvek", 45604, true));
		player.send(new SendString("608", 45610, true)); // combat level/
		player.send(new SendString("1200", 45610, true)); // hit points
		player.send(new SendString("Range, Melee", 45613, true)); // Weaknesses 45614
		player.send(new SendString("Melee,\\nDragon Breath", 45614, true)); // Attack Styles
		player.send(new SendString(
				"Galvek is a boss that spawns hourly, He drops various\\nloots @whi@all over the ground, "
						+ "@lre@take an anti-fire shield with you!"
						+ "\\nHe spawns in deep wilderness, so be on high alert!"
						+ "\\nHe has a special attack which burns its enemies, "
						+ "\\nRecommended to take a group to take galvek down!",
				45616, true)); // Information of the boss (long text)
	}

	public static void openArena(Player player) {
		sendStringsArena(player);
	}

	public static void sendStringsArena(Player player) {
		World.sendTeleportButtonNpc1(5129);
		World.sendTeleportButton1();
		player.send(new SendString("@whi@Glod", 46604, true));
		player.send(new SendString("806", 46610, true)); // combat level/
		player.send(new SendString("1150", 46610, true)); // hit points
		player.send(new SendString("Magic", 46613, true)); // Weaknesses 45614
		player.send(new SendString("Melee,\\nCrush Special", 46614, true)); // Attack Styles
		player.send(
				new SendString("Glod is a boss that spawns hourly, He drops juicy\\nloots @whi@all over the ground, "
						+ "@lre@he hits really high, but is slow!" + "\\nHe spawns in a safe location."
						+ "\\nGlod has a special attack which teleports its enemies, "
						+ "\\nRecommended to take a group to take Glod down!", 46616, true)); // Information of the boss
																								// (long text)
	}

	public static void openPorazdir(Player player) {
		sendPorazdirStrings(player);
	}

	public static void sendPorazdirStrings(Player player) {
		World.sendTeleportButtonNpc2(7860);
		World.sendTeleportButton2();
		player.send(new SendString("@whi@Porazdir", 47604, true));
		player.send(new SendString("235", 47610, true)); // combat level/
		player.send(new SendString("500", 47611, true)); // hit points
		player.send(new SendString("23", 47612, true)); // hit points
		player.send(new SendString("Magic", 47613, true)); // Weaknesses 45614
		player.send(new SendString("Melee,\\nCrush Special", 47614, true)); // Attack Styles
		player.send(new SendString("Porazdir is a boss that drops Torva!"
				+ "@lre@\\nhe hits really high, but is slow!" + "\\nHe spawns in a safe location."
				+ "\\nGlod has a special attack which teleports its enemies, "
				+ "\\nRecommended to take a group to take Glod down!", 47616, true)); // Information of the boss (long
																						// text)
	}

	public static void openSkotizo(Player player) {
		sendSkotizoStrings(player);
	}

	public static void sendSkotizoStrings(Player player) {
		World.sendTeleportButtonNpc3(7286);
		World.sendTeleportButton3();
		player.send(new SendString("@whi@Skotizo", 48604, true));
		player.send(new SendString("321", 48610, true)); // combat level/
		player.send(new SendString("1500", 48610, true)); // hit points
		player.send(new SendString("Range", 48613, true)); // Weaknesses 45614
		player.send(new SendString("Melee,\\nCrush Special", 48614, true)); // Attack Styles
		player.send(new SendString("Skotizo is a boss that drops High tier loot"
				+ "@lre@\\nhe hits really high, but is slow!" + "\\nHe spawns in a dangerous location."
				+ "\\nSkotizo has a special attack which teleports its enemies, "
				+ "\\nRecommended to take a group to take Skotizo down!", 48616, true)); // Information of the boss
																							// (long text)
	}

}