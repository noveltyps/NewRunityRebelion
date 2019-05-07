package io.server.content.skill.impl.agility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.server.Config;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.skill.impl.agility.obstacle.Obstacle;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.position.Position;

/**
 * Created by Daniel on 2017-11-02.
 */
public class Agility {
	public static final int GNOME_FLAGS = 0b0111_1111;
	public static final int BARBARIAN_FLAGS = 0b0111_1111;
	public static final int WILDERNESS_FLAGS = 0b0001_1111;
	public static final HashMap<Position, Obstacle> obstacles = new HashMap<>();

	public static void declare() {
		try {
			Obstacle[] loaded = new Gson().fromJson(
					new BufferedReader(new FileReader("./data/content/skills/agility.json")), Obstacle[].class);
			for (Obstacle obstacle : loaded) {
				if (obstacle.getObjectPosition() == null) {
					obstacle.setObjectPosition(obstacle.getStart());
				}
				if (obstacle.getType() == null) {
					System.out.println(obstacle + " No object type found. FIX MUST FIX");
				}
				obstacles.put(obstacle.getObjectPosition(), obstacle);
			}
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private final static double TICKET_EXPERIENCE = 2.5 * Config.AGILITY_MODIFICATION;


	public static boolean clickButton(Player player, int button) {
		int amount = -1;
		Consumer<Player> onClick = null;

		switch (button) {
		case 8387:
			amount = 1;
			onClick = p -> p.skills.addExperience(Skill.AGILITY,
					(TICKET_EXPERIENCE * 1) * new ExperienceModifier(p).getModifier());
			break;
		case 8389:
			amount = 10;
			onClick = p -> p.skills.addExperience(Skill.AGILITY,
					(TICKET_EXPERIENCE * 10) * new ExperienceModifier(p).getModifier());
			break;
		case 8390:
			amount = 25;
			onClick = p -> p.skills.addExperience(Skill.AGILITY,
					(TICKET_EXPERIENCE * 25) * new ExperienceModifier(p).getModifier());
			break;
		case 8391:
			amount = 100;
			onClick = p -> p.skills.addExperience(Skill.AGILITY,
					(TICKET_EXPERIENCE * 100) * new ExperienceModifier(p).getModifier());
			break;
		case 8392:
			amount = 1_000;
			onClick = p -> p.skills.addExperience(Skill.AGILITY,
					(TICKET_EXPERIENCE * 1000) * new ExperienceModifier(p).getModifier());
			break;
			
		case 8382:
			amount = 3;
			onClick = p -> p.inventory.addOrDrop(new Item(3049, 1));
			break;
		case 8383:
			amount = 10;
			onClick = p -> p.inventory.addOrDrop(new Item(3051, 1));
			break;
		case 8381:
			amount = 800;
			onClick = p -> p.inventory.addOrDrop(new Item(2997, 1));
			break;
		}
		if (amount > -1) {
			if (player.inventory.contains(2996, amount)) {
				player.inventory.remove(2996, amount);
				onClick.accept(player);
				player.interfaceManager.close();
				return true;
			} else {
				player.message("You do not have enough agility tickets to purchase that.");
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		// Write the obstacles so we don't have to deal with ugly as fuck wall
		// of code.
//		 obstacles.put(new Location(2551, 3554), new Obstacle.ObstacleBuilder(ObstacleType.ROPE_SWING, new ObeliskData(2551, 3554), new ObeliskData(2551, 3549))
//		 .setExperience(22f)
//		 .setLevel(35)
//		 .setOrdinal(0)
//		 .setNext(new Obstacle.ObstacleBuilder(ObstacleType.ROPE_SWING, new Location(2551, 3554), new ObeliskData(2551, 3549)).build())
//		 .build());

		try (FileWriter writer = new FileWriter(new File("./data/def/skills/agility1.json"))) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();

			writer.write(builder.toJson(obstacles.values()).replaceAll("\\{\n      \"x\"", "\\{ \"x\"")
					.replaceAll(",\n      \"y\"", ", \"y\"").replaceAll(",\n      \"z\"", ", \"z\"")
					.replaceAll("\n    \\},", " \\},"));
		} catch (Exception e) {
		}
	}

}
