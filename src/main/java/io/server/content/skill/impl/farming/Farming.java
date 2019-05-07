package io.server.content.skill.impl.farming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import io.server.game.Animation;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.object.GameObject;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class Farming {
	private final Player player;
	private Plant[] plants = new Plant[4];
	private GrassyPatch[] patches = new GrassyPatch[4];
//	private Map<Integer, BitConfigBuilder> configMap = new HashMap<>();

	public Farming(Player player) {
		this.player = player;

		for (int i = 0; i < patches.length; i++)
			if (patches[i] == null)
				patches[i] = new GrassyPatch();
	}

	public void sequence() {
		for (Plant i : plants) {
			if (i != null) {
				i.process(player);
			}
		}
		for (int i = 0; i < patches.length; i++) {
			if (i >= FarmingPatches.values().length)
				break;
			if ((patches[i] != null) && (!inhabited(FarmingPatches.values()[i].x, FarmingPatches.values()[i].y))) {
				patches[i].process(player, i);
			}
		}
	}

	public int config(FarmingPatches patch) {
		if (inhabited(patch.x, patch.y)) {
			for (Plant plant : plants) {
				if (plant != null && plant.getPatch() == patch) {
					return plant.getConfig();
				}
			}
		}

		return patches[patch.ordinal()].stage;
	}

	public void doConfig() {
//		Catherby
//		529(<<0) = Fruit Tree
//		529(<<24) = Herb
//		529(<<16) = Flower
//		529(<<8) = South Allotment
//		529(<<0) = North Allotment
//		511(<<24) = Compost Bin
//		Falador
//		529(<<0) = Tree
//		529(<<24) = Herb
//		529(<<16) = Flower
//		529(<<8) = South Allotment
//		529(<<0) = North Allotment
//		511(<<0) = Compost Bin
//		Ardougne
//		529(<<24) = Herb
//		529(<<16) = Flower
//		529(<<8) = South Allotment
//		529(<<0) = North Allotment
//		529(<<24) = Compost Bin
//		529(<<0) = Bush
//		Port Phasmatys
//		529(<<24) = Herb
//		529(<<16) = Flower
//		529(<<8) = South Allotment
//		529(<<0) = North Allotment
//		511(<<16) = Compost Bin
//		Lumbridge
//		529(<<0) = Tree
//				Taverley
//		529(<<0) = Tree
//				Varrock
//		529(<<0) = Tree
//		529(<<0) = Bush
//		Gnome Stronghold
//		529(<<0) = Tree
//		529(<<8) = Fruit Tree
//		Tree Gnome Village
//		529(<<0) = Fruit Tree
//		Brimhaven
//		529(<<0) = Fruit Tree
//		Rimmington
//		529(<<0) = Bush
//				Etceteria
//		529(<<0) = Bush
//		Al Kharid
//		529(<<0) = Cactus

		player.send(new SendConfig(529,
				(config(FarmingPatches.SOUTH_FALADOR_HERB) << 24) + (config(FarmingPatches.SOUTH_FALADOR_FLOWER) << 16)
						+ (config(FarmingPatches.SOUTH_FALADOR_ALLOTMENT_SOUTH) << 8)
						+ (config(FarmingPatches.SOUTH_FALADOR_ALLOTMENT_WEST))));
		// old stuff
//		int faladorConfig1 = config(FarmingPatches.SOUTH_FALADOR_ALLOTMENT_SOUTH);
//		int faladorConfig2 = config(FarmingPatches.SOUTH_FALADOR_ALLOTMENT_WEST);
//		int faladorConfig3 = config(FarmingPatches.SOUTH_FALADOR_FLOWER);
//		int faladorConfig4 = config(FarmingPatches.SOUTH_FALADOR_HERB);
//
//
//		player.getPacketSender().sendToggle(504, (faladorConfig1 << 8) + faladorConfig2);
//		player.getPacketSender().sendToggle(508, faladorConfig3);
//		player.getPacketSender().sendToggle(515, faladorConfig4);
	}

	public int getConfigFor(int configId) {
		int config = 0;

		for (FarmingPatches i : FarmingPatches.values()) {
			if (i.config == configId) {
				if (inhabited(i.x, i.y)) {
					for (Plant plant : plants)
						if ((plant != null) && (plant.getPatch().ordinal() == i.ordinal())) {
							config += plant.getConfig();
							break;
						}
				} else {
					config += patches[i.ordinal()].getConfig(i.ordinal());
				}
			}
		}

		return config;
	}

	/*
	 * public void doConfig() { player.send(new SendConfig(529,
	 * (config(FarmingPatches.SOUTH_FALADOR_HERB) << 24) +
	 * (config(FarmingPatches.SOUTH_FALADOR_FLOWER) << 16) +
	 * (config(FarmingPatches.SOUTH_FALADOR_ALLOTMENT_SOUTH) << 8) +
	 * (config(FarmingPatches.SOUTH_FALADOR_ALLOTMENT_WEST))));
	 * 
	 * }
	 */

	public boolean fillWateringCans(int item, GameObject object) {
		if (object.getId() == 6827) {
			int[] cans = { 5331, 5333, 5334, 5335, 5336, 5337, 5338, 5339 };
			int full = 5340;
			for (int can : cans) {
				if (can == item) {
					World.schedule(new Task(3) {
						@Override
						protected void execute() {
							for (int i = 0; i < 28; i++) {
								for (int can1 : cans) {
									if (can1 == player.inventory.get(i).getId()) {
										player.inventory.set(i, new Item(full), true);
										player.animate(new Animation(2295));
//										player.inventory.refresh();
										player.face(object.getPosition());
										player.message("You fill the can with water.");
										return;
									}
								}
							}
							cancel();
						}
					});
					return true;
				}
			}
		}
		return false;
	}

	public void clear() {
		for (int i = 0; i < plants.length; i++) {
			plants[i] = null;
		}

		for (int i = 0; i < patches.length; i++)
			patches[i] = new GrassyPatch();
	}

	public void nextWateringCan(int id) {
		if (id == 18682)
			return;
		player.inventory.remove(id, 1);
		player.inventory.add(id > 5333 ? (id - 1) : (id - 2), 1);
	}

	public void insert(Plant patch) {
		for (int i = 0; i < plants.length; i++)
			if (plants[i] == null) {
				plants[i] = patch;
				break;
			}
	}

	public boolean inhabited(int x, int y) {
		for (int i = 0; i < plants.length; i++) {
			if (plants[i] != null) {
				FarmingPatches patch = plants[i].getPatch();
				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					if (x == 3054 && y == 3307 && patch != FarmingPatches.SOUTH_FALADOR_FLOWER)
						continue;
					return true;
				}
			}
		}

		return false;
	}

	public boolean click(Player player, int x, int y, int option) {
		if (option == 1)
			for (int i = 0; i < FarmingPatches.values().length; i++) {
				FarmingPatches patch = FarmingPatches.values()[i];
				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					if (x == 3054 && y == 3307 && patch != FarmingPatches.SOUTH_FALADOR_FLOWER)
						continue;
					/*
					 * if(patch == FarmingPatches.SOUTH_FALADOR_ALLOTMENT_SOUTH) { player.send(new
					 * SendMessage("This patch is currently disabled.")); return true; }
					 */
					if ((inhabited(x, y)) || (patches[i] == null))
						break;
					patches[i].click(player, option, i);
					return true;
				}

			}
		for (int i = 0; i < plants.length; i++) {
			if (plants[i] != null) {
				FarmingPatches patch = plants[i].getPatch();
				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					if (x == 3054 && y == 3307 && patch != FarmingPatches.SOUTH_FALADOR_FLOWER)
						continue;
					plants[i].click(player, option);
					return true;
				}
			}
		}

		return false;
	}

	public void remove(Plant plant) {
		for (int i = 0; i < plants.length; i++)
			if ((plants[i] != null) && (plants[i] == plant)) {
				patches[plants[i].getPatch().ordinal()].setTime();
				plants[i] = null;
				doConfig();
				return;
			}
	}

	public boolean useItemOnPlant(int item, int x, int y) {
		if (item == 5341) {
			for (int i = 0; i < FarmingPatches.values().length; i++) {
				FarmingPatches patch = FarmingPatches.values()[i];
				if (x == 3054 && y == 3307 && patch != FarmingPatches.SOUTH_FALADOR_FLOWER)
					continue;
				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					if (x == 3054 && y == 3307 && patch != FarmingPatches.SOUTH_FALADOR_FLOWER)
						continue;
					player.action.execute(patches[i].raking(player, i));
					break;
				}
			}
			return true;
		}
		for (int i = 0; i < plants.length; i++) {
			if (plants[i] != null) {
				FarmingPatches patch = plants[i].getPatch();
				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					if (x == 3054 && y == 3307 && patch != FarmingPatches.SOUTH_FALADOR_FLOWER)
						continue;
					plants[i].useItemOnPlant(player, item);
					return true;
				}
			}
		}

		return false;
	}

	public boolean plant(int seed, int x, int y) {
		if (!Plants.isSeed(seed)) {
			return false;
		}

		for (FarmingPatches patch : FarmingPatches.values()) {
			if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
				if (x == 3054 && y == 3307 && patch != FarmingPatches.SOUTH_FALADOR_FLOWER)
					continue;
				if (!patches[patch.ordinal()].isRaked()) {
					player.send(new SendMessage("This patch needs to be raked before anything can grow in it."));
					return true;
				}

				for (Plants plant : Plants.values()) {
					if (plant.seed == seed) {
						if (player.skills.getLevel(Skill.FARMING) >= plant.level) {
							if (inhabited(x, y)) {
								player.send(new SendMessage("There are already seeds planted here."));
								return true;
							}

							if (patch.seedType != plant.type) {
								player.send(new SendMessage("You can't plant this type of seed here."));
								return true;
							}

							if (player.inventory.contains(patch.planter)) {
								player.animate(new Animation(2291));
								player.send(new SendMessage("You bury the seed in the dirt."));
								player.inventory.remove(new Item(seed), 1, true);
								Plant planted = new Plant(patch.ordinal(), plant.ordinal());
								planted.setTime();
								insert(planted);
								doConfig();
								player.skills.addExperience(Skill.FARMING, (int) plant.plantExperience);
							} else {
								String name = ItemDefinition.get(patch.planter).getName();
								player.send(new SendMessage(
										"You need " + Utility.getAOrAn(name) + " " + name + " to plant seeds."));
							}

						} else {
							player.send(
									new SendMessage("You need a Farming level of " + plant.level + " to plant this."));
						}

						return true;
					}
				}

				return false;
			}
		}

		return false;
	}

	public Plant[] getPlants() {
		return plants;
	}

	public void setPlants(Plant[] plants) {
		this.plants = plants;
	}

	public GrassyPatch[] getPatches() {
		return patches;
	}

	public void setPatches(GrassyPatch[] patches) {
		this.patches = patches;
	}

	public void save() {
		try {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter("./data/content/farming/" + player.getUsername() + ".txt"));
			for (int i = 0; i < patches.length; i++) {
				if (i >= FarmingPatches.values().length)
					break;
				if (patches[i] != null) {
					writer.write("[PATCH]");
					writer.newLine();
					writer.write("patch: " + i);
					writer.newLine();
					writer.write("stage: " + patches[i].stage);
					writer.newLine();
					writer.write("time: " + patches[i].time);
					writer.newLine();
					writer.write("END PATCH");
					writer.newLine();   
					writer.newLine();
				}
			}
			for (int i = 0; i < plants.length; i++) {
				if (plants[i] != null) {
					writer.write("[PLANT]");
					writer.newLine();
					writer.write("patch: " + plants[i].patch);
					writer.newLine();
					writer.write("plant: " + plants[i].plant);
					writer.newLine();
					writer.write("stage: " + plants[i].stage);
					writer.newLine();
					writer.write("watered: " + plants[i].watered);
					writer.newLine();
					writer.write("harvested: " + plants[i].harvested);
					writer.newLine();
					writer.write("time: " + plants[i].time);
					writer.newLine();
					writer.write("END PLANT");
					writer.newLine();
					writer.newLine();
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try {
			if (!new File("./data/content/farming/" + player.getUsername() + ".txt").exists())
				return;
			BufferedReader r = new BufferedReader(
					new FileReader("./data/content/farming/" + player.getUsername() + ".txt"));
			int stage = -1, patch = -1, plant = -1, watered = -1, harvested = -1;
			long time = -1;
			while (true) {
				String line = r.readLine();
				if (line == null) {
					break;
				} else {
					line = line.trim();
				}
				if (line.startsWith("patch"))
					patch = Integer.valueOf(line.substring(line.indexOf(":") + 2));
				else if (line.startsWith("stage"))
					stage = Integer.valueOf(line.substring(line.indexOf(":") + 2));
				else if (line.startsWith("plant"))
					plant = Integer.valueOf(line.substring(line.indexOf(":") + 2));
				else if (line.startsWith("watered"))
					watered = Integer.valueOf(line.substring(line.indexOf(":") + 2));
				else if (line.startsWith("harvested"))
					harvested = Integer.valueOf(line.substring(line.indexOf(":") + 2));
				else if (line.startsWith("time"))
					time = Long.valueOf(line.substring(line.indexOf(":") + 2));
				else if (line.equals("END PATCH") && patch >= 0) {
					patches[patch].stage = (byte) stage;
					patches[patch].time = time;
					patch = -1;
				} else if (line.equals("END PLANT") && patch >= 0) {
					plants[patch] = new Plant(patch, plant);
					plants[patch].watered = (byte) watered;
					plants[patch].stage = (byte) stage;
					plants[patch].harvested = (byte) harvested;
					plants[patch].time = time;
					patch = -1;
				}
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
